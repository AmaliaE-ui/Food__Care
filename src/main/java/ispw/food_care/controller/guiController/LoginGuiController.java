package ispw.food_care.controller.guiController;


import java.io.IOException;

import ispw.food_care.controller.applicationController.LoginController;
import ispw.food_care.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.stage.Stage;



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
            NavigationManager.switchScene(event, "/ispw/food_care/nutritionistList.fxml", "FoodCare");
        } else {
            loginMessageLabel.setText("Login fallito. Controlla le credenziali.");
        }

    }

    /*Metodo per la Registrazione*/
    @FXML
    private void handleRegistration(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/food_care/Login/chooseRole.fxml", "Seleziona ruolo");
    }

}

/*
Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/ispw/foodcare/style.css").toExternalForm());
        stage.setScene(scene);

*/
