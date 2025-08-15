package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.bean.UserBean;
import ispw.foodcare.dao.AppointmentDAO;
import ispw.foodcare.model.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.logging.Logger;

public class PersonalAreaPatientGuiController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label dobLabel;
    @FXML private TextArea goalsTextArea;

    private static final Logger logger = Logger.getLogger(PersonalAreaPatientGuiController.class.getName());

    @FXML public void initialize() {
        UserBean currentUser = Session.getInstance().getCurrentUser();

        if (currentUser != null) {
            nameLabel.setText(currentUser.getName() + " " + currentUser.getSurname());
            emailLabel.setText(currentUser.getEmail());
            phoneLabel.setText(currentUser.getPhoneNumber());

            goalsTextArea.setText("Obiettivi da raggiungere:\n" +
                    "Ancora da aggiungere.");
        }
    }

    @FXML private void onEditClick() {
        logger.info("Modifica profilo: non implementato");
    }

    @FXML private void onDeleteClick() {
        logger.info("Elimina account: non implementato");
    }
}