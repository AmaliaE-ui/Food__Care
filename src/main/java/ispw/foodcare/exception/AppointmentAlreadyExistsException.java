package ispw.foodcare.exception;

/** Slot già prenotato / vincolo di unicità violato. */
public class AppointmentAlreadyExistsException extends AppointmentException {
    public AppointmentAlreadyExistsException(String message, Throwable cause) { super(message, cause); }
}
