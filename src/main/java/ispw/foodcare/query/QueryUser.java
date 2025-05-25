package ispw.foodcare.query;

public class QueryUser {

    private QueryUser() {}

    public static final String INSERT_USER = "INSERT INTO user (username, password, ruolo, nome, cognome, n_telefono, email) VALUES (?, ?, ?, ?, ?, ?, ?)"
}
