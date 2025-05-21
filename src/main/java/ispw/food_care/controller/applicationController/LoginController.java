package ispw.food_care.controller.applicationController;

import ispw.food_care.bean.LoginBean;
import ispw.food_care.dao.LoginDAO;


public class LoginController {
    public static boolean validateLogin(String username, String password) {
        LoginBean bean = new LoginBean();
        bean.setUsername(username);
        bean.setPassword(password);

        LoginDAO dao = new LoginDAO();
        return dao.checkCredentials(bean);
    }
}
