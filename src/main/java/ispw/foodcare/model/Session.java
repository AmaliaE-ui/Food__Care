package ispw.foodcare.model;

import ispw.foodcare.Role;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.dao.*;
import ispw.foodcare.utils.InMemoryAppointmentSubject;
import ispw.foodcare.utils.patternobserver.AppointmentSubject;

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
    private final AddressDAO addressDAO;

    /*patetrn observer*/
    private final AppointmentSubject appointmentSubject = new InMemoryAppointmentSubject();
    public AppointmentSubject getAppointmentSubject() {
        return appointmentSubject; }

    private boolean isCLI = false;
    private boolean isDB = false;
    private boolean isRam = false;
    private boolean isFS=false;
    private final Map<String, Object> attributes = new HashMap<>();

    /*Costruttore privato per non avere istanziazioni esterne*/
    private Session() {
        String url  = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/foodcare");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASSWORD");
        this.cp = new DriverManagerConnectionProvider(url, user, pass);

        // === DAO con injection del provider ===
        this.addressDAO     = new AddressDAO(cp);
        this.userDAO        = new UserDAO(cp, addressDAO);        // <— passa il DAO
        this.nutritionistDAO= new NutritionistDAO(cp, addressDAO);
        this.availabilityDAO = new AvailabilityDAO(cp);
        this.appointmentDAO  = new AppointmentDAO(cp, this.availabilityDAO);
    }

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public UserBean getCurrentUser() { return currentUser; }
    public void setCurrentUser(UserBean currentUser) { this.currentUser = currentUser; }

    public void setCLI(boolean cli) { isCLI = cli; }
    public void setDB(boolean db) { isDB = db; }
    public void setRam(boolean ram) { isRam = ram; }
    public void setFs(boolean fs) { isFS = fs; }

    public boolean getDB() { return isDB; }
    public boolean getRam() { return isRam; }
    public boolean getFS() { return isFS; }

    public UserDAO getUserDAO(){ return userDAO; }
    public NutritionistDAO getNutritionistDAO() {return nutritionistDAO; }
    public AppointmentDAO getAppointmentDAO() {return appointmentDAO; }
    public AvailabilityDAO getAvailabilityDAO() {return availabilityDAO; }

    public void setAttributes(String key, Object value) { attributes.put(key, value); }
    public Object getAttributes(String key) { return attributes.get(key); }
    public void removeAttribute(String key) { attributes.remove(key); }
}
