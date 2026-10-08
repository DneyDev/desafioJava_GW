package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import models.Entrega;
import models.ItemEntrega;

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
}