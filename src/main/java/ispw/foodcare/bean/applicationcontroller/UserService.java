package ispw.foodcare.bean.applicationcontroller;

import ispw.foodcare.dao.UserDAO;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import ispw.foodcare.model.User;
import ispw.foodcare.model.Session;

public class UserService {

    private final UserDAO userDAO = Session.getInstance().getUserDAO();

    // Metodo per registrare un nuovo utente
    public boolean registerUser(User user) {
        try {
            // Salvataggio dell'utente con il DAO corretto (RAM/DB/FS)
            userDAO.saveUser(user);
            return true;
        } catch (AccountAlreadyExistsException e) {
            System.err.println("⚠ Registrazione fallita: utente già esistente.");
            return false;
        } catch (Exception e) {
            System.err.println("❌ Errore durante la registrazione: " + e.getMessage());
            return false;
        }
    }

    // Metodo per ottenere un utente tramite username
    public User loadUserData(User user) {
        return userDAO.getUserByUsername(user.getUsername());
    }

    // Metodo per il login: verifica le credenziali e carica i dati dell'utente
    public User login(String username, String password) {
        return userDAO.checkLogin(username, password);
    }
}

