package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminDashboardController implements Initializable {
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> cinColumn;
    @FXML private TableColumn<User, String> nomColumn;
    @FXML private TableColumn<User, String> prenomColumn;
    @FXML private TableColumn<User, String> telColumn;
    @FXML private TableColumn<User, String> mailColumn;
    @FXML private TableColumn<User, User.Role> roleColumn;
    @FXML private TableColumn<User, Void> actionsColumn;
    @FXML private TextField searchField;

    private UserService userService;
    private ObservableList<User> userList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userService = new UserService();
        userList = FXCollections.observableArrayList();

        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        telColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        setupActionsColumn();
        loadUsers();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox buttons = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleEditUser(user);
                });

                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleDeleteUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    private void loadUsers() {
        userList.clear();
        List<User> users = userService.getAll();

        // Using Streams to process the list
        userList.addAll(users.stream()
                .collect(Collectors.toList()));  // Collect as list using Stream API

        userTable.setItems(userList);
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();

        // Using Stream to filter the list based on search criteria
        ObservableList<User> filteredList = FXCollections.observableArrayList(
                userList.stream()
                        .filter(user -> user.getNom().toLowerCase().contains(searchText) ||
                                user.getPrenom().toLowerCase().contains(searchText) ||
                                user.getMail().toLowerCase().contains(searchText) ||
                                user.getCin().toLowerCase().contains(searchText))
                        .collect(Collectors.toList())
        );

        userTable.setItems(filteredList);
    }

    private void handleEditUser(User user) {
        openUserForm(user);  // Open form with selected user for editing
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
            }
        });
    }

    @FXML
    private void handleAddUser() {
        openUserForm(null);  // Open form with no user (new user)
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().clearSession();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/signin.fxml"));
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUserForm(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserForm.fxml"));
            Parent root = loader.load();

            UserFormController controller = loader.getController();
            controller.setUser(user);

            Stage stage = new Stage();
            stage.setTitle(user == null ? "Ajouter Utilisateur" : "Modifier Utilisateur");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadUsers(); // Refresh table after adding/editing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
