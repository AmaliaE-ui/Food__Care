package ispw.foodcare.model;

import ispw.foodcare.Role;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.dao.AppointmentDAO;
import ispw.foodcare.dao.AvailabilityDAO;
import ispw.foodcare.dao.NutritionistDAO;
import ispw.foodcare.dao.UserDAO;

import java.util.HashMap;
import java.util.Map;

public class Session {

    //Singleton: referenza unica nel sistema
    private static Session instance = null;

    //Campo contenenete l'utente corrente
    private UserBean currentUser;

   // private UserDAOInterface userDAO;
    private UserDAO userDAO;
    private NutritionistDAO nutritionistDAO;
    private AvailabilityDAO availabilityDAO;
    private AppointmentDAO appointmentDAO;

    private boolean isCLI = false;
    private boolean isDB = false;
    private boolean isRam = false;
    private Map<String, Object> attributes = new HashMap<>();

    //Costruttore privato per non avere istanziazioni esterne
    private Session() {}

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
    public void setCLI(boolean cli) { isCLI = cli; }
    public boolean isDB() { return isDB; }
    public void setDB(boolean db) { isDB = db; }
    public boolean isRam() { return isRam; }
    public void setRam(boolean ram) { isRam = ram; }

    public UserDAO getUserDAO(){ return userDAO; }
    public void setUserDAO(UserDAO userDAO){ this.userDAO = userDAO; }

    public NutritionistDAO getNutritionistDAO() {return nutritionistDAO; }
    public void setNutritionistDAO(NutritionistDAO nutritionistDAO) {this.nutritionistDAO = nutritionistDAO; }

    public AppointmentDAO getAppointmentDAO() {return appointmentDAO; }
    public void setAppointmentDAO(AppointmentDAO appointmentDAO) {this.appointmentDAO = appointmentDAO; }

    public AvailabilityDAO getAvailabilityDAO() {return availabilityDAO; }
    public void setAvailabilityDAO(AvailabilityDAO availabilityDAO) {this.availabilityDAO = availabilityDAO; }

    public void setAttributes(String key, Object value) { attributes.put(key, value); }
    public Object getAttributes(String key) { return attributes.get(key); }
    public void removeAttribute(String key) { attributes.remove(key); }

    public Role getCurrentRole() { return currentUser.getRole(); }
    public void logout() {
        this.currentUser = null;
    }

}
