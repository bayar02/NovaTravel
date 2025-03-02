package tn.esprit.services;

import tn.esprit.entities.User;
import java.sql.SQLException;

public class UserService extends ServiceUser {
    public boolean register(User user) throws SQLException {
        try {
            ajouter(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

} 