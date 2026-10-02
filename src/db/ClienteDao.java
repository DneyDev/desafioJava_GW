package src.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import src.models.Cliente;
import src.models.Endereco;

public class ClienteDao {

    public boolean existePorCpf(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM clientes WHERE cpf = ?";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Cliente> listar() throws SQLException {
        String sql = "SELECT c.id, c.nome, c.cpf, c.email, "
                   + "e.rua, e.numero, e.cidade, e.estado, e.cep "
                   + "FROM clientes c JOIN enderecos e ON e.cliente_id = c.id "
                   + "ORDER BY c.id";
        List<Cliente> lista = new ArrayList<>();
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Endereco end = new Endereco(
                    rs.getString("estado"), rs.getString("cidade"), rs.getString("cep"),
                    rs.getString("rua"), rs.getString("numero"));
                lista.add(new Cliente(
                    rs.getInt("id"), rs.getString("nome"),
                    rs.getString("cpf"), rs.getString("email"), end));
            }
        }
        return lista;
    }

    public void inserir(Cliente c) throws SQLException {
        String sqlCliente = "INSERT INTO clientes (nome, cpf, email) VALUES (?, ?, ?) RETURNING id";
        String sqlEndereco = "INSERT INTO enderecos (cliente_id, rua, numero, cidade, estado, cep) "
                           + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionService.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psCli = con.prepareStatement(sqlCliente);
                 PreparedStatement psEnd = con.prepareStatement(sqlEndereco)) {

                psCli.setString(1, c.getName());
                psCli.setString(2, c.getCpf());
                psCli.setString(3, c.getEmail());
                int id;
                try (ResultSet rs = psCli.executeQuery()) {
                    rs.next();
                    id = rs.getInt(1);
                }

                Endereco e = c.getEnd();
                psEnd.setInt(1, id);
                psEnd.setString(2, e.getRua());
                psEnd.setString(3, e.getNumero());
                psEnd.setString(4, e.getCidade());
                psEnd.setString(5, e.getEstado());
                psEnd.setString(6, e.getCep());
                psEnd.executeUpdate();

                con.commit();
                c.setId(id);
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        }
    }
}