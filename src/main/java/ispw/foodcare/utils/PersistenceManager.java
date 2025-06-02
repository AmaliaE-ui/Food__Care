package ispw.foodcare.utils;

import ispw.foodcare.dao.UserDAODatabase;
import ispw.foodcare.dao.UserDAOFileSystem;
import ispw.foodcare.controller.guicontroller.UserDAOInterface;
import ispw.foodcare.dao.UserDAORam;

public class PersistenceManager {
    private static PersistenceManager instance;
    private PersistenceType type;
    private final UserDAOInterface userDAO;

    public enum PersistenceType {
        NO_PERSISTENCE,
        FILE,
        DATABASE
    }

    private PersistenceManager(PersistenceType type) {
        this.type = type;
        switch (type) {
            case FILE -> this.userDAO = new UserDAOFileSystem();
            case DATABASE -> this.userDAO = new UserDAODatabase();
            case NO_PERSISTENCE -> this.userDAO = new UserDAORam();
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static void initialize(PersistenceType type) {
        if (instance == null) {
            instance = new PersistenceManager(type);
        }
    }

    public static PersistenceManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PersistenceManager not initialized.");
        }
        return instance;
    }

    public UserDAOInterface getUserDAO() {
        return userDAO;
    }

    public PersistenceType getPersistenceType() {
        return type;
    }
}

