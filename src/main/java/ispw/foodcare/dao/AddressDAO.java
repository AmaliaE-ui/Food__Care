package ispw.foodcare.dao;

import ispw.foodcare.bean.AddressBean;
import ispw.foodcare.query.QueryAddress;

import java.sql.*;

public class AddressDAO {

    public int saveAddress(AddressBean bean) throws SQLException {

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAddress.insert_address, Statement.RETURN_GENERATED_KEYS);) {

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
