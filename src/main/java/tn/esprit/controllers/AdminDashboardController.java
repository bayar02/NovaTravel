package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;
import tn.esprit.utils.SessionManager;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminDashboardController implements Initializable {
    @FXML private ListView<User> userListView;
    @FXML private TextField searchField;
    @FXML
    private Button backButton;

    @FXML
    private void handleBack() {
        System.out.println("Going back...");
        // Example: Close the current window
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    private final UserService userService = new UserService();
    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private final AdminController adminController = new AdminController(); // Initialize AdminController

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadUsers();
        setupListViewCellFactory();
    }

    private void setupListViewCellFactory() {
        userListView.setCellFactory(param -> new ListCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    User user = getItem();
                    if (user != null) handleEditUser(user);
                });

                deleteButton.setOnAction(event -> {
                    User user = getItem();
                    if (user != null) handleDeleteUser(user);
                });
            }

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setPrefWidth(userListView.getWidth() - 20);

                    Label nomLabel = createColumnLabel(user.getNom(), 100);
                    Label prenomLabel = createColumnLabel(user.getPrenom(), 100);
                    Label cinLabel = createColumnLabel(user.getCin(), 120);
                    Label telLabel = createColumnLabel(user.getTel(), 120);
                    Label emailLabel = createColumnLabel(user.getMail(), 200);
                    Label roleLabel = createColumnLabel(user.getRole().toString(), 120);

                    HBox buttonsBox = new HBox(10, editButton, deleteButton);
                    GridPane.setHgrow(buttonsBox, Priority.NEVER);

                    grid.add(nomLabel, 0, 0);
                    grid.add(prenomLabel, 1, 0);
                    grid.add(cinLabel, 2, 0);
                    grid.add(telLabel, 3, 0);
                    grid.add(emailLabel, 4, 0);
                    grid.add(roleLabel, 5, 0);
                    grid.add(buttonsBox, 6, 0);

                    setText(null);
                    setGraphic(grid);
                }
            }
        });
    }

    private Label createColumnLabel(String text, double width) {
        Label label = new Label(text);
        label.setMinWidth(width);
        label.setMaxWidth(width);
        label.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-background-color: white;");
        return label;
    }

    private void loadUsers() {
        userList.clear();
        List<User> users = userService.getAll();
        userList.addAll(users);
        userListView.setItems(userList);
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<User> filteredList = FXCollections.observableArrayList(
                userList.stream()
                        .filter(user -> user.getNom().toLowerCase().contains(searchText) ||
                                user.getPrenom().toLowerCase().contains(searchText) ||
                                user.getMail().toLowerCase().contains(searchText) ||
                                user.getCin().toLowerCase().contains(searchText))
                        .collect(Collectors.toList())
        );
        userListView.setItems(filteredList);
    }

    private void handleEditUser(User user) {
        openUserForm(user);

        // Collect the updated information
        String updatedInfo = "Name: " + user.getNom() + " " + user.getPrenom() +
                "\nEmail: " + user.getMail() +
                "\nRole: " + user.getRole();

        // Notify the user about the update
        adminController.notifyUserOfChanges(user, updatedInfo, false);
    }


    private void handleDeleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'utilisateur");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                userService.supprimer(user.getId());
                loadUsers();

                // Notify the user about the deletion
                adminController.notifyUserOfChanges(user, null, true);  // true means it's deletion
            }
        });
    }



    @FXML
    private void handleAddUser() {
        openUserForm2(null);

    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().clearSession();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/signin.fxml"));
            Stage stage = (Stage) userListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUserForm(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserForm.fxml"));
            Parent root = loader.load();

            UserFormController controller = loader.getController();
            controller.setUser(user);

            Stage stage = new Stage();
            stage.setTitle(user == null ? "Ajouter Utilisateur" : "Modifier Utilisateur");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadUsers();

            // After editing, send an email with the updated user information
            if (user != null) {
                adminController.notifyUserOfChanges(user, "Vos informations ont été mises à jour.", false);            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUserForm2(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserForm2.fxml"));
            Parent root = loader.load();

            UserFormController controller = loader.getController();
            controller.setUser(user);

            Stage stage = new Stage();
            stage.setTitle(user == null ? "Ajouter Utilisateur" : "Modifier Utilisateur");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadUsers();

            // After editing, send an email with the updated user information
            if (user != null) {
                adminController.notifyUserOfChanges(user, "Vos informations ont été mises à jour.", false);            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/acceuil_admin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
