package ispw.foodcare.controller.guicontroller;

import ispw.foodcare.bean.AddressBean;
import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.dao.AddressDAO;
import ispw.foodcare.model.NutritionistModel;
import ispw.foodcare.utils.FieldValidator;
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
        if (!validateFields()) return;

        try {

            //Costruzione del bean Address
            AddressBean address = new AddressBean();
            address.setVia(viaTextField.getText());
            address.setCivico(civicoTextField.getText());
            address.setCap(capTextField.getText());
            address.setCitta(cittaTextField.getText());
            address.setProvincia(provinciaTextField.getText());
            address.setRegione(regioneTextField.getText());

            int addressId = new AddressDAO().saveAddress(address);

            //Costruzione del bean Nutritionist
            NutritionistBean bean = new NutritionistBean();
            bean.setName(nameTextField.getText());
            bean.setSurname(surnameTextField.getText());
            bean.setPhoneNumber(phoneTextField.getText());
            bean.setUsername(usernameTextField.getText());
            bean.setEmail(emailTextField.getText());
            bean.setPassword(passwordTextField.getText());
            bean.setRole("NUTRITIONIST");
            bean.setPiva(pivaTextField.getText());
            bean.setTitoloStudio(titoloStudioChoiceBox.getValue());
            bean.setSpecializzazione(specializzazioneChoiceBox.getValue());

            new NutritionistModel().registerNutritionist(bean, addressId);

            errorLabel.setText("-fx-text-fill: green");
            errorLabel.setText("Registrazione avvenuta con successo!");

            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
            delay.setOnFinished(e -> NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login"));
        } catch (SQLException e) {
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

    private boolean validateFields() {
        if (FieldValidator.isEmpty(nameTextField.getText())) {
            errorLabel.setText("Il nome è obbligatorio."); return false;
        }
        if (FieldValidator.isEmpty(surnameTextField.getText())) {
            errorLabel.setText("Il cognome è obbligatorio."); return false;
        }
        if (FieldValidator.isEmpty(usernameTextField.getText())) {
            errorLabel.setText("Lo username è obbligatorio."); return false;
        }
        if (FieldValidator.isEmpty(emailTextField.getText())) {
            errorLabel.setText("L'email è obbligatoria."); return false;
        }
        if (!FieldValidator.isEmailValid(emailTextField.getText())) {
            errorLabel.setText("Email non valida."); return false;
        }
        if (FieldValidator.isEmpty(phoneTextField.getText())) {
            errorLabel.setText("Il numero di telefono è obbligatorio."); return false;
        }
        if (!FieldValidator.isPhoneValid(phoneTextField.getText())) {
            errorLabel.setText("Numero di telefono non valido."); return false;
        }
        if (FieldValidator.isEmpty(passwordTextField.getText()) || FieldValidator.isEmpty(confirmPasswordTextField.getText())) {
            errorLabel.setText("Inserisci e conferma la password."); return false;
        }
        if (!FieldValidator.doPasswordsMatch(passwordTextField.getText(), confirmPasswordTextField.getText())) {
            errorLabel.setText("Le password non corrispondono."); return false;
        }
        if (FieldValidator.isEmpty(pivaTextField.getText())) {
            errorLabel.setText("La partita IVA è obbligatoria."); return false;
        }
        if (titoloStudioChoiceBox.getValue() == null) {
            errorLabel.setText("Il titolo di studio è obbligatorio."); return false;
        }
        if (FieldValidator.isEmpty(viaTextField.getText()) ||
                FieldValidator.isEmpty(civicoTextField.getText()) ||
                FieldValidator.isEmpty(capTextField.getText()) ||
                FieldValidator.isEmpty(cittaTextField.getText()) ||
                FieldValidator.isEmpty(provinciaTextField.getText()) ||
                FieldValidator.isEmpty(regioneTextField.getText())) {
            errorLabel.setText("Tutti i campi dell'indirizzo sono obbligatori."); return false;
        }
        if (specializzazioneChoiceBox.getValue() == null) {
            errorLabel.setText("La specializzazione è obbligatoria."); return false;
        }
        return true;
    }
}
