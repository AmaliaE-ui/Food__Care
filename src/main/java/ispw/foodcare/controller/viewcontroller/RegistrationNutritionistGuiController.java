package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.Role;
import ispw.foodcare.bean.AddressBean;
import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.controller.applicationcontroller.RegistrationController;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Map;

public class RegistrationNutritionistGuiController {

    private final RegistrationController registrationController;

    /*Costruttore vuoto, utilr per FXML*/
    public RegistrationNutritionistGuiController() {
        var s = Session.getInstance();
        this.registrationController = new RegistrationController(s.getUserDAO());
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
    @FXML private ComboBox<String> specializzazioneComboBox;
    @FXML private Label errorLabel;

    @FXML private void onBackToLoginClick(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }

    public void onSaveButtonClick(ActionEvent event) {
        try {
            /*Costruzione del bean Address*/
            AddressBean address = new AddressBean();
            address.setVia(viaTextField.getText());
            address.setCivico(civicoTextField.getText());
            address.setCap(capTextField.getText());
            address.setCitta(cittaTextField.getText());
            address.setProvincia(provinciaTextField.getText());
            address.setRegione(regioneTextField.getText());

            /*Costruzione del bean Nutritionist*/
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
            bean.setSpecializzazione(specializzazioneComboBox.getValue());

            bean.setAddress(address);

            /*Validazione*/
            String error = ispw.foodcare.validation.NutritionistValidator.validateNutritionist(bean, address, confirmPasswordTextField.getText());
            if (error != null) {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText(error);
                return;
            }

            /*Invoca Controller Applicativo*/
            boolean success = this.registrationController.registrationNutritionist(bean);

            if(success){
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("Registrazione avvenuta con successo!");

                /* Attendi 2 secondi e poi torna al login*/
                var delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
                delay.setOnFinished(e -> NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login"));
                delay.play();
            } else{
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText("Errore nella registrazione! Controlla i dati inseriti");
            }

        } catch (IllegalArgumentException e) {
            /*Eccezione dai Bean*/
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText(e.getMessage());
        }

        // Se il controller applicativo propagasse eccezioni applicative “note”, gestiscile qui:
        // } catch (AccountAlreadyExistsException e) {
        //     errorLabel.setStyle("-fx-text-fill: red;");
        //     errorLabel.setText("Account già esistente: " + e.getMessage());

        catch (Exception e) {
            /*Eccezione*/
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Errore inatteso: " + e.getMessage());
        }
    }


    private final Map<String, String> descrizioneSpecializzazioni = Map.of(
            "Nutrizione per disturbi alimentari", "Supporto alimentare per chi soffre di anoressia/bulimia/obesità",
            "Nutrizione Vegetariana/Vegana", "Pianificazione di una dieta alimentare priva di alimenti di origine animale ",
            "Nutrizione sportiva", "Pianificazione di una dieta per prestazioni atletiche",
            "Nutrizione pedriatica", "Gestione dell'alimentazione nei bambini e adolescenti per una crescita sana.",
            "Nutritiozione in grvidanza", "Supporto nutrizionale per gestanti",
            "Nutrizione preventiva", "Piani alimentari per prevenire malattie croniche e promuovere il benessere.",
            "Nutrizione clinica", "Gestione dietetica in presenza di patologie croniche o acute."
    );


    @FXML public void initialize() {
        titoloStudioChoiceBox.getItems().addAll(
                "Laurea in Dietistica",
                "Laurea in Biologia",
                "Scienze della Nutrizione",
                "Master in Nutrizione Clinica"
        );

        specializzazioneComboBox.getItems().addAll(descrizioneSpecializzazioni.keySet());

        specializzazioneComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                    setTooltip(null);
                }else{
                    setText(item);
                    Tooltip tooltip = new Tooltip(descrizioneSpecializzazioni.get(item));
                    setTooltip(tooltip);
                }
            }
        });
    }
}
