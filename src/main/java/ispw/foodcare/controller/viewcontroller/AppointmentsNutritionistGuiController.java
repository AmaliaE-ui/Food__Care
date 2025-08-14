package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.bean.AppointmentBean;
import ispw.foodcare.controller.applicationcontroller.AppointmentController;
import ispw.foodcare.model.Session;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentsNutritionistGuiController {

    @FXML private TableView<AppointmentBean> appointmentsTableView;
    @FXML private TableColumn<AppointmentBean, String> nameColumn;
    @FXML private TableColumn<AppointmentBean, String> surnameColumn;
    @FXML private TableColumn<AppointmentBean, String> dateColumn;
    @FXML private TableColumn<AppointmentBean, String> timeColumn;
    @FXML private TableColumn<AppointmentBean, String> notesColumn;

    private final AppointmentController controller = new AppointmentController();

    @FXML
    private void initialize() {

        // Mostra nome
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPatientName()));

        // Mostra cognome
        surnameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPatientSurname()));

        // Mostra data
        dateColumn.setCellValueFactory(cellData ->
                Bindings.createStringBinding(() ->
                        cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        // Mostra ora
        timeColumn.setCellValueFactory(cellData ->
               //bindings per data e ora formattate
                Bindings.createStringBinding(() ->
                        cellData.getValue().getTime().format(DateTimeFormatter.ofPattern("HH:mm"))));

        // Mostra note
        notesColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNotes()));

        loadAppointments();
    }

    private void loadAppointments() {
        String nutritionistUsername = Session.getInstance().getCurrentUser().getUsername();
        List<AppointmentBean> appointments = controller.getAppointmentsForNutritionist(nutritionistUsername);
        appointmentsTableView.setItems(FXCollections.observableArrayList(appointments));

        // Reset badge delle notifiche
        controller.markAppointmentsAsViewedForNutritionist(nutritionistUsername);
    }
}
