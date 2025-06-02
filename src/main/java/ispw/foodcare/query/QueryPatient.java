package ispw.foodcare.query;

public class QueryPatient {

    private QueryPatient() {}

    public static final String INSERT_PATIENT = "INSERT INTO patient (username, data_nascita, genere) VALUES (?, ?, ?)";

    public static final String DELETE_PATIENT = "DELETE FROM patient WHERE username = ?";

    public static final String SELECT_PATIENT = "SELECT data_nascita, genere FROM patient WHERE username = ?";
}
