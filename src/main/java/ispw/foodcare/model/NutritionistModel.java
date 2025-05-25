package ispw.foodcare.model;

import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.dao.NutritionistDAO;
import ispw.foodcare.dao.UserDAO;

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
