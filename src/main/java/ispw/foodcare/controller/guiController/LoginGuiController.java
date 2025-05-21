package ispw.foodcare.controller.guicontroller;


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


        // Validazione credenziali
        if (LoginController.validateLogin(username, password)) {
            NavigationManager.switchScene(event, "/ispw/foodcare/nutritionistList.fxml", "FoodCare");
        } else {
            loginMessageLabel.setText("Login fallito. Controlla le credenziali.");
        }

    }

    /*Metodo per la Registrazione*/
    @FXML
    private void handleRegistration(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/chooseRole.fxml", "Seleziona ruolo");
    }

}
