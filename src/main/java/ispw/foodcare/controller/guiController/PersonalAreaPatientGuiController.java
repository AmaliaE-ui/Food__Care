package ispw.foodcare.controller.guicontroller;

import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class PersonalAreaPatientGuiController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private TextArea goalsTextArea;

    @FXML
    public void initialize() {
        UserBean currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            nameLabel.setText(currentUser.getName() + " " + currentUser.getSurname());
            emailLabel.setText(currentUser.getEmail());
            phoneLabel.setText(currentUser.getPhoneNumber());
            //dobLabel.setText(currentUser.getBirthDate());

            goalsTextArea.setText("Obiettivi da raggiungere:\n" +
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        }
    }

    @FXML
    private void handleEdit() {
        System.out.println("Modifica profilo: implementa funzione");
    }

    @FXML
    private void handleDelete() {
        System.out.println("Elimina account: implementa funzione");
    }
}