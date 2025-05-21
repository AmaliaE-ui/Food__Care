package ispw.foodcare.controller.applicationcontroller;

import ispw.foodcare.bean.LoginBean;
import ispw.foodcare.dao.LoginDAO;


public class LoginController {
    private LoginController(){} //impedisce l'istanza

    public static boolean validateLogin(String username, String password) {
        LoginBean bean = new LoginBean();
        bean.setUsername(username);
        bean.setPassword(password);

        LoginDAO dao = new LoginDAO();
        return dao.checkCredentials(bean);
    }
}
