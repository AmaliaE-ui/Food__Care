package ispw.foodcare.query;

public class QueryNutrtionist {

    private QueryNutrtionist() {}

    public static final String insert_nutritionist = "INSERT INTO nutritionist (username, piva, titolo_studio, indirizzo_studio, specializzazione) VALUES (?, ?, ?, ?, ?)";
}
