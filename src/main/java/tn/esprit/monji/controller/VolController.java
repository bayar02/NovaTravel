package tn.esprit.monji.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;

import javafx.stage.Stage;
import tn.esprit.monji.model.Vol;
import java.time.LocalDate;
import java.time.ZoneId;
import tn.esprit.monji.service.VolService;

public class VolController implements Initializable {
    @FXML private ListView<Vol> flightListView;
    
    @FXML private TextField departureAirportField;
    @FXML private TextField destinationField;
    @FXML private DatePicker departureDatePicker;
    @FXML private DatePicker arrivalDatePicker;
    @FXML private TextField priceField;
    @FXML private TextField compagnieField;
    @FXML private Label messageLabel;
    
    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchTypeComboBox;
    @FXML private ComboBox<String> sortByComboBox;

    private ObservableList<Vol> volList = FXCollections.observableArrayList();
    private FilteredList<Vol> filteredVols;
    private Vol selectedVol;
    private final VolService volService = new VolService();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupListView();
        setupSearchBar();
        loadVols();
        setupDatePickers();
    }

    private void setupListView() {
        flightListView.setItems(volList);
        flightListView.setCellFactory(lv -> new ListCell<Vol>() {
            @Override
            protected void updateItem(Vol vol, boolean empty) {
                super.updateItem(vol, empty);
                if (empty || vol == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox container = new VBox(5);
                    container.getStyleClass().add("flight-list-cell");

                    // Header with Company and Price
                    HBox header = new HBox(10);
                    Label companyLabel = new Label("✈️ " + vol.getCompagnie());
                    companyLabel.getStyleClass().add("flight-company");
                    Label priceLabel = new Label(String.format("$%.2f", vol.getPrix()));
                    priceLabel.getStyleClass().add("flight-price");
                    header.getChildren().addAll(companyLabel, new Region(), priceLabel);
                    HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);

                    // Route
                    HBox route = new HBox(10);
                    Label fromTo = new Label(vol.getAeroportDepart() + " ➜ " + vol.getDestination());
                    fromTo.getStyleClass().add("flight-route");
                    route.getChildren().add(fromTo);

                    // Dates
                    HBox dates = new HBox(10);
                    Label departureDate = new Label("🛫 " + dateFormat.format(vol.getDateDepart()));
                    Label arrivalDate = new Label("🛬 " + dateFormat.format(vol.getDateArrivee()));
                    dates.getChildren().addAll(departureDate, arrivalDate);
                    departureDate.getStyleClass().add("flight-date");
                    arrivalDate.getStyleClass().add("flight-date");

                    container.getChildren().addAll(header, route, dates);
                    setGraphic(container);
                }
            }
        });

        // Add selection listener
        flightListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedVol = newVal;
            if (newVal != null) {
                populateFields(newVal);
            } else {
                clearFields();
            }
        });
    }

    private void setupSearchBar() {
        // Initialize filtered list
        filteredVols = new FilteredList<>(volList, p -> true);
        flightListView.setItems(filteredVols);

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
        filteredVols.setPredicate(vol -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            String searchType = searchTypeComboBox.getValue();
            if (searchType == null) {
                return true;
            }

            String lowerCaseFilter = searchText.toLowerCase();

            switch (searchType) {
                case "Company":
                    return vol.getCompagnie().toLowerCase().contains(lowerCaseFilter);
                case "Destination":
                    return vol.getDestination().toLowerCase().contains(lowerCaseFilter);
                case "Departure":
                    return vol.getAeroportDepart().toLowerCase().contains(lowerCaseFilter);
                default:
                    return true;
            }
        });
    }

    private void loadVols() {
        try {
            volList.clear();
            volList.addAll(volService.findAll());
            updateFilterPredicate(searchField.getText()); // Reapply filter after loading
        } catch (RuntimeException e) {
            showMessage("Error loading flights: " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleAdd() {
        try {
            Vol newVol = createVolFromFields();
            volService.add(newVol);
            loadVols(); // Reload to get the new ID
            clearFields();
            showMessage("Flight added successfully", false);
        } catch (Exception e) {
            showMessage("Error adding flight: " + e.getMessage(), true);
        }
    }

    @FXML
    private void goToSearchAirport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reseachAireport.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Airport Search");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedVol == null) {
            showMessage("Please select a flight to update", true);
            return;
        }

        try {
            // Create a new Vol object with the updated values
            Vol updatedVol = createVolFromFields();
            updatedVol.setId(selectedVol.getId()); // Keep the same ID
            updatedVol.setCompagnie(selectedVol.getCompagnie()); // Keep the same company
            
            // Update in database
            volService.update(updatedVol);
            
            // Refresh the list
            loadVols();
            
            // Reselect the updated flight
            for (Vol vol : volList) {
                if (vol.getId() == updatedVol.getId()) {
                    flightListView.getSelectionModel().select(vol);
                    break;
                }
            }
            
            showMessage("Flight updated successfully", false);
        } catch (Exception e) {
            showMessage("Error updating flight: " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedVol == null) {
            showMessage("Please select a flight to delete", true);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Delete Flight");
        alert.setContentText("Are you sure you want to delete this flight?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                volService.delete(selectedVol.getId());
                loadVols(); // Reload to refresh the list
                clearFields();
                showMessage("Flight deleted successfully", false);
            } catch (Exception e) {
                showMessage("Error deleting flight: " + e.getMessage(), true);
            }
        }
    } 

    @FXML
    private void handleClear() {
        clearFields();
        selectedVol = null;
        flightListView.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSort() {
        String sortCriteria = sortByComboBox.getValue();
        if (sortCriteria == null) {
            showMessage("Please select a sort criteria", true);
            return;
        }

        Comparator<Vol> comparator = switch (sortCriteria) {
            case "Price (Low to High)" -> Comparator.comparingDouble(Vol::getPrix);
            case "Price (High to Low)" -> Comparator.comparingDouble(Vol::getPrix).reversed();
            case "Departure Date" -> Comparator.comparing(Vol::getDateDepart);
            case "Company" -> Comparator.comparing(Vol::getCompagnie);
            default -> null;
        };

        if (comparator != null) {
            FXCollections.sort(volList, comparator);
            showMessage("Flights sorted by " + sortCriteria, false);
        }
    }

    @FXML
    private void handleStreamStats() {
        // Calculate statistics using streams
        DoubleSummaryStatistics priceStats = volList.stream()
            .mapToDouble(Vol::getPrix)
            .summaryStatistics();

        Map<String, Long> companyDistribution = volList.stream()
            .collect(Collectors.groupingBy(Vol::getCompagnie, Collectors.counting()));

        Map<String, Double> avgPriceByCompany = volList.stream()
            .collect(Collectors.groupingBy(
                Vol::getCompagnie,
                Collectors.averagingDouble(Vol::getPrix)
            ));

        // Create statistics message
        StringBuilder stats = new StringBuilder("Flight Statistics:\n\n");
        stats.append(String.format("Total Flights: %d\n", volList.size()));
        stats.append(String.format("Average Flight Price: $%.2f\n", priceStats.getAverage()));
        stats.append(String.format("Highest Flight Price: $%.2f\n", priceStats.getMax()));
        stats.append(String.format("Lowest Flight Price: $%.2f\n\n", priceStats.getMin()));
        
        stats.append("Flights by Company:\n");
        companyDistribution.forEach((company, count) -> 
            stats.append(String.format("%s: %d flights\n", company, count)));

        stats.append("\nAverage Price by Company:\n");
        avgPriceByCompany.forEach((company, avgPrice) ->
            stats.append(String.format("%s: $%.2f\n", company, avgPrice)));

        // Show statistics in a dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Flight Statistics");
        alert.setHeaderText(null);
        alert.setContentText(stats.toString());
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();
    }

    private Vol createVolFromFields() {
        validateFields();
        Vol vol = new Vol(
            0, // ID will be set by database
            compagnieField.getText(),
            departureAirportField.getText(),
            destinationField.getText(), // Arrival airport
            destinationField.getText(), // Destination
            Date.from(departureDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
            Date.from(arrivalDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
            Double.parseDouble(priceField.getText())
        );
        return vol;
    }

    private void validateFields() {
        StringBuilder errors = new StringBuilder();

        if (compagnieField.getText().isEmpty()) errors.append("Company is required\n");
        if (departureAirportField.getText().isEmpty()) errors.append("Departure airport is required\n");
        if (destinationField.getText().isEmpty()) errors.append("Destination is required\n");
        if (departureDatePicker.getValue() == null) errors.append("Departure date is required\n");
        if (arrivalDatePicker.getValue() == null) errors.append("Arrival date is required\n");
        if (priceField.getText().isEmpty()) errors.append("Price is required\n");
        else {
            try {
                double prix = Double.parseDouble(priceField.getText());
                if (prix <= 0) errors.append("Price must be positive\n");
            } catch (NumberFormatException e) {
                errors.append("Invalid price\n");
            }
        }

        if (errors.length() > 0) {
            throw new IllegalArgumentException(errors.toString());
        }
    }

    private void populateFields(Vol vol) {
        departureAirportField.setText(vol.getAeroportDepart());
        destinationField.setText(vol.getDestination());
        departureDatePicker.setValue(vol.getDateDepart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        arrivalDatePicker.setValue(vol.getDateArrivee().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        priceField.setText(String.valueOf(vol.getPrix()));
        compagnieField.setText(vol.getCompagnie());
    }

    private void clearFields() {
        departureAirportField.clear();
        destinationField.clear();
        departureDatePicker.setValue(null);
        arrivalDatePicker.setValue(null);
        priceField.clear();
        compagnieField.clear();
        messageLabel.setText("");
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    private void setupDatePickers() {
        LocalDate today = LocalDate.now();
        departureDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && (item.isBefore(today))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // Optional: style for disabled dates
                }
            }
        });
        arrivalDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && (item.isBefore(today))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // Optional: style for disabled dates
                }
            }
        });
    }
} 