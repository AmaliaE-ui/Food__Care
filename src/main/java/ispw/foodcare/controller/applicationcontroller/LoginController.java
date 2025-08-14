package ispw.foodcare.controller.applicationcontroller;

import ispw.foodcare.bean.UserBean;
import ispw.foodcare.dao.UserDAO;
import ispw.foodcare.model.Session;
import ispw.foodcare.model.User;
import ispw.foodcare.utils.Converter;

public class LoginController {

    private final UserDAO userDAO;

    /* Constructor injection*/
    public LoginController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**/
    public LoginController() {
        var s = Session.getInstance();
        this.userDAO = s.getUserDAO();
    }

    /*Autenticazione utente. Se le credenziali sono errate ritorna null*/
    public UserBean authenticateUser(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Username e password sono obbligatori.");
        }

        User user = userDAO.checkLogin(username, password); // il DAO fa il check credenziali
        return (user == null) ? null : Converter.userToBean(user);
    }
}
