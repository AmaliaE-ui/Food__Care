package ispw.foodcare.controller.guicontroller;

import ispw.foodcare.Role;
import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.controller.applicationcontroller.RegistrationController;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeParseException;

public class RegistrationPatientGuiController {

    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField usernameTextField;
    @FXML private TextField emailTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private PasswordField confPasswordTextField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ChoiceBox<String> genderChoiceBox;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        genderChoiceBox.getItems().addAll("Maschio", "Femmina", "Altro");
    }

    @FXML
    public void handleSaveButton(ActionEvent event) {
        try {
            PatientBean bean = new PatientBean();
            bean.setName(nameTextField.getText());
            bean.setSurname(surnameTextField.getText());
            bean.setPhoneNumber(phoneTextField.getText());
            bean.setUsername(usernameTextField.getText());
            bean.setEmail(emailTextField.getText());
            bean.setPassword(passwordTextField.getText());
            bean.setRole(Role.PATIENT);
            if(birthDatePicker.getValue() != null) {
                bean.setBirthDate(birthDatePicker.getValue().toString());
            }
            bean.setGender(genderChoiceBox.getValue());

            //Validazione
            String error = ispw.foodcare.validation.PatientValidator.validatePatient(bean, confPasswordTextField.getText());
            if (error != null) {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText(error);
                return;
            }

            RegistrationController controller = new RegistrationController();
            boolean success = controller.registrationPatient(bean);

            if(success){
                errorLabel.setStyle("-fx-text-fill: green");
                errorLabel.setText("Registrazione avvenuta con successo!");

                // Attendi 3 secondi e poi torna al login
                javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
                delay.setOnFinished(e -> NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login"));
                delay.play();
            } else{
                errorLabel.setStyle("fx-text-fill: red;");
                errorLabel.setText("Errore nella registrazione! Controlla i dati inseriti");
            }

        } catch (Exception e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            if (e instanceof DateTimeParseException){
                errorLabel.setText("Formato data non valido. Usa GG/MM/AAAA.");
            } else {
                errorLabel.setText("Errore: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        ispw.foodcare.utils.NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }
}
