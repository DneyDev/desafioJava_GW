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
        String sqlEntrega = "INSERT INTO entregas (cliente_id, origem_nome, origem_tipo, origem_documento, origem_email, origem_rua, origem_numero, origem_cidade, origem_estado, origem_cep, endereco_destino_id, codigo_rastreio, status, origem_bairro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        String sqlItem = "INSERT INTO itens_entrega (entrega_id, produto_id, quantidade, preco_unitario) "
                + "SELECT ?, p.id, ?, ? FROM produtos p WHERE p.id = ? AND p.ativo = TRUE";

        try (Connection con = ConnectionService.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psEntrega = con.prepareStatement(sqlEntrega);
                 PreparedStatement psItem = con.prepareStatement(sqlItem)) {
                psEntrega.setInt(1, entrega.getCliente().getId());
                psEntrega.setString(2, entrega.getRemetente().getName());
                psEntrega.setString(3, entrega.getRemetente().getTipo().name());
                psEntrega.setString(4, entrega.getRemetente().getDocumento());
                psEntrega.setString(5, entrega.getRemetente().getEmail());
                psEntrega.setString(6, entrega.getOrigem().getRua());
                psEntrega.setString(7, entrega.getOrigem().getNumero());
                psEntrega.setString(8, entrega.getOrigem().getCidade());
                psEntrega.setString(9, entrega.getOrigem().getEstado());
                psEntrega.setString(10, entrega.getOrigem().getCep());
                psEntrega.setInt(11, entrega.getDestino().getId());
                psEntrega.setString(12, entrega.getIdRastreio());
                psEntrega.setString(13, entrega.getStatus());
                psEntrega.setString(14, entrega.getOrigem().getBairro());

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
        String sqlEntrega = "UPDATE entregas SET origem_nome = ?, origem_tipo = ?, origem_documento = ?, origem_email = ?, origem_rua = ?, origem_numero = ?, origem_cidade = ?, origem_estado = ?, origem_cep = ?, endereco_destino_id = ?, origem_bairro = ? WHERE id = ? AND codigo_rastreio = ? AND status = 'Pendente'";
        String sqlExcluirItens = "DELETE FROM itens_entrega WHERE entrega_id = ?";
        String sqlInserirItem = "INSERT INTO itens_entrega (entrega_id, produto_id, quantidade, preco_unitario) "
                + "SELECT ?, p.id, ?, ? FROM produtos p WHERE p.id = ? AND p.ativo = TRUE";

        try (Connection con = ConnectionService.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psEntrega = con.prepareStatement(sqlEntrega);
                 PreparedStatement psExcluirItens = con.prepareStatement(sqlExcluirItens);
                 PreparedStatement psInserirItem = con.prepareStatement(sqlInserirItem)) {
                psEntrega.setString(1, entrega.getRemetente().getName());
                psEntrega.setString(2, entrega.getRemetente().getTipo().name());
                psEntrega.setString(3, entrega.getRemetente().getDocumento());
                psEntrega.setString(4, entrega.getRemetente().getEmail());
                psEntrega.setString(5, entrega.getOrigem().getRua());
                psEntrega.setString(6, entrega.getOrigem().getNumero());
                psEntrega.setString(7, entrega.getOrigem().getCidade());
                psEntrega.setString(8, entrega.getOrigem().getEstado());
                psEntrega.setString(9, entrega.getOrigem().getCep());
                psEntrega.setInt(10, entrega.getDestino().getId());
                psEntrega.setString(11, entrega.getOrigem().getBairro());
                psEntrega.setInt(12, entrega.getId());
                psEntrega.setString(13, entrega.getIdRastreio());

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

    // Só apaga se a entrega estiver Cancelada (o filtro está no próprio SQL).
    // Os itens somem junto por causa do ON DELETE CASCADE em itens_entrega.
    public boolean excluirCancelada(String codigoRastreio) throws SQLException {
        String sql = "DELETE FROM entregas WHERE codigo_rastreio = ? AND status = 'Cancelada'";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoRastreio);
            return ps.executeUpdate() > 0;
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
            + "en.origem_nome AS orig_nome, en.origem_tipo AS orig_tipo, en.origem_documento AS orig_documento, en.origem_email AS orig_email, en.origem_rua AS orig_rua, en.origem_numero AS orig_numero, en.origem_cidade AS orig_cidade, en.origem_estado AS orig_estado, en.origem_cep AS orig_cep, en.origem_bairro AS orig_bairro, "
            + "dest.id AS dest_id, dest.cliente_id AS dest_cliente_id, dest.rua AS dest_rua, dest.numero AS dest_numero, dest.cidade AS dest_cidade, dest.estado AS dest_estado, dest.cep AS dest_cep, dest.bairro AS dest_bairro "
            + "FROM entregas en JOIN clientes c ON c.id = en.cliente_id "
            + "JOIN enderecos dest ON dest.id = en.endereco_destino_id ORDER BY en.id";
        String sqlItens = "SELECT ie.entrega_id, ie.quantidade, ie.preco_unitario, p.id, p.nome, p.descricao, p.preco, p.peso, p.unidade "
            + "FROM itens_entrega ie JOIN produtos p ON p.id = ie.produto_id ORDER BY ie.entrega_id";
        List<Entrega> lista = new ArrayList<>();
        Map<Integer, Entrega> porId = new HashMap<>();

        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Endereco origem = new Endereco(
                    rs.getString("orig_estado"), rs.getString("orig_cidade"), rs.getString("orig_cep"),
                    rs.getString("orig_rua"), rs.getString("orig_numero"), rs.getString("orig_bairro")
                );
                Endereco destino = new Endereco(
                    rs.getInt("dest_id"), rs.getInt("dest_cliente_id"), rs.getString("dest_estado"),
                    rs.getString("dest_cidade"), rs.getString("dest_cep"), rs.getString("dest_rua"), rs.getString("dest_numero"), rs.getString("dest_bairro")
                );
                Cliente cliente = new Cliente(
                    rs.getInt("cliente_id"), rs.getString("nome"), TipoCliente.valueOf(rs.getString("tipo")),
                    rs.getString("documento"), rs.getString("email"), new ArrayList<>()
                );
                Entrega entrega = new Entrega(rs.getInt("entrega_id"), rs.getString("codigo_rastreio"), cliente, origem, destino);
                entrega.setStatus(rs.getString("status"));
                entrega.setRemetente(new Cliente(
                    0, rs.getString("orig_nome"), TipoCliente.valueOf(rs.getString("orig_tipo")),
                    rs.getString("orig_documento"), rs.getString("orig_email"), new ArrayList<>()
                ));
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
                        rsItens.getBigDecimal("preco"), rsItens.getBigDecimal("peso"), rsItens.getString("unidade")
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