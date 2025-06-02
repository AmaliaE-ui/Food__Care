package ispw.foodcare.utils;

import ispw.foodcare.Role;
import ispw.foodcare.bean.UserBean;

public class SessionManager {

    private static SessionManager instance;
    private UserBean currentUser;

    private SessionManager() {
        // Costruttore privato per singleton
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(UserBean user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public UserBean getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public Role getCurrentRole() {
        return isLoggedIn() ? currentUser.getRole() : null;
    }

    public String getCurrentUsername() {
        return isLoggedIn() ? currentUser.getUsername() : null;
    }
}