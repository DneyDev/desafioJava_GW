package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import models.Cliente;
import models.Endereco;
import models.Entrega;
import models.ItemEntrega;
import models.Produto;
import models.TipoCliente;

public class EntregaDao {

    public Set<String> codigosPorUf(String uf) throws SQLException {
        String sql = "SELECT codigo_rastreio FROM entregas WHERE codigo_rastreio LIKE ?";
        Set<String> codigos = new HashSet<>();
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + uf);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    codigos.add(rs.getString(1));
                }
            }
        }
        return codigos;
    }

    public void salvar(Entrega entrega) throws SQLException {
        String sqlEntrega = "INSERT INTO entregas (cliente_id, origem_rua, origem_numero, origem_cidade, origem_estado, origem_cep, endereco_destino_id, codigo_rastreio, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        String sqlItem = "INSERT INTO itens_entrega (entrega_id, produto_id, quantidade, preco_unitario) "
                + "SELECT ?, p.id, ?, ? FROM produtos p WHERE p.id = ? AND p.ativo = TRUE";

        try (Connection con = ConnectionService.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psEntrega = con.prepareStatement(sqlEntrega);
                 PreparedStatement psItem = con.prepareStatement(sqlItem)) {
                psEntrega.setInt(1, entrega.getCliente().getId());
                psEntrega.setString(2, entrega.getOrigem().getRua());
                psEntrega.setString(3, entrega.getOrigem().getNumero());
                psEntrega.setString(4, entrega.getOrigem().getCidade());
                psEntrega.setString(5, entrega.getOrigem().getEstado());
                psEntrega.setString(6, entrega.getOrigem().getCep());
                psEntrega.setInt(7, entrega.getDestino().getId());
                psEntrega.setString(8, entrega.getIdRastreio());
                psEntrega.setString(9, entrega.getStatus());

                int entregaId;
                try (ResultSet rs = psEntrega.executeQuery()) {
                    rs.next();
                    entregaId = rs.getInt(1);
                }

                for (ItemEntrega item : entrega.getItens()) {
                    inserirItemAtivo(psItem, entregaId, item);
                }
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        }
    }

    public Entrega buscarPorCodigo(String codigoRastreio) throws SQLException {
        String codigo = codigoRastreio == null ? "" : codigoRastreio.trim();
        for (Entrega entrega : listarTodas()) {
            if (entrega.getIdRastreio() != null && entrega.getIdRastreio().trim().equals(codigo)) {
                return entrega;
            }
        }
        return null;
    }

    public boolean atualizar(Entrega entrega) throws SQLException {
        String sqlEntrega = "UPDATE entregas SET origem_rua = ?, origem_numero = ?, origem_cidade = ?, origem_estado = ?, origem_cep = ?, endereco_destino_id = ? WHERE id = ? AND codigo_rastreio = ? AND status = 'Pendente'";
        String sqlExcluirItens = "DELETE FROM itens_entrega WHERE entrega_id = ?";
        String sqlInserirItem = "INSERT INTO itens_entrega (entrega_id, produto_id, quantidade, preco_unitario) "
                + "SELECT ?, p.id, ?, ? FROM produtos p WHERE p.id = ? AND p.ativo = TRUE";

        try (Connection con = ConnectionService.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psEntrega = con.prepareStatement(sqlEntrega);
                 PreparedStatement psExcluirItens = con.prepareStatement(sqlExcluirItens);
                 PreparedStatement psInserirItem = con.prepareStatement(sqlInserirItem)) {
                psEntrega.setString(1, entrega.getOrigem().getRua());
                psEntrega.setString(2, entrega.getOrigem().getNumero());
                psEntrega.setString(3, entrega.getOrigem().getCidade());
                psEntrega.setString(4, entrega.getOrigem().getEstado());
                psEntrega.setString(5, entrega.getOrigem().getCep());
                psEntrega.setInt(6, entrega.getDestino().getId());
                psEntrega.setInt(7, entrega.getId());
                psEntrega.setString(8, entrega.getIdRastreio());

                if (psEntrega.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }

                psExcluirItens.setInt(1, entrega.getId());
                psExcluirItens.executeUpdate();
                for (ItemEntrega item : entrega.getItens()) {
                    inserirItemAtivo(psInserirItem, entrega.getId(), item);
                }
                con.commit();
                return true;
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        }
    }

    private void inserirItemAtivo(PreparedStatement psItem, int entregaId, ItemEntrega item) throws SQLException {
        psItem.setInt(1, entregaId);
        psItem.setInt(2, item.getQuantidade());
        psItem.setBigDecimal(3, item.getPrecoUnitario());
        psItem.setInt(4, item.getProduto().getCodigo());
        if (psItem.executeUpdate() == 0) {
            throw new SQLException("Produto inexistente ou inativo.", "P0001");
        }
    }

    public String lerStatusAtual(String codigoRastreio) throws SQLException {
        String sql = "SELECT status FROM entregas WHERE codigo_rastreio = ?";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoRastreio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("status") : null;
            }
        }
    }

    public boolean atualizarStatus(String codigoRastreio, String novoStatus) throws SQLException {
        String sql = "UPDATE entregas SET status = ? WHERE codigo_rastreio = ?";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, novoStatus);
            ps.setString(2, codigoRastreio);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Entrega> listarTodas() throws SQLException {
        String sql = "SELECT en.id AS entrega_id, en.codigo_rastreio, en.status, "
            + "c.id AS cliente_id, c.nome, c.tipo, c.documento, c.email, "
            + "en.origem_rua AS orig_rua, en.origem_numero AS orig_numero, en.origem_cidade AS orig_cidade, en.origem_estado AS orig_estado, en.origem_cep AS orig_cep, "
            + "dest.id AS dest_id, dest.cliente_id AS dest_cliente_id, dest.rua AS dest_rua, dest.numero AS dest_numero, dest.cidade AS dest_cidade, dest.estado AS dest_estado, dest.cep AS dest_cep "
            + "FROM entregas en JOIN clientes c ON c.id = en.cliente_id "
            + "JOIN enderecos dest ON dest.id = en.endereco_destino_id ORDER BY en.id";
        String sqlItens = "SELECT ie.entrega_id, ie.quantidade, ie.preco_unitario, p.id, p.nome, p.descricao, p.preco, p.peso "
            + "FROM itens_entrega ie JOIN produtos p ON p.id = ie.produto_id ORDER BY ie.entrega_id";
        List<Entrega> lista = new ArrayList<>();
        Map<Integer, Entrega> porId = new HashMap<>();

        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Endereco origem = new Endereco(
                    rs.getString("orig_estado"), rs.getString("orig_cidade"), rs.getString("orig_cep"),
                    rs.getString("orig_rua"), rs.getString("orig_numero")
                );
                Endereco destino = new Endereco(
                    rs.getInt("dest_id"), rs.getInt("dest_cliente_id"), rs.getString("dest_estado"),
                    rs.getString("dest_cidade"), rs.getString("dest_cep"), rs.getString("dest_rua"), rs.getString("dest_numero")
                );
                Cliente cliente = new Cliente(
                    rs.getInt("cliente_id"), rs.getString("nome"), TipoCliente.valueOf(rs.getString("tipo")),
                    rs.getString("documento"), rs.getString("email"), new ArrayList<>()
                );
                Entrega entrega = new Entrega(rs.getInt("entrega_id"), rs.getString("codigo_rastreio"), cliente, origem, destino);
                entrega.setStatus(rs.getString("status"));
                lista.add(entrega);
                porId.put(entrega.getId(), entrega);
            }

            try (PreparedStatement psItens = con.prepareStatement(sqlItens);
                 ResultSet rsItens = psItens.executeQuery()) {
                while (rsItens.next()) {
                    Entrega entrega = porId.get(rsItens.getInt("entrega_id"));
                    if (entrega == null) {
                        continue;
                    }
                    Produto produto = new Produto(
                        rsItens.getInt("id"), rsItens.getString("nome"), rsItens.getString("descricao"),
                        rsItens.getBigDecimal("preco"), rsItens.getBigDecimal("peso")
                    );
                    entrega.getItens().add(new ItemEntrega(
                        produto, rsItens.getInt("quantidade"), rsItens.getBigDecimal("preco_unitario")
                    ));
                }
            }
        }
        return lista;
    }
}