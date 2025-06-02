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

    //Con questi controlli, ogni volta che chiamo DBManager.getIstance().getConnection(), sono sicura di avere una connessione attiva
    public static DBManager getInstance() throws SQLException {
        //connection == null - copre i casi in cui la connessione non è mai stata creata
        if(instance == null || instance.connection == null || instance.connection.isClosed()){
            instance = new DBManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        //Controlle di no usare una Connection chiusa
        if(connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
