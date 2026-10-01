package src.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

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
}