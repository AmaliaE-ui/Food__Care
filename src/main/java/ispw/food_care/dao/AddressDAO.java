package ispw.food_care.dao;

import ispw.food_care.bean.AddressBean;

import java.sql.*;

public class AddressDAO {

    public int saveAddress(AddressBean bean) throws SQLException {
        String sql = "INSERT INTO address (via, civico, cap, citta, provincia, regione) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, bean.getVia());
            stmt.setString(2, bean.getCivico());
            stmt.setString(3, bean.getCap());
            stmt.setString(4, bean.getCitta());
            stmt.setString(5, bean.getProvincia());
            stmt.setString(6, bean.getRegione());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Errore nella generazione dell'ID indirizzo");
            }
        }
    }
}
