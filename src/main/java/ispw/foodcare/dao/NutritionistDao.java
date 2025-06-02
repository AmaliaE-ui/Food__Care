package ispw.foodcare.dao;

import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.query.QueryNutrtionist;

import java.sql.*;

public class NutritionistDao {

    public void saveNutritionist(NutritionistBean bean, int addressID) throws SQLException {
        try(Connection conn = DBManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(QueryNutrtionist.INSERT_NUTRITIONIST)) {

            stmt.setString(1, bean.getUsername());
            stmt.setString(2, bean.getPiva());
            stmt.setString(3, bean.getTitoloStudio());
            stmt.setInt(4, addressID);
            stmt.setString(5, bean.getSpecializzazione());

            stmt.executeUpdate();
        }
    }
}

