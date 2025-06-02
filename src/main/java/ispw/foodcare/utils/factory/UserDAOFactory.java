package ispw.foodcare.utils.factory;

import ispw.foodcare.dao.UserDAODatabase;
import ispw.foodcare.dao.UserDAOFileSystem;
import ispw.foodcare.controller.guicontroller.UserDAOInterface;
import ispw.foodcare.dao.UserDAORam;

public class UserDAOFactory {

    //Singeton
    private static UserDAOFactory instance = null;

    private UserDAOFactory() {}

    private UserDAOInterface fileSystemInstance;
    private UserDAOInterface databaseInstance;

    public static synchronized UserDAOFactory getInstance() {
        if (instance == null) {
            instance = new UserDAOFactory();
        }
        return instance;
    }

    //Metodo per ottenere il DAO corretto a seconda della persistenza RAM/DB/FS
    public UserDAOInterface getUserDAO(boolean useDb, boolean useRam) {
        if (useRam){
            return new UserDAORam();
        }
        if(useDb){
            if(databaseInstance == null){ databaseInstance = new UserDAODatabase(); }
            return databaseInstance;
        } else {
            if(fileSystemInstance == null){ fileSystemInstance = new UserDAOFileSystem(); }
            return fileSystemInstance;
        }
    }
}
