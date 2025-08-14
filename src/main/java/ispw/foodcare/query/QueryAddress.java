package ispw.foodcare.query;

public class QueryAddress {

    private QueryAddress() {}

    public static final String INSERT_ADDRESS = "INSERT INTO address (via, civico, cap, citta, provincia, regione) VALUES (?, ?, ?, ?, ?, ?)";

    public static final String SELECT_ADDRESS = "SELECT * FROM address WHERE id_address = ?";
}
