package ispw.foodcare.query;

public class QueryAddress {

    private QueryAddress() {}

    public static final String INSERT_ADDRESS = "INSERT INTO address (via, civico, cap, citta, provincia, regione) VALUES (?, ?, ?, ?, ?, ?)";
}
