package ispw.foodcare.query;

public class QueryAddress {

    private QueryAddress() {}

    public static final String insert_address = "INSERT INTO address (via, civico, cap, citta, provincia, regione) VALUES (?, ?, ?, ?, ?, ?)";
}
