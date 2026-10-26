package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import models.Cliente;
import models.Endereco;
import models.TipoCliente;

public class ClienteDao {

    public boolean existePorDocumento(String documento) throws SQLException {
        String sql = "SELECT 1 FROM clientes WHERE documento = ?";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existePorEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM clientes WHERE email = ?";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existePorDocumentoExceto(String documento, int id) throws SQLException {
        String sql = "SELECT 1 FROM clientes WHERE documento = ? AND id <> ?";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existePorEmailExceto(String email, int id) throws SQLException {
        String sql = "SELECT 1 FROM clientes WHERE email = ? AND id <> ?";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public Cliente buscarPorId(int id) throws SQLException {
        for (Cliente cliente : listar()) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    public boolean atualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nome = ?, tipo = ?, documento = ?, email = ? WHERE id = ?";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getName());
            ps.setString(2, cliente.getTipo().name());
            ps.setString(3, cliente.getDocumento());
            ps.setString(4, cliente.getEmail());
            ps.setInt(5, cliente.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // Os endereços do cliente são apagados junto (ON DELETE CASCADE).
    // Se o cliente tiver entregas, o Postgres recusa (ON DELETE RESTRICT) com SQLState 23503.
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Cliente> listar() throws SQLException {
        String sql = "SELECT c.id AS cliente_id, c.nome, c.tipo, c.documento, c.email, "
                   + "e.id AS endereco_id, e.rua, e.numero, e.cidade, e.estado, e.cep "
                   + "FROM clientes c LEFT JOIN enderecos e ON e.cliente_id = c.id "
                   + "ORDER BY c.id, e.id";
        Map<Integer, Cliente> porId = new LinkedHashMap<>();

        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int clienteId = rs.getInt("cliente_id");
                Cliente cliente = porId.get(clienteId);
                if (cliente == null) {
                    cliente = new Cliente(
                        clienteId,
                        rs.getString("nome"),
                        TipoCliente.valueOf(rs.getString("tipo")),
                        rs.getString("documento"),
                        rs.getString("email"),
                        new ArrayList<>()
                    );
                    porId.put(clienteId, cliente);
                }
                int enderecoId = rs.getInt("endereco_id");
                if (enderecoId > 0) {
                    cliente.getEnderecos().add(new Endereco(
                        enderecoId,
                        clienteId,
                        rs.getString("estado"),
                        rs.getString("cidade"),
                        rs.getString("cep"),
                        rs.getString("rua"),
                        rs.getString("numero")
                    ));
                }
            }
        }
        return new ArrayList<>(porId.values());
    }

    public void inserir(Cliente cliente) throws SQLException {
        String sqlCliente = "INSERT INTO clientes (nome, tipo, documento, email) VALUES (?, ?, ?, ?) RETURNING id";
        String sqlEndereco = "INSERT INTO enderecos (cliente_id, rua, numero, cidade, estado, cep) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionService.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psCliente = con.prepareStatement(sqlCliente);
                 PreparedStatement psEndereco = con.prepareStatement(sqlEndereco)) {
                psCliente.setString(1, cliente.getName());
                psCliente.setString(2, cliente.getTipo().name());
                psCliente.setString(3, cliente.getDocumento());
                psCliente.setString(4, cliente.getEmail());
                int id;
                try (ResultSet rs = psCliente.executeQuery()) {
                    rs.next();
                    id = rs.getInt(1);
                }
                if (cliente.getEnderecos() != null) {
                    for (Endereco endereco : cliente.getEnderecos()) {
                        psEndereco.setInt(1, id);
                        psEndereco.setString(2, endereco.getRua());
                        psEndereco.setString(3, endereco.getNumero());
                        psEndereco.setString(4, endereco.getCidade());
                        psEndereco.setString(5, endereco.getEstado());
                        psEndereco.setString(6, endereco.getCep());
                        psEndereco.addBatch();
                    }
                    psEndereco.executeBatch();
                }
                con.commit();
                cliente.setId(id);
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        }
    }
}