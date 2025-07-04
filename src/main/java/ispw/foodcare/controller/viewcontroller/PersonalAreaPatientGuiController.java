package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.bean.UserBean;
import ispw.foodcare.model.Session;
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
        UserBean currentUser = Session.getInstance().getCurrentUser();

        if (currentUser != null) {
            nameLabel.setText(currentUser.getName() + " " + currentUser.getSurname());
            emailLabel.setText(currentUser.getEmail());
            phoneLabel.setText(currentUser.getPhoneNumber());

            goalsTextArea.setText("Obiettivi da raggiungere:\n" +
                    "Ancora da aggiungere.");
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