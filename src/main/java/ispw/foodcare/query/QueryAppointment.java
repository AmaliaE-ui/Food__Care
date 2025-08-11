package ispw.foodcare.query;

public class QueryAppointment {
    private QueryAppointment() {};

    public static final String INSERT_APPOINTMENT = "INSERT INTO appointment (patient_username, nutritionist_username, date, " +
            "time, status, notes, viewed_by_nutritionist) VALUES (?,?,?,?,?,?,?)";
    public static final String SELECT_TIMES_FOR_NUTRITIONIST = "SELECT time FROM appointment WHERE" +
            " nutritionist_username = ? AND date = ? AND status = 'CONFIRMED'";
    public static final String SELECT_APPOINTMENTS_FOR_PATIENT = "SELECT * FROM appointment WHERE" +
            " patient_username = ? AND status = 'CONFIRMED'";
    public static final String SELECT_APPOINTMENTS_FOR_STATUS = "SELECT COUNT(*) FROM appointment WHERE" +
            "  nutritionist_username = ? AND" +
            " date = ? AND time = ? AND status = 'CONFIRMED'";
    public static final String SELECT_NUTRITIONIST_FOR_STATUS = "SELECT time FROM appointment WHERE" +
            " nutritionist_username = ? AND date = ? AND status = 'CONFIRMED'";
    public static final String DELETE_APPOINTMENT = "DELETE FROM appointment WHERE nutritionist_username = ? AND date = ? AND time = ?";

    public static final String DELETE_APPOINTMENT_BY_ID = "DELETE FROM appointment WHERE id_appointment = ?";

    //Prima di cancellare l'appuntamento recupera dati nutrizioista
    public static final String SELECT_NUTRITIONIST_BEFORE_DELETE = "SELECT nutritionist_username, date, time FROM appointment WHERE id_appointment = ?";

    public static final String MARK_APPOINTMENTS_VIEWED_FOR_NUTRITIONIST = "UPDATE appointment SET" +
            " viewed_by_nutritionist = 1 WHERE nutritionist_username = ?;";
    public static final String HAS_UNVIEWED_APPOINTMENTS_FOR_NUTRITIONIST = "SELECT COUNT(*) FROM appointment\n" +
            "WHERE nutritionist_username = ? AND viewed_by_nutritionist = 0";
    public static final String SELECT_APPOINTMENTS_FOR_NUTRITIONIST_WITH_PATIENT_NAME =
            "SELECT appointment.*, user.nome, user.cognome " +
                    "FROM appointment " +
                    "JOIN user ON appointment.patient_username = user.username " +
                    "WHERE appointment.nutritionist_username = ?" +
                    "ORDER BY appointment.date, appointment.time";

}
