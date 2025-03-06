package tn.esprit.monji.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ReseachAireport {
    @FXML
    private TextField airportInput;
    @FXML
    private TextArea apiOutput;
    @FXML
    private Button searchButton;
    @FXML
    private Button clearButton;

    @FXML
    private void searchAirport() {
        String airportName = airportInput.getText().trim();
        if (airportName.isEmpty()) {
            showError("Please enter an airport name!");
            return;
        }

        String data = NinjasAPI.getAirportData(airportName);
        if (data == null || data.isEmpty()) {
            showError("No data found for: " + airportName);
        } else {
            apiOutput.setText(formatResponse(data));
        }
    }

    @FXML
    private void clearFields() {
        airportInput.clear();
        apiOutput.clear();
    }

    private void showError(String message) {
        apiOutput.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        apiOutput.setText("⚠ " + message);
    }

    private String formatResponse(String data) {
        return "🌍 Airport Info:\n\n" + data;
    }
}
