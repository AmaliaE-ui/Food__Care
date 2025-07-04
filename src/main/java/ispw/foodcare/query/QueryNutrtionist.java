package ispw.foodcare.query;

public class QueryNutrtionist {

    private QueryNutrtionist() {}

    public static final String INSERT_NUTRITIONIST = "INSERT INTO nutritionist (username, piva, titolo_studio, indirizzo_studio, specializzazione) VALUES (?, ?, ?, ?, ?)";

    public static final String SELECT_NUTRITIONIST = "SELECT piva, titolo_studio, indirizzo_studio, specializzazione FROM nutritionist WHERE username = ?";

    public static final String SELECT_BY_CITY = "SELECT * FROM nutritionist \n" +
            "JOIN address ON nutritionist.indirizzo_studio = address.id_address \n" +
            "JOIN user ON nutritionist.username = user.username \n" +
            "WHERE address.citta = ?\n";
}
