package ispw.foodcare.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public final class DriverManagerConnectionProvider implements ConnectionProvider {
    private final String url;
    private final String user;
    private final String password;

    public DriverManagerConnectionProvider(String url, String user, String password) {
        this.url = Objects.requireNonNull(url, "DB url mancante");
        this.user = user;
        this.password = password;
    }

    @Override public Connection getConnection() throws SQLException {
        /*Apro una nuova connessione; chiudo con try-with-resources nei DAO*/
        return DriverManager.getConnection(url, user, password);
    }
}
