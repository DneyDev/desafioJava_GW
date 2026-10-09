package db;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import models.Entrega;
import models.ItemEntrega;
import models.Cliente;
import models.Endereco;

public class EntregaDao {

    public Set<String> codigosPorUf(String uf) throws SQLException {
        String sql = "SELECT codigo_rastreio FROM entregas WHERE codigo_rastreio LIKE ?";
        Set<String> codigos = new HashSet<>();
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + uf);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) codigos.add(rs.getString(1));
            }
        }
        return codigos;
    }

    public void salvar(Entrega entrega) throws SQLException {
        String sqlEntrega = "INSERT INTO entregas (cliente_id, endereco_id, codigo_rastreio, status) "
                        + "VALUES (?, (SELECT id FROM enderecos WHERE cliente_id = ?), ?, ?) RETURNING id";
        String sqlItem = "INSERT INTO itens_entrega (entrega_id, produto_id, quantidade, preco_unitario) "
                    + "VALUES (?, ?, ?, ?)";

        try (Connection con = ConnectionService.getConnection()) {
            con.setAutoCommit(false);
            try (
                PreparedStatement psEnt = con.prepareStatement(sqlEntrega);
                PreparedStatement psItem = con.prepareStatement(sqlItem)
            ){

                int clienteId = entrega.getCliente().getId();
                psEnt.setInt(1, clienteId);
                psEnt.setInt(2, clienteId);
                psEnt.setString(3, entrega.getIdRastreio());
                psEnt.setString(4, entrega.getStatus());
                int entregaId;
                try (ResultSet rs = psEnt.executeQuery()) {
                    rs.next();
                    entregaId = rs.getInt(1);
                }

                for (ItemEntrega item : entrega.getItens()) {
                    psItem.setInt(1, entregaId);
                    psItem.setInt(2, item.getProduto().getCodigo());
                    psItem.setInt(3, item.getQuantidade());
                    psItem.setBigDecimal(4, item.getPrecoUnitario());
                    psItem.addBatch();
                }
                psItem.executeBatch();

                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        }
    }
    public List<Entrega> listarTodas() throws SQLException{
        String sql = "SELECT en.id AS entrega_id, en.codigo_rastreio, en.status, "
           + "c.id AS cliente_id, c.nome, c.cpf, c.email, "
           + "e.rua, e.numero, e.cidade, e.estado, e.cep "
           + "FROM entregas en "
           + "JOIN clientes c ON c.id = en.cliente_id "
           + "JOIN enderecos e ON e.id = en.endereco_id "
           + "ORDER BY en.id";
        List<Entrega> lista = new ArrayList<>();
        try(Connection con = ConnectionService.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while(rs.next()){
                Endereco endereco = new Endereco(
                    rs.getString("estado"), rs.getString("cidade"), rs.getString("cep"),
                    rs.getString("rua"), rs.getString("numero")
                );
                Cliente cliente = new Cliente(
                    rs.getInt("cliente_id"), rs.getString("nome"), 
                    rs.getString("cpf"), rs.getString("email"), endereco
                );
                Entrega entrega = new Entrega(
                    rs.getInt("entrega_id"),
                    rs.getString("codigo_rastreio"),
                    cliente
                );
                lista.add(entrega);
            }
        }
        return lista;
    }
}