package tn.esprit.interfaces;

import javafx.event.ActionEvent;

public interface IUser {
    void login(ActionEvent event);
    void register(ActionEvent event);
    void logout(ActionEvent event);
} 