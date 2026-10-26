package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Produto;

public class ProdutoDao {

    public boolean existePorNome(String nome) throws SQLException {
        String sql = "SELECT 1 FROM produtos "
                   + "WHERE LOWER(nome) = LOWER(?) AND ativo = TRUE";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    public boolean existePorNomeExceto(String nome, int id) throws SQLException {
        String sql = "SELECT 1 FROM produtos "
                   + "WHERE LOWER(nome) = LOWER(?) "
                   + "AND id <> ? AND ativo = TRUE";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nome);
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    public Produto buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, descricao, preco, peso, unidade, ativo "
                   + "FROM produtos WHERE id = ? AND ativo = TRUE";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("preco"),
                        rs.getBigDecimal("peso"),
                        rs.getString("unidade")
                    );
                    produto.setAtivo(rs.getBoolean("ativo"));
                    return produto;
                }
            }
        }
        return null;
    }
    public boolean atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE produtos "
                   + "SET nome = ?, descricao = ?, preco = ?, peso = ?, unidade = ? "
                   + "WHERE id = ? AND ativo = TRUE";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, produto.getProdName());
            ps.setString(2, produto.getDesc());
            ps.setBigDecimal(3, produto.getPrice());
            ps.setBigDecimal(4, produto.getPeso());
            ps.setString(5, produto.getUnidade());
            ps.setInt(6, produto.getCodigo());

            return ps.executeUpdate() > 0;
        }
    }
    public boolean desativar(int id) throws SQLException {
        String sql = "UPDATE produtos SET ativo = FALSE "
                   + "WHERE id = ? AND ativo = TRUE";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    public boolean reativar(int id) throws SQLException {
        String sql = "UPDATE produtos SET ativo = TRUE "
                   + "WHERE id = ? AND ativo = FALSE";

        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    public void inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos "
                   + "(nome, descricao, preco, peso, unidade) "
                   + "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, produto.getProdName());
            ps.setString(2, produto.getDesc());
            ps.setBigDecimal(3, produto.getPrice());
            ps.setBigDecimal(4, produto.getPeso());
            ps.setString(5, produto.getUnidade());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    produto.setCodigo(rs.getInt(1));
                }
            }
        }
    }
    public List<Produto> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, descricao, preco, peso, unidade, ativo "
                   + "FROM produtos ORDER BY ativo DESC, nome";
        List<Produto> lista = new ArrayList<>();

        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Produto produto = new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getBigDecimal("preco"),
                    rs.getBigDecimal("peso"),
                    rs.getString("unidade")
                );
                produto.setAtivo(rs.getBoolean("ativo"));
                lista.add(produto);
            }
        }
        return lista;
    }
    // Mantém apenas produtos ativos para formulários e novas entregas.
    public List<Produto> listar() throws SQLException {
        String sql = "SELECT id, nome, descricao, preco, peso, unidade "
                   + "FROM produtos WHERE ativo = TRUE ORDER BY id";
        List<Produto> lista = new ArrayList<>();
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getBigDecimal("preco"),
                    rs.getBigDecimal("peso"),
                    rs.getString("unidade")
                ));
            }
        }
        return lista;
    }
}