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
                
                Cliente c = porId.get(clienteId);
                if (c == null) {
                    c = new Cliente(
                        clienteId, rs.getString("nome"),
                        TipoCliente.valueOf(rs.getString("tipo")),
                        rs.getString("documento"), rs.getString("email"), 
                        new ArrayList<>()
                    );
                    porId.put(clienteId, c);
                }
                
                int enderecoId = rs.getInt("endereco_id");
                if (enderecoId > 0) {
                    Endereco end = new Endereco(
                        enderecoId, clienteId, rs.getString("estado"), 
                        rs.getString("cidade"), rs.getString("cep"),
                        rs.getString("rua"), rs.getString("numero")
                    );
                    c.getEnderecos().add(end);
                }
            }
        }
        return new ArrayList<>(porId.values());
    }

    public void inserir(Cliente c) throws SQLException {
        String sqlCliente = "INSERT INTO clientes (nome, tipo, documento, email) VALUES (?, ?, ?, ?) RETURNING id";
        String sqlEndereco = "INSERT INTO enderecos (cliente_id, rua, numero, cidade, estado, cep) "
                           + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionService.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psCli = con.prepareStatement(sqlCliente);
                 PreparedStatement psEnd = con.prepareStatement(sqlEndereco)) {

                psCli.setString(1, c.getName());
                psCli.setString(2, c.getTipo().name());
                psCli.setString(3, c.getDocumento());
                psCli.setString(4, c.getEmail());
                int id;
                try (ResultSet rs = psCli.executeQuery()) {
                    rs.next();
                    id = rs.getInt(1);
                }

                if (c.getEnderecos() != null) {
                    for (Endereco e : c.getEnderecos()) {
                        psEnd.setInt(1, id);
                        psEnd.setString(2, e.getRua());
                        psEnd.setString(3, e.getNumero());
                        psEnd.setString(4, e.getCidade());
                        psEnd.setString(5, e.getEstado());
                        psEnd.setString(6, e.getCep());
                        psEnd.addBatch();
                    }
                    psEnd.executeBatch();
                }

                con.commit();
                c.setId(id);
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        }
    }
}