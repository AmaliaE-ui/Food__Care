package ispw.foodcare.controller.viewcontroller;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import ispw.foodcare.bean.AppointmentBean;
import ispw.foodcare.bean.AvailabilityBean;
import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.controller.applicationcontroller.BookAppointmentController;
import ispw.foodcare.utils.NavigationManager;
import ispw.foodcare.utils.ShowAlert;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.scene.layout.StackPane;
import com.calendarfx.view.CalendarView;
import com.calendarfx.model.Calendar;

public class BookAppointmentGuiController {

    @FXML private StackPane calendarPane;
    @FXML private Label selectionSummaryLabel;
    @FXML private TextArea notesTextArea;

    private LocalTime selectedTime;
    private LocalDate selectedDate;
    private NutritionistBean selectedNutritionist;
    private CalendarView calendarView;
    private final BookAppointmentController appointmentController = new BookAppointmentController();
    private final ShowAlert alert = new ShowAlert();

    public void setNutritionist(NutritionistBean nutritionist) {
        this.selectedNutritionist = nutritionist;
        populateCalendarWithAvailability();
    }

    @FXML private void initialize() {
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

        // Listener selezione data e ora cliccando sullo slot
        calendarView.setEntryDetailsPopOverContentCallback(param -> {
            Entry<?> entry = param.getEntry();
            if (entry != null) {
                selectedDate = entry.getStartDate();
                selectedTime = entry.getStartTime();
                selectionSummaryLabel.setText("Stai prenotando un appuntamento per il giorno " +
                        selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " alle ore " +
                        selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            return null; // Usa popover di default
        });

        populateCalendarWithAvailability();
    }

    private void populateCalendarWithAvailability() {
        if (selectedNutritionist == null) return;

        List<AvailabilityBean> availabilities =
                appointmentController.getAvailabilitiesForNutritionist(selectedNutritionist.getUsername());

        Calendar<String> calendar = new Calendar<>("Disponibilità");
        calendar.setStyle(Calendar.Style.STYLE1); // verde

        for (AvailabilityBean availability : availabilities) {
            LocalDate date = availability.getDate();
            LocalTime startTime = availability.getStartTime();
            LocalTime endTime = availability.getEndTime();

            // Recupero orari già prenotati per quella data
            List<LocalTime> bookedTimes =
                    appointmentController.getBookedTimes(selectedNutritionist.getUsername(), date);

            // Se lo slot è già prenotato, skip
            if (bookedTimes.contains(startTime)) {
                continue;
            }

            Entry<String> entry = new Entry<>("Disponibile");
            entry.setInterval(date.atTime(startTime), date.atTime(endTime));
            entry.setLocation(startTime + " - " + endTime);

            calendar.addEntry(entry);
        }

        calendarView.getCalendarSources().clear();
        CalendarSource source = new CalendarSource("Disponibilità Nutr.");
        source.getCalendars().add(calendar);
        calendarView.getCalendarSources().add(source);
    }


    @FXML private void onBookAppointmentClick() {
        if (selectedDate == null || selectedTime == null) {
            alert.showAlert("Errore", "Devi selezionare una data e un orario disponibili dal calendario.");
            return;
        }

        AppointmentBean appointmentBean = new AppointmentBean();
        appointmentBean.setDate(selectedDate);
        appointmentBean.setTime(selectedTime);
        appointmentBean.setNotes(notesTextArea.getText());
        appointmentBean.setNutritionistUsername(selectedNutritionist.getUsername());

        try{
            appointmentController.bookAppointment(appointmentBean, appointmentBean.getPatientUsername());
            alert.showAlert("Successo", "Appuntamento prenotato correttamente!");
            clearForm();

            //Aggiorna la Gui ricaricano il calendario senza lo slot prenotato
            populateCalendarWithAvailability();
        }catch (RuntimeException e){
            alert.showAlert("Errore", e.getMessage());
        }

    }

    private void clearForm() {
        selectedDate = null;
        selectedTime = null;
        notesTextArea.clear();
        selectionSummaryLabel.setText("Seleziona una data e un orario sul calendario per procedere.");
    }

    @FXML private void onBackClick() {
        NavigationManager.switchPane(
                "/ispw/foodcare/BookAppointment/nutritionistProfile.fxml",
                calendarPane, // nodo generico
                selectedNutritionist
        );
    }
}
