package ispw.foodcare.model;

import ispw.foodcare.Role;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.dao.*;

import java.util.HashMap;
import java.util.Map;

public class Session {

    /*Singleton: referenza unica nel sistema*/
    private static Session instance = null;

    /*Campo contenenete l'utente corrente*/
    private UserBean currentUser;

    private final ConnectionProvider cp;
    private final UserDAO userDAO;
    private final NutritionistDAO nutritionistDAO;
    private final AvailabilityDAO availabilityDAO;
    private final AppointmentDAO appointmentDAO;

    private boolean isCLI = false;
    private boolean isDB = false;
    private boolean isRam = false;
    private Map<String, Object> attributes = new HashMap<>();

    /*Costruttore privato per non avere istanziazioni esterne*/
    private Session() {
        String url  = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/foodcare");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASSWORD");
        this.cp = new DriverManagerConnectionProvider(url, user, pass);

        // === DAO con injection del provider ===
        this.userDAO = new UserDAO(cp);
        this.nutritionistDAO = new NutritionistDAO(cp);
        this.availabilityDAO = new AvailabilityDAO(cp);
        this.appointmentDAO  = new AppointmentDAO(cp);
    }

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public UserBean getCurrentUser() { return currentUser; }
    public void setCurrentUser(UserBean currentUser) { this.currentUser = currentUser; }

    public boolean isCLI() { return isCLI; }
    public void setCLI(boolean cli) { isCLI = cli; }
    public boolean isDB() { return isDB; }
    public void setDB(boolean db) { isDB = db; }
    public boolean isRam() { return isRam; }
    public void setRam(boolean ram) { isRam = ram; }

    public UserDAO getUserDAO(){ return userDAO; }

    public NutritionistDAO getNutritionistDAO() {return nutritionistDAO; }

    public AppointmentDAO getAppointmentDAO() {return appointmentDAO; }

    public AvailabilityDAO getAvailabilityDAO() {return availabilityDAO; }

    public void setAttributes(String key, Object value) { attributes.put(key, value); }
    public Object getAttributes(String key) { return attributes.get(key); }
    public void removeAttribute(String key) { attributes.remove(key); }

    public Role getCurrentRole() { return currentUser.getRole(); }
    public void logout() {
        this.currentUser = null;
    }
}
