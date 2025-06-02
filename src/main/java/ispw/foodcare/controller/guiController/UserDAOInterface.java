package ispw.foodcare.controller.guicontroller;

import ispw.foodcare.bean.UserBean;
import ispw.foodcare.model.User;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import java.io.FileNotFoundException;

public interface UserDAOInterface {

    //void saveUser(String emial, Stirng passwowrd) ...
    void saveUser(User user) throws AccountAlreadyExistsException;
    User getUserByEmail(String email);
    UserBean checkLogin(String email, String password);
    void updateUserModelData(User currentUser);
    User loadUserData(User user) throws FileNotFoundException;
}
