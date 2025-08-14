package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.utils.NavigationManager;
import ispw.foodcare.utils.ShowAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;

public class NutritionistProfileGuiController {

    @FXML private Label nameLabel;
    @FXML private Label surnameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label specializationLabel;
    @FXML private Label addressLabel;
    @FXML private ImageView profileImageView;
    @FXML private WebView mapWebView;

    private ShowAlert alert = new ShowAlert();

    private static NutritionistBean nutritionistBean;

    @FXML public void setNutritionistBean(NutritionistBean bean) {
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

            String fullAddress = nutritionistBean.getAddress().getVia() + " "
                    + nutritionistBean.getAddress().getCivico() + ", "
                    + nutritionistBean.getAddress().getCitta();

            String encodedAddress = fullAddress.replace(" ", "+");
            String iframe = "<iframe width='100%' height='300' frameborder='0' style='border:0' "
                    + "src='https://www.google.com/maps/embed/v1/place?key=AIzaSyDqb_J_8LDobiNeRynHIdhdWZ6JWVMabVA&q=" + encodedAddress + "' "
                    + "allowfullscreen></iframe>";

            String html = "<html><body style='margin:0;padding:0;'>" + iframe + "</body></html>";
            mapWebView.getEngine().loadContent(html, "text/html");
        } else {
            addressLabel.setText("N/D");
            mapWebView.getEngine().loadContent("<html><body><h3>Indirizzo non disponibile</h3></body></html>");
        }

        profileImageView.setImage(new Image(getClass().getResourceAsStream("/ispw/foodcare/images/nutritionist_generic.png")));
    }

    @FXML private void onBookAppointmentClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/BookAppointment/bookAppointment.fxml",
                nameLabel,  /*Nodo dell'interfaccia per accedere alla scena*/
                nutritionistBean
        );
    }


    @FXML private void onReviewsClick(ActionEvent event) {
        alert.showAlert("Non implementato","Le recensioni non sono state implementate.");
    }

    @FXML private void onBackClick(ActionEvent event) {
        /*Torna alla lista dei nutrizionisti*/
        NavigationManager.switchScene(event,"/ispw/foodcare/BookAppointment/homePatient.fxml", "FoodCare - Home");
    }
}
