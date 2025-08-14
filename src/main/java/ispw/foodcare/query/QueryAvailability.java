package ispw.foodcare.query;

public class QueryAvailability {

    private QueryAvailability() {}

    public static final String INSERT_AVAILABILITY =
            "INSERT INTO availability (username_nutrizionista, data, ora_inizio, ora_fine) VALUES (?, ?, ?, ?)";

    public static final String SELECT_AVAILABILITIES_FOR_NUTRITIONIST =
            "SELECT data, ora_inizio, ora_fine FROM availability WHERE username_nutrizionista = ?";

    public static final String DELETE_AVAILABILITY =
            "DELETE FROM availability WHERE username_nutrizionista = ? AND data = ? AND ora_inizio = ? AND ora_fine = ?";

    public static final String DELETE_AVAILABILITY_BY_DATE =
            "DELETE FROM availability WHERE data < ?";

}
