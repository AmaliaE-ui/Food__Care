package ispw.foodcare.utils.patternobserver;

public interface AppointmentSubject {

    /*Iscrizione per un certo nutrizionista; ritorna una "subscription" da chiudere*/
    AutoCloseable subscribe(String nutritionistUsername, AppointmentListener listener);
    void unsubscribe(String nutritionistUsername, AppointmentListener listener);
    void notifyNewAppointment(AppointmentEvent event);
}
