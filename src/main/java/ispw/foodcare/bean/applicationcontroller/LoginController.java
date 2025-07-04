package ispw.foodcare.bean.applicationcontroller;

import ispw.foodcare.bean.UserBean;
import ispw.foodcare.model.User;
import ispw.foodcare.utils.Converter;

public class LoginController {

    private static final LoginController instance = new LoginController();
    private static final UserService userService = new UserService(); //Creo istanza di USerService

    private LoginController() {}

    public static LoginController getInstance() {
        return instance;
    }

    public static UserBean authenticateUser(String username, String password) {
        User user = userService.login(username, password);
        if (user != null) {
            return Converter.userToBean(user);
        }
        return null;
    }
}
