package nl.inholland.javafx.logic;

import nl.inholland.javafx.dal.Database;
import nl.inholland.javafx.model.User;

import java.util.List;

public class UserService {

    private Database db;

    public UserService (Database db) {
        this.db = db;
    }

    public
    List<User> getAllUsers () {
        return db.getUsers();
    }

    public User validateCredentials (String username, String password) {
        List<User> users = getAllUsers();
        for (User u: users
             ) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
