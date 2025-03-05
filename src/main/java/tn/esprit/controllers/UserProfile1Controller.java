package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.utils.SessionManager;

import java.io.IOException;

public class UserProfile1Controller {

    @FXML private ImageView profileImage;
    @FXML private Label fullNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label cinLabel;
    @FXML private Label telLabel;

    private User currentUser;

    @FXML
    public void initialize() {
        // Retrieve the current user from the session
        currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            updateProfileView();
        }
    }

    public void setUser(User user) {
        this.currentUser = user;
        updateProfileView();
    }

    private void updateProfileView() {
        if (currentUser != null) {
            fullNameLabel.setText(currentUser.getNom() + " " + currentUser.getPrenom());
            emailLabel.setText(currentUser.getMail());
            cinLabel.setText(currentUser.getCin());
            telLabel.setText(currentUser.getTel());


        }
    }
    @FXML
    private void handleEditProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserProfile.fxml"));
            Parent root = loader.load();

            // Pass user data to profile controller
            UserProfileController profileController = loader.getController();
            profileController.setUser(SessionManager.getInstance().getCurrentUser());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user_dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
