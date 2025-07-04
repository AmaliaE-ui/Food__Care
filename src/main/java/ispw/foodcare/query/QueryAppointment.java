package ispw.foodcare.query;

public class QueryAppointment {
    public static final String INSERT_APPOINTMENT = "INSERT INTO appointment (id_appointment, patient_username, nutritionist_username, date, time, status, notes) VALUES (?,?,?,?,?,?,?,?)";
    public static final String SELECT_TIMES_FOR_NUTRITIONIST = "SELECT time FROM appointment WHERE nutritionist_username = ? AND date = ? AND status = 'CONFIRMED'";
}
