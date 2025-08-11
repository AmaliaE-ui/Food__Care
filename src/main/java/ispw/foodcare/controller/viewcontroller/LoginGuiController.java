package ispw.foodcare.controller.viewcontroller;


import ispw.foodcare.Role;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.applicationcontroller.LoginController;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginGuiController {

    @FXML private Button loginButton;
    @FXML private Label loginMessageLabel;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordPasswordField;

    /*Metodo per il Login*/
    @FXML private void handleLogin(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        UserBean userBean = LoginController.getInstance().authenticateUser(username, password);

        if (userBean != null) {
            //Salva nella sessione
            Session.getInstance().setCurrentUser(userBean);

            //Ottieni path corretto dalla GuiFactory
            Role role = userBean.getRole();
            String homePath = ispw.foodcare.utils.factory.GuiFactory.getHomePath(role);

            //Home dinamica
            NavigationManager.switchScene(event, homePath, "FoodCare - Home");
        } else {
            loginMessageLabel.setText("Login faillito");
        }
    }

    /*Metodo per la Registrazione*/
    @FXML private void handleRegistration(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/chooseRole.fxml", "Seleziona ruolo");
    }
}
