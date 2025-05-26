package ispw.foodcare.controller.applicationcontroller;

import ispw.foodcare.bean.LoginBean;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.dao.LoginDAO;
import ispw.foodcare.dao.UserDAO;


public class LoginController {
    private static final LoginController instance = new LoginController();

    private LoginController() {}

    public static LoginController getInstance() {
        return instance;
    }

    public static UserBean authenticateUser(String username, String password) {
        return new UserDAO().checkLogin(username, password);
    }
}
