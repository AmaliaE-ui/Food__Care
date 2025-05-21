package ispw.foodcare.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static DBManager instance;
    private static final String URL = "jdbc:mysql://localhost:3306/foodcare";
    private static final String USER = "root";
    private static final String PASSWORD = "Primolevi1!";

    private Connection connection;

    private DBManager() throws SQLException {
        try{
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (SQLException e){
            throw new SQLException("Errore nella connessione al database");
        }
    }

    public static DBManager getInstance() throws SQLException {
        if(instance == null || instance.getConnection() == null){
            instance = new DBManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
