package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.bean.AppointmentBean;
import ispw.foodcare.controller.applicationcontroller.BookAppointmentController;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.ShowAlert;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentsPatientGuiController {

    @FXML private TableView<AppointmentBean> appointmentsTableView;
    @FXML private TableColumn<AppointmentBean, String> dateColumn;
    @FXML private TableColumn<AppointmentBean, String> timeColumn;
    @FXML private TableColumn<AppointmentBean, String> nutritionistColumn;
    @FXML private TableColumn<AppointmentBean, String> statusColumn;
    @FXML private TableColumn<AppointmentBean, String> notesColumn;
    @FXML private Button deleteButton;

    private final BookAppointmentController controller = new BookAppointmentController();
    ShowAlert alert = new ShowAlert();

    @FXML private void initialize() {
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        timeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTime().toString()));

        nutritionistColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNutritionistUsername()));

        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().name()));/*.name() - Converto Enum in stringa*/

        notesColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNotes()));

        appointmentsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            deleteButton.setDisable(newSelection == null);
        });


        loadAppointments();
    }

    private void loadAppointments() {
        String patientUsername = Session.getInstance().getCurrentUser().getUsername();
        List<AppointmentBean> appointments = controller.getPatientAppointments(patientUsername);
        appointmentsTableView.setItems(FXCollections.observableArrayList(appointments));
    }

    @FXML private void onDeleteAppointmentClick() {
        AppointmentBean selected = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert.showAlert("Attenzione", "Seleziona un appuntamento da eliminare.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Conferma eliminazione");
        confirm.setHeaderText(null);
        confirm.setContentText("Vuoi davvero eliminare l'appuntamento del " +
                selected.getDate() + " alle " + selected.getTime() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                controller.deleteAppointment(selected);

                appointmentsTableView.getItems().remove(selected);

                alert.showAlert("Eliminato", "Appuntamento eliminato con successo.");
            }
        });
    }
}
