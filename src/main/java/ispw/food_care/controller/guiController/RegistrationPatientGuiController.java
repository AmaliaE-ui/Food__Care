package ispw.food_care.controller.guiController;

import ispw.food_care.bean.PatientBean;
import ispw.food_care.dao.PatientDAO;
import ispw.food_care.dao.UserDAO;
import ispw.food_care.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import ispw.food_care.utils.FieldValidator;

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
        if (!validateFields()) return;

        try {
            PatientBean bean = new PatientBean();
            bean.setName(nameTextField.getText());
            bean.setSurname(surnameTextField.getText());
            bean.setPhoneNumber(phoneTextField.getText());
            bean.setUsername(usernameTextField.getText());
            bean.setEmail(emailTextField.getText());
            bean.setPassword(passwordTextField.getText());
            bean.setRole("PATIENT");
            bean.setBirthDate(birthDatePicker.getValue().toString());
            bean.setGender(genderChoiceBox.getValue());

            new UserDAO().saveUser(bean, "PATIENT");
            new PatientDAO().savePatient(bean);

            // Mostra messaggio verde
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Registrazione avvenuta con successo!");

            // Attendi 5 secondi e poi torna al login
            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
            delay.setOnFinished(e -> NavigationManager.switchScene(event, "/ispw/food_care/Login/login.fxml", "FoodCare - Login"));
            delay.play();

        } catch (SQLException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Errore: " + e.getMessage());
        }catch (DateTimeParseException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Formato data non valido. Usa GG/MM/AAAA.");
        }
    }

    private boolean validateFields() {
        if (FieldValidator.isEmpty(nameTextField.getText())) {
            errorLabel.setText("Il nome è obbligatorio.");
            return false;
        }
        if (FieldValidator.isEmpty(surnameTextField.getText())) {
            errorLabel.setText("Il cognome è obbligatorio.");
            return false;
        }
        if (FieldValidator.isEmpty(usernameTextField.getText())) {
            errorLabel.setText("Lo username è obbligatorio.");
            return false;
        }
        if (FieldValidator.isEmpty(emailTextField.getText())) {
            errorLabel.setText("L'email è obbligatoria.");
            return false;
        }
        if (!FieldValidator.isEmailValid(emailTextField.getText())) {
            errorLabel.setText("Email non valida.");
            return false;
        }
        if (FieldValidator.isEmpty(phoneTextField.getText())) {
            errorLabel.setText("Il numero di telefono è obbligatorio.");
            return false;
        }
        if (!FieldValidator.isPhoneValid(phoneTextField.getText())) {
            errorLabel.setText("Numero di telefono non valido.");
            return false;
        }
        if (FieldValidator.isEmpty(passwordTextField.getText()) || FieldValidator.isEmpty(confPasswordTextField.getText())) {
            errorLabel.setText("Inserisci e conferma la password.");
            return false;
        }
        if (!FieldValidator.doPasswordsMatch(passwordTextField.getText(), confPasswordTextField.getText())) {
            errorLabel.setText("Le password non corrispondono.");
            return false;
        }
        if (birthDatePicker.getValue() == null) {
            errorLabel.setText("La data di nascita è obbligatoria.");
            return false;
        }
        if (genderChoiceBox.getValue() == null || genderChoiceBox.getValue().isEmpty()) {
            errorLabel.setText("Seleziona un genere.");
            return false;
        }
        return true;
    }

    private String parseDate(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(input, inputFormatter);
        return date.toString(); // restituisce nel formato yyyy-MM-dd
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        ispw.food_care.utils.NavigationManager.switchScene(event, "/ispw/food_care/Login/login.fxml", "FoodCare - Login");
    }
}
