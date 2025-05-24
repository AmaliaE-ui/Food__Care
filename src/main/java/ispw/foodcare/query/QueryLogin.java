package ispw.foodcare.query;

public class QueryLogin {
    private QueryLogin() {}

    public static final String check_credentials = "SELECT * FROM user WHERE username = ? AND password = ?";
}
