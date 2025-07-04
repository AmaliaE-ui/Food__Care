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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookAppointmentController {

    private final NutritionistDAO nutritionistDAO = Session.getInstance().getNutritionistDAO();
    private final AppointmentDAO appointmentDAO = Session.getInstance().getAppointmentDAO();
    private final AvailabilityDAO availabilityDAO = Session.getInstance().getAvailabilityDAO();


    /*Ricerca nutizionista per Città*/
    public List<NutritionistBean> searchNutritionistsByCity(String city) {
        List<Nutritionist> nutritionists = nutritionistDAO.getByCity(city);
        return nutritionists.stream()
                .map(n -> (NutritionistBean) Converter.userToBean(n))
                .toList();
    }

    /*Restituisce gli orari disponbili per il nutrizionista in una specifica data*/
    public List<LocalTime> getAvailableTimes(NutritionistBean nutritionistBean, LocalDate date){
        //interrogo AvailabiityDAO per estrarre gli orari disponibili
        return appointmentDAO.getAvailableTimesForNutritionist(nutritionistBean.getUsername(), date);
    }

    /*Prenota appuntamento*/
    public void bookAppointment(AppointmentBean appointmentBean){
        String patientUsername = Session.getInstance().getCurrentUser().getUsername();
        Appointment appointment = new Appointment(
                patientUsername,
                appointmentBean.getNutritionistUsername(),
                appointmentBean.getDate(),
                appointmentBean.getTime(),
                appointmentBean.getNotes()
        );
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentDAO.saveAppointment(appointment);
    }

    public void addAvailability(AvailabilityBean bean) {
        Availability availability = new Availability(bean.getDate(), bean.getStartTime(), bean.getEndTime(), bean.getNutritionistUsername());
        availabilityDAO.addAvailability(availability);
    }

    //Recupera disponibilità per nutrizionista
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

    //Cancella disponibilità
    public void deleteAvailability(AvailabilityBean bean) {
        Availability availability = new Availability(bean.getDate(), bean.getStartTime(), bean.getEndTime(), bean.getNutritionistUsername());
        availabilityDAO.deleteAvailability(availability);
    }

    //Slot fissi validi
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

    //Controlla se la data è un girono feriale
    public static boolean isWeekday(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return !(day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
    }
}
