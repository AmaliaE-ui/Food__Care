package ispw.foodcare.exception;

/** Qualsiasi errore tecnico lato persistenza appuntamenti. */
public class AppointmentPersistenceException extends AppointmentException {
    public AppointmentPersistenceException(String message, Throwable cause) { super(message, cause); }
}
