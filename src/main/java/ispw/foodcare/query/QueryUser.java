package ispw.foodcare.query;

public class QueryUser {

    private QueryUser() {}

    public static final String INSERT_USER = "INSERT INTO user (username, password, ruolo, nome, cognome, n_telefono, email) VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String SELECT_BY_USERNAME = "SELECT * FROM user WHERE username = ?";
}
