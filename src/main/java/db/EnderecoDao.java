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
        String sql = "SELECT id, cliente_id, rua, numero, cidade, estado, cep, bairro FROM enderecos WHERE cliente_id = ? ORDER BY id";
        List<Endereco> lista = new ArrayList<>();
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Endereco(
                        rs.getInt("id"), rs.getInt("cliente_id"), rs.getString("estado"),
                        rs.getString("cidade"), rs.getString("cep"), rs.getString("rua"), rs.getString("numero"), rs.getString("bairro")
                    ));
                }
            }
        }
        return lista;
    }

    public Endereco buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, cliente_id, rua, numero, cidade, estado, cep, bairro FROM enderecos WHERE id = ?";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Endereco(
                        rs.getInt("id"), rs.getInt("cliente_id"), rs.getString("estado"),
                        rs.getString("cidade"), rs.getString("cep"), rs.getString("rua"), rs.getString("numero"), rs.getString("bairro")
                    );
                }
            }
        }
        return null;
    }

    public boolean atualizar(Endereco endereco) throws SQLException {
        String sql = "UPDATE enderecos SET rua = ?, numero = ?, cidade = ?, estado = ?, cep = ?, bairro = ? WHERE id = ?";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, endereco.getRua());
            ps.setString(2, endereco.getNumero());
            ps.setString(3, endereco.getCidade());
            ps.setString(4, endereco.getEstado());
            ps.setString(5, endereco.getCep());
            ps.setString(6, endereco.getBairro());
            ps.setInt(7, endereco.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public void inserir(Endereco endereco) throws SQLException {
        String sql = "INSERT INTO enderecos (cliente_id, rua, numero, cidade, estado, cep, bairro) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, endereco.getClienteId());
            ps.setString(2, endereco.getRua());
            ps.setString(3, endereco.getNumero());
            ps.setString(4, endereco.getCidade());
            ps.setString(5, endereco.getEstado());
            ps.setString(6, endereco.getCep());
            ps.setString(7, endereco.getBairro());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    endereco.setId(rs.getInt(1));
                }
            }
        }
    }
}