package ispw.food_care.model;

import ispw.food_care.bean.NutritionistBean;
import ispw.food_care.dao.NutritionistDAO;
import ispw.food_care.dao.UserDAO;

import java.sql.SQLException;

public class NutritionistModel {
    private final NutritionistDAO nutritionistDAO = new NutritionistDAO();
    private final UserDAO userDAO = new UserDAO();

    public void registerNutritionist(NutritionistBean bean, int addressID) throws SQLException {
        // Prima salva nella tabella user (con ruolo nutritionist)
        userDAO.saveUser(bean, "NUTRITIONIST");

        // Poi nella tabella nutritionist
        nutritionistDAO.saveNutritionist(bean, addressID);
    }
}
