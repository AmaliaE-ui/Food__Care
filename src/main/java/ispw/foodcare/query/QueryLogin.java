package ispw.foodcare.query;

public class QueryLogin {
    private QueryLogin() {}

    public static final String CHECK_CREDENTIALS = "SELECT * FROM user WHERE username = ? AND password = ?";
}
