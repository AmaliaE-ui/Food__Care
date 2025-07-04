package ispw.foodcare.controller.guicontroller;

import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.model.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class PersonalAreaNutritionistGuiController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label specializationLabel;
    @FXML
    private TextArea bioTextArea;

    @FXML
    public void initialize() {
        UserBean currentUser = Session.getInstance().getCurrentUser();

        if (currentUser instanceof NutritionistBean nutritionist) {
            nameLabel.setText(nutritionist.getName() + " " + nutritionist.getSurname());
            emailLabel.setText(nutritionist.getEmail());
            phoneLabel.setText(nutritionist.getPhoneNumber());
            specializationLabel.setText(nutritionist.getSpecializzazione());

            /*bioTextArea.setText("Biografia / Presentazione:\n"
                    + "Specializzato in " + nutritionist.getSpecializzazione() + ".\n"
                    + "Qui puoi inserire una breve descrizione del tuo approccio nutrizionale.");*/
        } else {
            nameLabel.setText("N/A");
            emailLabel.setText("N/A");
            phoneLabel.setText("N/A");
            specializationLabel.setText("N/A");
            bioTextArea.setText("Dati non disponibili.");
        }
    }

    @FXML
    private void handleEdit() {
        System.out.println("Modifica profilo nutrizionista: implementa funzione");
    }

    @FXML
    private void handleDelete() {
        System.out.println("Elimina account nutrizionista: implementa funzione");
    }
}
