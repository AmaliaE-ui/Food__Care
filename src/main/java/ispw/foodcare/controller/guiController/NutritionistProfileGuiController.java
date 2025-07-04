package ispw.foodcare.controller.guicontroller;

import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NutritionistProfileGuiController {

    @FXML private Label nameLabel;
    @FXML private Label surnameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label specializationLabel;
    @FXML private Label addressLabel;
    @FXML private ImageView profileImageView;

    private static NutritionistBean nutritionistBean;

    public static void setSelectedNutritionist(NutritionistBean bean) {
        nutritionistBean = bean;
    }

    @FXML
    public void setNutritionistBean(NutritionistBean bean) {
        this.nutritionistBean = bean;
        nameLabel.setText(bean.getName());
        surnameLabel.setText(bean.getSurname());
        emailLabel.setText(bean.getEmail());
        phoneLabel.setText(bean.getPhoneNumber());
        specializationLabel.setText(bean.getSpecializzazione());

        if (bean.getAddress() != null) {
            addressLabel.setText(bean.getAddress().getCitta() + ", " +
                    bean.getAddress().getVia() + " " +
                    bean.getAddress().getCivico());
        } else {
            addressLabel.setText("N/D");
        }

        profileImageView.setImage(new Image(getClass().getResourceAsStream("/ispw/foodcare/images/nutritionist_generic.png")));
    }

    @FXML
    private void handleBookAppointment(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/BookAppointment/bookAppointment.fxml",
                nameLabel,  // Nodo dell'interfaccia per accedere alla scena
                nutritionistBean
        );
    }


    @FXML
    private void handleReviews(ActionEvent event) {
        // TODO: implementa apertura finestra recension
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Non implementato");
        alert.setHeaderText(null);
        alert.setContentText("Le recensioni non sono state implementate.");
        alert.showAndWait();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        // Torna alla lista dei nutrizionisti
        NavigationManager.switchScene(event,"/ispw/foodcare/BookAppointment/homePatient.fxml", "FoodCare - Home");
    }
}
