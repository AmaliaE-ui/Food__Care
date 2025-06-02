package ispw.foodcare.dao;

import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.guicontroller.UserDAOInterface;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import ispw.foodcare.model.*;
import ispw.foodcare.utils.Converter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class UserDAORam implements UserDAOInterface {

    private final List<User> userList = new ArrayList<>();

    @Override
    public void saveUser(User user) throws AccountAlreadyExistsException {
        if (getUserByEmail(user.getUsername()) != null) {
            throw new AccountAlreadyExistsException("User già esistente con username: " + user.getUsername());
        }
        userList.add(user);
    }

    @Override
    public void updateUserModelData(User user) {
        //In RAM non ha persistenza, non faccio nulla
        /*Cerca l'utente in memoria con lo stesso username
        * se lo trovo, lo sostiutisco
        * se non o trovo, lo aggiungo*/
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equals(user.getUsername())) {
                userList.set(i, user);
                return;
            }
        }

        //Altrimenti lo aggiungo
        userList.add(user);
    }

    @Override
    public User getUserByEmail(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserBean checkLogin(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return Converter.userToBean(user);
            }
        }
        return null;
    }

    @Override
    public User loadUserData(User user) throws FileNotFoundException {
        return getUserByEmail(user.getUsername());
    }

    public List<User> getUserList() {
        return userList;
    }
}