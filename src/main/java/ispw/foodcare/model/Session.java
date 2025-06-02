package ispw.foodcare.model;

import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.guicontroller.UserDAOInterface;

public class Session {
    //Singleton: referenza unica nel sistema
    private static Session instance = null;

    //Campo contenenete l'utente corrente
    private UserBean currentUser;
    private UserDAOInterface userDAO;
    private boolean isCLI = false;
    private boolean isDB = false;
    private boolean isRam = false;

    //Costruttore privato per non avere istanziazioni esterne
    private Session() {}

    //
    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void clear(){ this.currentUser = null; }

    public UserBean getCurrentUser() { return currentUser; }
    public void setCurrentUser(UserBean currentUser) { this.currentUser = currentUser; }
    public boolean isCLI() { return isCLI; }
    public void setCLI(boolean CLI) { isCLI = CLI; }
    public boolean isDB() { return isDB; }
    public void setDB(boolean DB) { isDB = DB; }
    public boolean isRam() { return isRam; }
    public void setRam(boolean Ram) { isRam = Ram; }
    public UserDAOInterface getUserDAO() { return userDAO; }
    public void setUserDAO(UserDAOInterface userDAO) { this.userDAO = userDAO; }
}
