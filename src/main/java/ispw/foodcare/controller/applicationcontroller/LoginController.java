package ispw.foodcare.controller.applicationcontroller;

import ispw.foodcare.bean.UserBean;
import ispw.foodcare.dao.UserDAO;
import ispw.foodcare.model.Session;
import ispw.foodcare.model.User;
import ispw.foodcare.utils.Converter;
import ispw.foodcare.utils.ShowAlert;

import java.util.logging.Logger;

public class LoginController {

    private static final LoginController instance = new LoginController();
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    /*Prendo il DAO inizializzato in Main*/
    private static final UserDAO userDAO = Session.getInstance().getUserDAO();

    private LoginController() {
    }

    public static LoginController getInstance() {
        return instance;
    }

    /*Autenticazione utente con username/password
     *Ritorno un UserBean se le credenziali sono valide, altrimenti null*/
    public UserBean authenticateUser(String username, String password) {
        try {
            User user = userDAO.checkLogin(username, password);
            if (user == null) { /*Nessun utente trovato*/
                return null;
            }
            return Converter.userToBean(user);
        } catch (Exception e) {
            logger.severe("Login fallito: " + e.getMessage());
            return null;
        }
    }
}
