package ispw.foodcare.controller.guicontroller;

import ispw.foodcare.bean.AppointmentBean;
import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.bean.applicationcontroller.BookAppointmentController;
import ispw.foodcare.utils.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javafx.scene.layout.StackPane;
import com.calendarfx.view.CalendarView;

public class BookAppointmentGuiController {

    @FXML private StackPane calendarPane;
    private CalendarView calendarView;
    @FXML private ComboBox<LocalTime> timeComboBox;
    @FXML private TextArea notesTextArea;

    private NutritionistBean selectedNutritionist;

    public void setNutritionist(NutritionistBean nutritionist) {
        this.selectedNutritionist = nutritionist;
    }

    @FXML
    private void initialize() {
        calendarView = new CalendarView();
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);
        calendarView.setShowSearchField(false);
        calendarView.setShowToolBar(false);
        calendarView.setShowPageSwitcher(false);
        calendarView.setShowSourceTray(false);
        calendarView.showMonthPage();

        calendarView.setRequestedTime(LocalTime.now());
        calendarPane.getChildren().add(calendarView);

        // Aggiungere un listener per selezionare la data
        calendarView.dateProperty().addListener((obs, oldDate, newDate) -> {
            // Sincronizzare la selezione con il tuo ComboBox degli orari
            System.out.println("Data selezionata: " + newDate);
        });
    }


    private void loadAvailableTimes(LocalDate date) {
        if (date == null || selectedNutritionist == null) {
            timeComboBox.getItems().clear();
            return;
        }
        BookAppointmentController controller = new BookAppointmentController();
        List<LocalTime> availableTimes = controller.getAvailableTimes(selectedNutritionist, date);

        timeComboBox.getItems().setAll(availableTimes);
    }

    @FXML
    private void handleBookAppointment() {
        LocalDate date = calendarView.getDate();
        LocalTime time = timeComboBox.getValue();
        String notes = notesTextArea.getText();

        if (date == null || time == null) {
            showAlert("Errore", "Devi selezionare una data e un orario disponibili.");
            return;
        }

        AppointmentBean appointmentBean = new AppointmentBean();
        appointmentBean.setDate(date);
        appointmentBean.setTime(time);
        appointmentBean.setNotes(notes);
        appointmentBean.setNutritionistUsername(selectedNutritionist.getUsername());

        BookAppointmentController controller = new BookAppointmentController();
        controller.bookAppointment(appointmentBean);

        showAlert("Successo", "Appuntamento prenotato correttamente!");
        clearForm();
    }

    private void clearForm() {
        timeComboBox.getItems().clear();
        notesTextArea.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBack(){
        NavigationManager.switchPane(
                "/ispw/foodcare/BookAppointment/nutritionistProfile.fxml",
                timeComboBox, //posso usare qualsiasi nodo del controller per recuperare la scena
                selectedNutritionist
        );
    }

}
