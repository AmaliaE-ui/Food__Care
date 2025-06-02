package ispw.foodcare.controller.guicontroller;

import ispw.foodcare.Role;
import ispw.foodcare.bean.AddressBean;
import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.controller.applicationcontroller.RegistrationController;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class RegistrationNutritionistGuiController {

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        ispw.foodcare.utils.NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }

    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField usernameTextField;
    @FXML private TextField emailTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private PasswordField confirmPasswordTextField;
    @FXML private TextField pivaTextField;
    @FXML private ChoiceBox<String> titoloStudioChoiceBox;
    @FXML private TextField viaTextField;
    @FXML private TextField civicoTextField;
    @FXML private TextField capTextField;
    @FXML private TextField cittaTextField;
    @FXML private TextField provinciaTextField;
    @FXML private TextField regioneTextField;
    @FXML private ChoiceBox<String> specializzazioneChoiceBox;
    @FXML private Label errorLabel;

    public void handleSaveButton(ActionEvent event) {
        try {
            //Costruzione del bean Address
            AddressBean address = new AddressBean();
            address.setVia(viaTextField.getText());
            address.setCivico(civicoTextField.getText());
            address.setCap(capTextField.getText());
            address.setCitta(cittaTextField.getText());
            address.setProvincia(provinciaTextField.getText());
            address.setRegione(regioneTextField.getText());

            //Costruzione del bean Nutritionist
            NutritionistBean bean = new NutritionistBean();
            bean.setName(nameTextField.getText());
            bean.setSurname(surnameTextField.getText());
            bean.setPhoneNumber(phoneTextField.getText());
            bean.setUsername(usernameTextField.getText());
            bean.setEmail(emailTextField.getText());
            bean.setPassword(passwordTextField.getText());
            bean.setRole(Role.NUTRITIONIST);
            bean.setPiva(pivaTextField.getText());
            bean.setTitoloStudio(titoloStudioChoiceBox.getValue());
            bean.setSpecializzazione(specializzazioneChoiceBox.getValue());

            bean.setAddress(address);

            //Validazione
            String error = ispw.foodcare.validation.NutritionistValidator.validateNutritionist(bean, address,confirmPasswordTextField.getText());
            if (error != null) {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText(error);
                return;
            }

            RegistrationController controller = new RegistrationController();
            controller.register(bean);

            errorLabel.setStyle("-fx-text-fill: green");
            errorLabel.setText("Registrazione avvenuta con successo!");

            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
            delay.setOnFinished(e -> NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login"));
            delay.play();

        } catch (Exception e) {
            errorLabel.setText("-fx-text-fill: red");
            errorLabel.setText("Errore: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        titoloStudioChoiceBox.getItems().addAll(
                "Laurea in Dietistica",
                "Laurea in Biologia",
                "Scienze della Nutrizione",
                "Master in Nutrizione Clinica"
        );

        specializzazioneChoiceBox.getItems().addAll(
                "Sportiva",
                "Pediatrica",
                "Vegana",
                "Disturbi Alimentari",
                "Clinica"
        );
    }
}
