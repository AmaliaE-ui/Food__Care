package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.ShowAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class PersonalAreaPatientGuiController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label birthDayLabel;
    @FXML private TextArea goalsTextArea;

    private static final Logger logger = Logger.getLogger(PersonalAreaPatientGuiController.class.getName());
    private static final DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    ShowAlert showAlert = new ShowAlert();

    @FXML public void initialize() {
        UserBean currentUser = Session.getInstance().getCurrentUser();

        if (currentUser == null) {
            logger.warning("Utente non presente in sessione.");
            return;
        }
        nameLabel.setText(currentUser.getName() + " " + currentUser.getSurname());
        emailLabel.setText(currentUser.getEmail());
        phoneLabel.setText(currentUser.getPhoneNumber());

        /*Se è un paziente, mostra la data di nascita*/
        if (currentUser instanceof PatientBean patientBean) {
            birthDayLabel.setText(patientBean.getBirthDate().format(date));
        }else {
            birthDayLabel.setText("N/D");
        }
        goalsTextArea.setText("Obiettivi da raggiungere:\n" +
                    "Ancora da aggiungere.");
    }


    @FXML private void onEditClick() {
        showAlert.showAlert( "Errore","Modifica profilo: non implementato");
    }

    @FXML private void onDeleteClick() {
        showAlert.showAlert( "Errore","Elimina account: non implementato");
    }
}