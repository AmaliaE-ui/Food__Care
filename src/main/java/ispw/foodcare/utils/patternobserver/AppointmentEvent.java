package ispw.foodcare.utils.patternobserver;

/*Evento di dominio - nuovo appuntamento prenotato*/
public record AppointmentEvent(
        String nutritionistUsername,
        String patientUsername,
        java.time.LocalDate date,
        java.time.LocalTime time
) {}