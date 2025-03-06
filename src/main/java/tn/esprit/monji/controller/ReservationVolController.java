package tn.esprit.monji.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import tn.esprit.monji.model.ReservationVol;
import tn.esprit.monji.model.Vol;
import tn.esprit.monji.model.User;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.monji.service.ReservationVolService;
import tn.esprit.monji.service.VolService;
import tn.esprit.monji.service.UserService;
import javafx.application.Platform;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Map;

public class ReservationVolController implements Initializable {
    @FXML private ListView<ReservationVol> reservationListView;
    
    @FXML private ComboBox<User> userComboBox;
    @FXML private ComboBox<Vol> flightComboBox;
    @FXML private TextField ticketsField;
    @FXML private ComboBox<String> classComboBox;
    @FXML private TextField totalPriceField;
    @FXML private Label messageLabel;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchTypeComboBox;
    @FXML private ComboBox<String> sortByComboBox;

    private ObservableList<ReservationVol> reservationList = FXCollections.observableArrayList();
    private ObservableList<Vol> volList = FXCollections.observableArrayList();
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private FilteredList<ReservationVol> filteredReservations;
    private ReservationVol selectedReservation;
    private final ReservationVolService reservationService = new ReservationVolService();
    private final VolService volService = new VolService();
    private final UserService userService = new UserService();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");

    // Add a refresh timer
    private javafx.animation.Timeline refreshTimer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupListView();
        setupComboBoxes();
        setupSearchBar();
        loadReservations();
        loadVols();
        loadUsers();
        setupRefreshTimer();
    }

    private void setupRefreshTimer() {
        refreshTimer = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(5), event -> {
                loadVols(); // Refresh flights every 5 seconds
            })
        );
        refreshTimer.setCycleCount(javafx.animation.Animation.INDEFINITE);
        refreshTimer.play();
    }

    private void setupListView() {
        reservationListView.setItems(reservationList);
        reservationListView.setCellFactory(lv -> new ListCell<ReservationVol>() {
            @Override
            protected void updateItem(ReservationVol reservation, boolean empty) {
                super.updateItem(reservation, empty);
                if (empty || reservation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox container = new VBox(5);
                    container.getStyleClass().add("reservation-list-cell");

                    // Header with User and Total Price
                    HBox header = new HBox(10);
                    User user = findUserById(reservation.getIdUser());
                    Vol vol = findVolById(reservation.getIdVol());
                    
                    Label userLabel = new Label("👤 " + (user != null ? user.getNom() + " " + user.getPrenom() : "Unknown User"));
                    userLabel.getStyleClass().add("reservation-user");
                    
                    double totalPrice = vol != null ? vol.getPrix() * reservation.getNbBillets() : 0.0;
                    Label priceLabel = new Label(String.format("$%.2f", totalPrice));
                    priceLabel.getStyleClass().add("reservation-price");
                    
                    header.getChildren().addAll(userLabel, new Region(), priceLabel);
                    HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);

                    // Flight Info
                    HBox flightInfo = new HBox(10);
                    String flightText = vol != null ? 
                        "✈️ " + vol.getAeroportDepart() + " ➜ " + vol.getDestination() :
                        "Unknown Flight";
                    Label flightLabel = new Label(flightText);
                    flightLabel.getStyleClass().add("reservation-flight");
                    flightInfo.getChildren().add(flightLabel);

                    // Class and Tickets
                    HBox details = new HBox(10);
                    Label classLabel = new Label("🎫 " + reservation.getClasse());
                    Label ticketsLabel = new Label("🎟️ " + reservation.getNbBillets() + " tickets");
                    details.getChildren().addAll(classLabel, ticketsLabel);
                    classLabel.getStyleClass().add("reservation-details");
                    ticketsLabel.getStyleClass().add("reservation-details");

                    container.getChildren().addAll(header, flightInfo, details);
                    setGraphic(container);
                }
            }
        });

        // Add selection listener
        reservationListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedReservation = newVal;
            if (newVal != null) {
                populateFields(newVal);
            } else {
                clearFields();
            }
        });
    }

    private void setupComboBoxes() {
        // Setup user combo box
        userComboBox.setItems(userList);
        userComboBox.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(String.format("%d - %s %s", user.getId(), user.getNom(), user.getPrenom()));
                }
            }
        });
        userComboBox.setButtonCell(userComboBox.getCellFactory().call(null));

        // Setup flight combo box
        flightComboBox.setItems(volList);
        flightComboBox.setCellFactory(lv -> new ListCell<Vol>() {
            @Override
            protected void updateItem(Vol vol, boolean empty) {
                super.updateItem(vol, empty);
                if (empty || vol == null) {
                    setText(null);
                } else {
                    setText(String.format("%s -> %s (%.2f)",
                            vol.getAeroportDepart(),
                            vol.getDestination(),
                            vol.getPrix()));
                }
            }
        });
        flightComboBox.setButtonCell(flightComboBox.getCellFactory().call(null));

        classComboBox.setItems(FXCollections.observableArrayList(
            "Economy", "Business", "First Class"
        ));

        // Update total price when flight or tickets change
        flightComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalPrice());
        ticketsField.textProperty().addListener((obs, oldVal, newVal) -> updateTotalPrice());
    }

    private void setupSearchBar() {
        // Initialize filtered list
        filteredReservations = new FilteredList<>(reservationList, p -> true);
        reservationListView.setItems(filteredReservations);

        // Add listener to search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateFilterPredicate(newValue);
        });

        // Add listener to search type combo box
        searchTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateFilterPredicate(searchField.getText());
        });
    }

    private void updateFilterPredicate(String searchText) {
        filteredReservations.setPredicate(reservation -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            String searchType = searchTypeComboBox.getValue();
            if (searchType == null) {
                return true;
            }

            String lowerCaseFilter = searchText.toLowerCase();

            switch (searchType) {
                case "User":
                    User user = findUserById(reservation.getIdUser());
                    if (user != null) {
                        return user.getNom().toLowerCase().contains(lowerCaseFilter) ||
                               user.getPrenom().toLowerCase().contains(lowerCaseFilter);
                    }
                    return false;
                case "Flight":
                    Vol vol = findVolById(reservation.getIdVol());
                    if (vol != null) {
                        return vol.getDestination().toLowerCase().contains(lowerCaseFilter) ||
                               vol.getAeroportDepart().toLowerCase().contains(lowerCaseFilter);
                    }
                    return false;
                case "Class":
                    return reservation.getClasse().toLowerCase().contains(lowerCaseFilter);
                default:
                    return true;
            }
        });
    }

    private void loadUsers() {
        try {
            User selectedUser = userComboBox.getValue();
            userList.clear();
            userList.addAll(userService.findAll());
            if (selectedUser != null) {
                userList.stream()
                       .filter(u -> u.getId() == selectedUser.getId())
                       .findFirst()
                       .ifPresent(u -> userComboBox.setValue(u));
            }
        } catch (RuntimeException e) {
            showMessage("Error loading users: " + e.getMessage(), true);
        }
    }

    private void loadReservations() {
        try {
            reservationList.clear();
            reservationList.addAll(reservationService.findAll());
        } catch (RuntimeException e) {
            showMessage("Error loading reservations: " + e.getMessage(), true);
        }
    }

    private void loadVols() {
        try {
            // Store selected flight
            Vol selectedFlight = flightComboBox.getValue();
            
            // Refresh list
            volList.clear();
            volList.addAll(volService.findAll());
            
            // Restore selected flight if it still exists
            if (selectedFlight != null) {
                volList.stream()
                      .filter(v -> v.getId() == selectedFlight.getId())
                      .findFirst()
                      .ifPresent(v -> flightComboBox.setValue(v));
            }
        } catch (RuntimeException e) {
            showMessage("Error loading flights: " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleAdd() {
        try {
            System.out.println("=== Starting reservation process ===");
            System.out.println("Selected user: " + userComboBox.getValue());
            System.out.println("Selected flight: " + flightComboBox.getValue());
            System.out.println("Selected class: " + classComboBox.getValue());
            System.out.println("Number of tickets: " + ticketsField.getText());
            
            ReservationVol newReservation = createReservationFromFields();
            System.out.println("Created reservation object: " + newReservation);
            
            reservationService.add(newReservation);
            System.out.println("Reservation added successfully to database");
            
            loadReservations();
            clearFields();
            showMessage("Reservation added successfully", false);
        } catch (Exception e) {
            System.out.println("Error in handleAdd: " + e.getMessage());
            e.printStackTrace();
            showMessage("Error adding reservation: " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedReservation == null) {
            showMessage("Please select a reservation to update", true);
            return;
        }

        try {
            updateReservationFromFields(selectedReservation);
            reservationService.update(selectedReservation);
            loadReservations(); // Reload to refresh all data
            showMessage("Reservation updated successfully", false);
        } catch (Exception e) {
            showMessage("Error updating reservation: " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedReservation == null) {
            showMessage("Please select a reservation to delete", true);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Delete Reservation");
        alert.setContentText("Are you sure you want to delete this reservation?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                reservationService.delete(selectedReservation.getId());
                loadReservations(); // Reload to refresh all data
                clearFields();
                showMessage("Reservation deleted successfully", false);
            } catch (Exception e) {
                showMessage("Error deleting reservation: " + e.getMessage(), true);
            }
        }
    }

    @FXML
    private void handleClear() {
        clearFields();
        selectedReservation = null;
        reservationListView.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleAircraftApiPopup() {
        // Call the aircraft API and retrieve data
        // For demonstration, let's assume we have a method fetchAircraftData() that returns a String
        String aircraftData = fetchAircraftData();

        // Create a dialog to display the aircraft data
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aircraft Information");
        alert.setHeaderText(null);
        alert.setContentText(aircraftData);
        alert.showAndWait();
    }

    private String fetchAircraftData() {
        String apiUrl = "https://api.api-ninjas.com/v1/aircraft";
        StringBuilder result = new StringBuilder();
        try {
            // Example parameters, you can modify this based on user input
            String manufacturer = "Gulfstream";
            String model = "G550";
            String requestUrl = String.format("%s?manufacturer=%s&model=%s", apiUrl, manufacturer, model);

            // Create a URL object
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Api-Key", "D+op30gmMeJ0geFc31syIg==Lq1jiRpNh69NYT9w");

            // Check the response code
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    result.append(inputLine);
                }
                in.close();

                // Parse the JSON response
                JSONArray aircraftArray = new JSONArray(result.toString());
                if (aircraftArray.length() > 0) {
                    JSONObject aircraft = aircraftArray.getJSONObject(0);
                    return String.format("Manufacturer: %s\nModel: %s\nEngine Type: %s\nMax Speed: %s knots\nRange: %s nautical miles", 
                        aircraft.getString("manufacturer"), 
                        aircraft.getString("model"), 
                        aircraft.getString("engine_type"), 
                        aircraft.getString("max_speed_knots"), 
                        aircraft.getString("range_nautical_miles")
                    );
                } else {
                    return "No aircraft data found.";
                }
            } else {
                return "Error: " + conn.getResponseCode();
            }
        } catch (Exception e) {
            return "Error fetching aircraft data: " + e.getMessage();
        }
    }

    private ReservationVol createReservationFromFields() {
        validateFields();
        return new ReservationVol(
            0, // ID will be set by database
            userComboBox.getValue().getId(),
            flightComboBox.getValue().getId(),
            classComboBox.getValue(),
            Integer.parseInt(ticketsField.getText())
        );
    }

    private void updateReservationFromFields(ReservationVol reservation) {
        validateFields();
        reservation.setIdUser(userComboBox.getValue().getId());
        reservation.setIdVol(flightComboBox.getValue().getId());
        reservation.setClasse(classComboBox.getValue());
        reservation.setNbBillets(Integer.parseInt(ticketsField.getText()));
    }

    private void validateFields() {
        StringBuilder errors = new StringBuilder();

        if (userComboBox.getValue() == null) errors.append("User is required\n");
        if (flightComboBox.getValue() == null) errors.append("Flight is required\n");
        if (classComboBox.getValue() == null) errors.append("Class is required\n");
        if (ticketsField.getText().isEmpty()) {
            errors.append("Number of tickets is required\n");
        } else {
            try {
                int tickets = Integer.parseInt(ticketsField.getText());
                if (tickets < 1) errors.append("Number of tickets must be at least 1\n");
            } catch (NumberFormatException e) {
                errors.append("Invalid number of tickets\n");
            }
        }

        if (errors.length() > 0) {
            throw new IllegalArgumentException(errors.toString());
        }
    }

    private void populateFields(ReservationVol reservation) {
        User user = findUserById(reservation.getIdUser());
        userComboBox.setValue(user);
        flightComboBox.setValue(findVolById(reservation.getIdVol()));
        classComboBox.setValue(reservation.getClasse());
        ticketsField.setText(String.valueOf(reservation.getNbBillets()));
    }

    private void clearFields() {
        userComboBox.setValue(null);
        flightComboBox.setValue(null);
        classComboBox.setValue(null);
        ticketsField.clear();
        totalPriceField.clear();
    }

    private Vol findVolById(int id) {
        return volList.stream()
                     .filter(v -> v.getId() == id)
                     .findFirst()
                     .orElse(null);
    }

    private User findUserById(int id) {
        return userList.stream()
                      .filter(u -> u.getId() == id)
                      .findFirst()
                      .orElse(null);
    }

    private void showMessage(String message, boolean isError) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        }
    }

    private void updateTotalPrice() {
        Vol selectedFlight = flightComboBox.getValue();
        if (selectedFlight != null && !ticketsField.getText().isEmpty()) {
            try {
                int tickets = Integer.parseInt(ticketsField.getText());
                double total = selectedFlight.getPrix() * tickets;
                totalPriceField.setText(String.format("%.2f", total));
            } catch (NumberFormatException e) {
                totalPriceField.setText("Invalid");
            }
        } else {
            totalPriceField.setText("");
        }
    }

    @FXML
    private void handleSort() {
        String sortCriteria = sortByComboBox.getValue();
        if (sortCriteria == null) {
            showMessage("Please select a sort criteria", true);
            return;
        }

        Comparator<ReservationVol> comparator = switch (sortCriteria) {
            case "Price (Low to High)" -> Comparator.comparingDouble(res -> {
                Vol vol = findVolById(res.getIdVol());
                return vol != null ? vol.getPrix() * res.getNbBillets() : 0.0;
            });
            case "Price (High to Low)" -> Comparator.comparingDouble(res -> {
                Vol vol = findVolById(res.getIdVol());
                return vol != null ? -1 * vol.getPrix() * res.getNbBillets() : 0.0;
            });
            case "Number of Tickets" -> Comparator.comparingInt(ReservationVol::getNbBillets).reversed();
            case "Class" -> Comparator.comparing(ReservationVol::getClasse);
            default -> null;
        };

        if (comparator != null) {
            FXCollections.sort(reservationList, comparator);
            showMessage("Reservations sorted by " + sortCriteria, false);
        }
    }

    @FXML
    private void handleStreamStats() {
        // Calculate statistics using streams
        DoubleSummaryStatistics priceStats = reservationList.stream()
            .mapToDouble(res -> {
                Vol vol = findVolById(res.getIdVol());
                return vol != null ? vol.getPrix() * res.getNbBillets() : 0.0;
            })
            .summaryStatistics();

        Map<String, Long> classDistribution = reservationList.stream()
            .collect(Collectors.groupingBy(ReservationVol::getClasse, Collectors.counting()));

        // Calculate average tickets per class
        Map<String, Double> avgTicketsPerClass = reservationList.stream()
            .collect(Collectors.groupingBy(
                ReservationVol::getClasse,
                Collectors.averagingInt(ReservationVol::getNbBillets)
            ));

        // Create statistics message
        StringBuilder stats = new StringBuilder("Reservation Statistics:\n\n");
        stats.append(String.format("Total Reservations: %d\n", reservationList.size()));
        stats.append(String.format("Total Revenue: $%.2f\n", priceStats.getSum()));
        stats.append(String.format("Average Reservation Price: $%.2f\n", priceStats.getAverage()));
        stats.append(String.format("Highest Reservation Price: $%.2f\n", priceStats.getMax()));
        stats.append(String.format("Lowest Reservation Price: $%.2f\n\n", priceStats.getMin()));
        
        stats.append("Class Distribution:\n");
        classDistribution.forEach((className, count) -> 
            stats.append(String.format("%s: %d reservations\n", className, count)));

        stats.append("\nAverage Tickets per Class:\n");
        avgTicketsPerClass.forEach((className, avgTickets) ->
            stats.append(String.format("%s: %.1f tickets\n", className, avgTickets)));

        // Show statistics in a dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservation Statistics");
        alert.setHeaderText(null);
        alert.setContentText(stats.toString());
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();
    }
} 