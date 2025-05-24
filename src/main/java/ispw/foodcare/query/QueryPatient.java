package ispw.foodcare.query;

public class QueryPatient {

    private QueryPatient() {}

    public static final String insert_patient = "INSERT INTO patient (username, data_nascita, genere) VALUES (?, ?, ?)";
}
