package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Endereco;

public class EnderecoDao {

    public List<Endereco> listarPorCliente(int clienteId) throws SQLException {
        String sql = "SELECT id, cliente_id, rua, numero, cidade, estado, cep "
                   + "FROM enderecos WHERE cliente_id = ? ORDER BY id";
        List<Endereco> lista = new ArrayList<>();
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Endereco(
                        rs.getInt("id"),
                        rs.getInt("cliente_id"),
                        rs.getString("estado"),
                        rs.getString("cidade"),
                        rs.getString("cep"),
                        rs.getString("rua"),
                        rs.getString("numero")
                    ));
                }
            }
        }
        return lista;
    }

    public void inserir(Endereco e) throws SQLException {
        String sql = "INSERT INTO enderecos (cliente_id, rua, numero, cidade, estado, cep) "
                   + "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, e.getClienteId());
            ps.setString(2, e.getRua());
            ps.setString(3, e.getNumero());
            ps.setString(4, e.getCidade());
            ps.setString(5, e.getEstado());
            ps.setString(6, e.getCep());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    e.setId(rs.getInt(1));
                }
            }
        }
    }
}