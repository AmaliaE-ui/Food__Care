package ispw.foodcare.query;

public class QueryPatient {

    private QueryPatient() {}

    public static final String INSERT_PATIENT = "INSERT INTO patient (username, data_nascita, genere) VALUES (?, ?, ?)";
}
