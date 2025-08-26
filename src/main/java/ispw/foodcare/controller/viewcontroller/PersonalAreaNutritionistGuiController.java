package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.model.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.logging.Logger;

public class PersonalAreaNutritionistGuiController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label specializationLabel;
    @FXML private TextArea bioTextArea;

    private static final Logger logger = Logger.getLogger(PersonalAreaNutritionistGuiController.class.getName());

    @FXML public void initialize() {
        UserBean currentUser = Session.getInstance().getCurrentUser();

        if (currentUser instanceof NutritionistBean nutritionist) {
            nameLabel.setText(nutritionist.getName() + " " + nutritionist.getSurname());
            emailLabel.setText(nutritionist.getEmail());
            phoneLabel.setText(nutritionist.getPhoneNumber());
            specializationLabel.setText(nutritionist.getSpecializzazione());
        } else {
            nameLabel.setText("N/A");
            emailLabel.setText("N/A");
            phoneLabel.setText("N/A");
            specializationLabel.setText("N/A");
            bioTextArea.setText("Dati non disponibili.");
        }
    }

    @FXML private void onEditClick() {
        logger.info("Modifica profilo nutrizionista: non implementato");
    }

    @FXML private void onDeleteClick() {
        logger.info("Elimina account nutrizionista: non implementato");
    }
}
