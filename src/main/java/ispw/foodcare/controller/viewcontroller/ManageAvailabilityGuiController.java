package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.bean.AvailabilityBean;
import ispw.foodcare.utils.ShowAlert;
import ispw.foodcare.controller.applicationcontroller.BookAppointmentController;
import ispw.foodcare.model.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ManageAvailabilityGuiController {

    @FXML private DatePicker datePicker;
    @FXML private ListView<LocalTime> slotsListView;
    @FXML private TableView<AvailabilityBean> availabilityTableView;
    @FXML private TableColumn<AvailabilityBean, LocalDate> dateColumn;
    @FXML private TableColumn<AvailabilityBean, LocalTime> startTimeColumn;
    @FXML private TableColumn<AvailabilityBean, LocalTime> endTimeColumn;
    @FXML private TableColumn<AvailabilityBean, Void> actionColumn;

    private final BookAppointmentController controller = new BookAppointmentController();
    private final ObservableList<AvailabilityBean> availabilityList = FXCollections.observableArrayList();

    ShowAlert alert = new ShowAlert();

    @FXML private void initialize() {

        controller.deleteExpiredAvailabilities();
        slotsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        availabilityTableView.setItems(availabilityList);

        /*Aggiunta colonna 'Elimina'*/
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Elimina");

            {
                deleteButton.setOnAction(event -> {
                    AvailabilityBean availability = getTableView().getItems().get(getIndex());
                    controller.deleteAvailability(availability);
                    availabilityList.remove(availability);
                    alert.showAlert("Eliminato", "Disponibilità rimossa con successo!");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });


        /*Carica disponibilità già presenti*/
        String username = Session.getInstance().getCurrentUser().getUsername();
        List<AvailabilityBean> loadedAvailabilities =
               new java.util.ArrayList<>(controller.getAvailabilitiesForNutritionist(username));
        loadedAvailabilities.removeIf(bean -> bean.getDate().isBefore(LocalDate.now()));
        availabilityList.addAll(loadedAvailabilities);

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                if (!controller.isWeekday(newDate)) {
                    alert.showAlert("Data non valida", "Puoi inserire disponibilità solo nei giorni lavorativi (lun-ven).");
                    datePicker.setValue(null);
                } else {
                    slotsListView.setItems(FXCollections.observableArrayList(controller.generateFixedSlots()));
                }
            }
        });
    }

    @FXML private void onAddAvailabilityClick() {
        LocalDate date = datePicker.getValue();
        List<LocalTime> selectedSlots = slotsListView.getSelectionModel().getSelectedItems();

        if (date == null || selectedSlots.isEmpty()) {
            alert.showAlert("Errore", "Seleziona una data valida e almeno uno slot orario.");
            return;
        }

        String username = Session.getInstance().getCurrentUser().getUsername();

        for (LocalTime slot : selectedSlots) {
            //Evita duplicati prima di una nuovo inserimento
            boolean alreadyExists = availabilityList.stream().anyMatch(a ->
                    a.getDate().equals(date) && a.getStartTime().equals(slot));

            if (alreadyExists) {
                alert.showAlert("Attenzione", "Esiste già una disponibilità per questa data e orario.");
                continue; // passa allo slot successivo senza inserire duplicati
            }

            try {
                AvailabilityBean bean = new AvailabilityBean();
                bean.setDate(date); // può lanciare eccezione se la data è nel passato
                bean.setStartTime(slot);
                bean.setEndTime(slot.plusMinutes(45));
                bean.setNutritionistUsername(username);

                controller.addAvailability(bean); // può sollevare eccezioni dal DAO
                availabilityList.add(bean);

                availabilityList.sort((a1, a2) -> {
                    int cmp = a1.getDate().compareTo(a2.getDate());
                    return cmp != 0 ? cmp : a1.getStartTime().compareTo(a2.getStartTime());
                });
                alert.showAlert("Successo", "Disponibilità aggiunte con successo!");

            } catch (IllegalArgumentException e) {
                alert.showAlert("Errore di validazione", e.getMessage());
            } catch (Exception e) {
                alert.showAlert("Errore", "Impossibile aggiungere la disponibilità: " + e.getMessage());
            }
        }

        //Pulizia form dopo un inserimento
        datePicker.setValue(null);
        slotsListView.getItems().clear();
        slotsListView.getSelectionModel().clearSelection();

    }
}
