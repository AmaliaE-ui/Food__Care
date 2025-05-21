package ispw.food_care.dao;

import ispw.food_care.bean.NutritionistBean;
import ispw.food_care.bean.AddressBean;

import java.sql.*;

public class NutritionistDAO{

    public void saveNutritionist(NutritionistBean bean, int addressID) throws SQLException {
        String insertNutritionistSql = "INSERT INTO nutritionist (username, piva, titolo_studio, indirizzo_studio, specializzazione) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DBManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(insertNutritionistSql)) {

            stmt.setString(1, bean.getUsername());
            stmt.setString(2, bean.getPiva());
            stmt.setString(3, bean.getTitoloStudio());
            stmt.setInt(4, addressID);
            stmt.setString(5, bean.getSpecializzazione());

            stmt.executeUpdate();
        }
    }
}

