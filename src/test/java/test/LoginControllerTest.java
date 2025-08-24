package test;

import ispw.foodcare.Role;
import ispw.foodcare.controller.applicationcontroller.LoginController;
import ispw.foodcare.dao.UserDAO;
import ispw.foodcare.model.Session;
import ispw.foodcare.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Test del metodo LoginController.authenticateUser(...) */
class LoginControllerTest {

    private Session s;
    private UserDAO userDAO;
    private LoginController controller;

    @BeforeEach
    void setUp() throws Exception {
        s = Session.getInstance();
        s.setRam(true); s.setDB(false); s.setFs(false);

        userDAO = s.getUserDAO();
        controller = new LoginController(userDAO);

        /*RAM*/
        userDAO.saveUser(new User(
                "mario", "pwd", "Mario", "Rossi",
                "mario@test.it", "333123", Role.PATIENT));
    }

    @Test
    void authenticateUser_success() {
        var bean = controller.authenticateUser("mario", "pwd");
        assertNotNull(bean);
        assertEquals("mario", bean.getUsername());
    }

    @Test
    void authenticateUser_wrongPassword_returnsNull() {
        assertNull(controller.authenticateUser("mario", "WRONG"));
    }

    @Test
    void authenticateUser_blankParams_throwIAE() {
        assertThrows(IllegalArgumentException.class,
                () -> controller.authenticateUser(" ", " "));
    }
}
