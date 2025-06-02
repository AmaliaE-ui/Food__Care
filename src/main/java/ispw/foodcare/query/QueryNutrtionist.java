package ispw.foodcare.query;

public class QueryNutrtionist {

    private QueryNutrtionist() {}

    public static final String INSERT_NUTRITIONIST = "INSERT INTO nutritionist (username, piva, titolo_studio, indirizzo_studio, specializzazione) VALUES (?, ?, ?, ?, ?)";

    public static final String SELECT_NUTRITIONIST = "SELECT piva, titolo_studio, indirizzo_studio, specializzazione FROM nutritionist WHERE username = ?";
}
