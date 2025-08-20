package ispw.foodcare.controller.applicationcontroller;

import ispw.foodcare.AppointmentStatus;
import ispw.foodcare.bean.AppointmentBean;
import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.dao.AppointmentDAO;
import ispw.foodcare.dao.AvailabilityDAO;
import ispw.foodcare.dao.NutritionistDAO;
import ispw.foodcare.model.Appointment;
import ispw.foodcare.model.Nutritionist;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.Converter;
import ispw.foodcare.bean.AvailabilityBean;
import ispw.foodcare.model.Availability;
import ispw.foodcare.utils.patternobserver.AppointmentEvent;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookAppointmentController {

    private final LoginController loginController = new LoginController();
    private final NutritionistDAO nutritionistDAO;
    private final AppointmentDAO appointmentDAO;
    private final AvailabilityDAO availabilityDAO;

    /*Costruttore*/
    public BookAppointmentController(NutritionistDAO nutritionistDAO,
                                     AppointmentDAO appointmentDAO,
                                     AvailabilityDAO availabilityDAO) {
        this.nutritionistDAO = nutritionistDAO;
        this.appointmentDAO = appointmentDAO;
        this.availabilityDAO = availabilityDAO;
    }

    public BookAppointmentController() {
        var s = Session.getInstance();
        this.nutritionistDAO = s.getNutritionistDAO();
        this.appointmentDAO = s.getAppointmentDAO();
        this.availabilityDAO = s.getAvailabilityDAO();
    }

    /*Ricerca nutizionista per Città*/
    public List<NutritionistBean> searchNutritionistsByCity(String city) {
        List<Nutritionist> nutritionists = nutritionistDAO.getByCity(city);
        return nutritionists.stream()
                .map(n -> (NutritionistBean) Converter.userToBean(n))
                .toList();
    }

    /*Prenota appuntamento*/
    public void bookAppointment(AppointmentBean appointmentBean){
        /*String patientUsername = Session.getInstance().getCurrentUser().getUsername(); SE LO TENESSI IL CONTROLLER NON SAREBBE STATELESS
        *Quindi passo lo username dalla GUI/CLI*/

        if(loginController.valLogin(Session.getInstance().getCurrentUser().getUsername(),Session.getInstance().getCurrentUser().getPassword())){
            Appointment appointment = new Appointment(
                appointmentBean.getPatientUsername(),
                appointmentBean.getNutritionistUsername(),
                appointmentBean.getDate(),
                appointmentBean.getTime(),
                appointmentBean.getNotes()
            );
            /*Imposto lo stato prima del salvataggio*/
            appointment.setStatus(AppointmentStatus.CONFIRMED);

            /*Controllo se slot è già prenotato*/
            if (appointmentDAO.isSlotAlreadyBooked(
                appointmentBean.getNutritionistUsername(),
                appointmentBean.getDate(),
                appointmentBean.getTime())) {
                throw new IllegalStateException("L'orario selezionato è già stato prenotato.");
             }

            /*Salvataggio*/
            appointmentDAO.saveAppointment(appointment);

            /*Push verso le view interessate del nuovo appuntamento prenotato*/
            Session.getInstance().getAppointmentSubject().notifyNewAppointment(
                new AppointmentEvent(
                        appointment.getNutritionistUsername(),
                        appointment.getPatientUsername(),
                        appointment.getDate(),
                        appointment.getTime()
                )
            );
        }
    }

    /*Aggiungi una disponibilità*/
    public void addAvailability(AvailabilityBean bean) {


        if(loginController.valLogin(Session.getInstance().getCurrentUser().getUsername(),Session.getInstance().getCurrentUser().getPassword())){
            Availability availability = new Availability();
            availability.setDate(bean.getDate());
            availability.setStartTime(bean.getStartTime());
            availability.setEndTime(bean.getEndTime());
            availability.setNutritionistUsername(bean.getNutritionistUsername());

            availabilityDAO.addAvailability(availability);
        }
    }

    /*Recupera disponibilità per nutrizionista*/
    public List<AvailabilityBean> getAvailabilitiesForNutritionist(String nutritionistUsername) {
        List<Availability> availabilities = availabilityDAO.getAvailabilitiesForNutritionist(nutritionistUsername);
        return availabilities.stream().map(a -> {
            AvailabilityBean bean = new AvailabilityBean();
            bean.setDate(a.getDate());
            bean.setStartTime(a.getStartTime());
            bean.setEndTime(a.getEndTime());
            bean.setNutritionistUsername(a.getNutritionistUsername());
            return bean;
        }).toList();
    }

    /*Cancella disponibilità*/
    public void deleteAvailability(AvailabilityBean bean) {
        Availability availability = new Availability();
        availability.setDate(bean.getDate());
        availability.setStartTime(bean.getStartTime());
        availability.setEndTime(bean.getEndTime());
        availability.setNutritionistUsername(bean.getNutritionistUsername());

        availabilityDAO.deleteAvailability(availability);
    }

    /*Slot fissi validi*/
    public static List<LocalTime> generateFixedSlots() {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime[] morningSlots = { LocalTime.of(9,0), LocalTime.of(9,45), LocalTime.of(10,30),
                LocalTime.of(11,15), LocalTime.of(12,0) };
        LocalTime[] afternoonSlots = { LocalTime.of(15,0), LocalTime.of(15,45), LocalTime.of(16,30),
                LocalTime.of(17,15), LocalTime.of(18,0) };
        slots.addAll(Arrays.asList(morningSlots));
        slots.addAll(Arrays.asList(afternoonSlots));
        return slots;
    }

    /*Controlla se la data è un girono feriale*/
    public static boolean isWeekday(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return !(day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
    }


    /*Restituisce lista appuntamenti del paziente*/
    public List<AppointmentBean> getPatientAppointments(String patientUsername) {
        List<Appointment> appointments = appointmentDAO.getAppointmentsForPatient(patientUsername);
        return appointments.stream()
                .map(Converter::appointmentToBean)
                .toList();
    }

    /*Restituise orari già prenotati*/
    public List<LocalTime> getBookedTimes(String nutritionistUsername, LocalDate date) {
        return appointmentDAO.getBookedTimesForNutritionist(nutritionistUsername, date);
    }

    /*Cancella appuntamento e ripristino disponibilità se la data è in futuro*/
    public void deleteAppointment(AppointmentBean appointmentBean) {
        appointmentDAO.deleteAppointment(
                appointmentBean.getNutritionistUsername(),
                appointmentBean.getDate(),
                appointmentBean.getTime()
        );
        /*Se servirà lo strico - aggiornare lo stato dell'appuntamento a CANCELLED invece di eliminarla */
        if(appointmentBean.getDate().isAfter(LocalDate.now())) {
            addAvailability(appointmentToAvailabilityBean(appointmentBean));
        }
    }

    /*Mapping appuntamento -> disponibilità*/
    public AvailabilityBean appointmentToAvailabilityBean(AppointmentBean appointmentBean) {
        AvailabilityBean availability = new AvailabilityBean();
        availability.setDate(appointmentBean.getDate());
        availability.setStartTime(appointmentBean.getTime());
        availability.setEndTime(appointmentBean.getTime().plusMinutes(45));
        availability.setNutritionistUsername(appointmentBean.getNutritionistUsername());
        return availability;
    }

    /*Pulizia disponibilità scadute*/
    public void deleteExpiredAvailabilities() {
        availabilityDAO.deleteAvailabilitybydata(LocalDate.now());
    }
}
