package ispw.foodcare.controller.guicontroller;


import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.applicationcontroller.LoginController;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;


public class LoginGuiController {

    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;

    /*Metodo per il Login*/
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        UserBean user = LoginController.getInstance().authenticateUser(username, password);

        if (user != null) {
            //Salva nella sessione
            ispw.foodcare.utils.SessionManager.getInstance().login(user);

            //Ottieni path corretto dalla GuiFactory
            String role = user.getRole();
            String homePath = ispw.foodcare.utils.factory.GuiFactory.getHomePath(role);

            //Home dinamica
            NavigationManager.switchScene(event, homePath, "FoodCare - Home");
        } else {
            loginMessageLabel.setText("Login faillito");
        }
    }

    /*Metodo per la Registrazione*/
    @FXML
    private void handleRegistration(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/chooseRole.fxml", "Seleziona ruolo");
    }

}
