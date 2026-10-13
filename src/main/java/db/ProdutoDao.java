/* 
ALIAS LIST

ps = PreparedStatement
rs = ResultSet
con = connection
*/

package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Produto;

public class ProdutoDao {

    public void inserir(Produto p) throws SQLException {
        String sql = "INSERT INTO produtos (nome, descricao, preco, peso) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getProdName());
            ps.setString(2, p.getDesc());
            ps.setBigDecimal(3, p.getPrice());
            ps.setBigDecimal(4, p.getPeso());
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){ p.setCodigo(rs.getInt(1)); } //só vai tentar se realmente existir uma linha no banco
            }
        }
    }

    public List<Produto> listar() throws SQLException {
        String sql = "SELECT id, nome, descricao, preco, peso FROM produtos ORDER BY id";
        List<Produto> lista = new ArrayList<>();
        try (Connection con = ConnectionService.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Produto(
                    rs.getInt("id"), rs.getString("nome"),
                    rs.getString("descricao"), rs.getBigDecimal("preco"),
                    rs.getBigDecimal("peso")));
            }
        }
        return lista;
    }
}