package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.Role;
import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.controller.applicationcontroller.RegistrationController;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

public class RegistrationPatientGuiController {

    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField usernameTextField;
    @FXML private TextField emailTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ChoiceBox<String> genderChoiceBox;
    @FXML private Label errorLabel;

    private final RegistrationController registrationController;

    public RegistrationPatientGuiController() {
        var s = Session.getInstance();
        this.registrationController = new RegistrationController(s.getUserDAO());
    }


    private static final String STYLE_ERROR   = "-fx-text-fill: red;";
    private static final String STYLE_SUCCESS   = "-fx-text-fill: green;";

    private static final Logger logger = Logger.getLogger(RegistrationPatientGuiController.class.getName());

    @FXML public void initialize() {
        genderChoiceBox.getItems().addAll("Maschio", "Femmina", "Altro");
    }

    @FXML public void onSaveButtonClick(ActionEvent event) {
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
                bean.setBirthDate(birthDatePicker.getValue());
            }
            bean.setGender(genderChoiceBox.getValue());

            /*Invoca Controller Applicativo*/
            boolean success = this.registrationController.registrationPatient(bean);


            if(success){
                errorLabel.setStyle(STYLE_SUCCESS);
                errorLabel.setText("Registrazione avvenuta con successo!");

                /*Attendi 2 secondi e poi torna al login*/
                var delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
                delay.setOnFinished(e -> NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login"));
                delay.play();
            } else{
                errorLabel.setStyle(STYLE_ERROR);
                errorLabel.setText("Errore nella registrazione! Controlla i dati inseriti");
            }

        } catch (Exception e) {
            gestisciEccezione(e);
        }
    }

    @FXML private void onBackToLoginClick(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }

    private void gestisciEccezione(Exception e){
        errorLabel.setStyle(STYLE_ERROR);
        if (e instanceof DateTimeParseException){
            errorLabel.setText("Formato data non valido. Usa GG/MM/AAAA.");
            logger.warning("Formato data non valido. Usa GG/MM/AAAA.");
        } else {
            errorLabel.setText("Errore: " + e.getMessage());
            logger.warning("Errore: " + e.getMessage());
        }

    }

}
