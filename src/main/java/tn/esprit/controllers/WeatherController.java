package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherController {

    @FXML
    private TextField cityTextField;

    @FXML
    private Label weatherLabel;

    private final String API_KEY = "ab0b97c2c0f270f0a5918714e1c14064"; // Replace with your OpenWeatherMap API key

    public void showWeather(ActionEvent actionEvent) {
        String cityName = cityTextField.getText().trim();
        if (cityName.isEmpty()) {
            showAlert("Veuillez entrer le nom d'une ville.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Step 1: Fetch latitude and longitude for the city using OpenWeatherMap Geocoding API
            String cityApiUrl = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s",
                    cityName, API_KEY);
            JSONArray cityResponse = fetchApiDataArray(cityApiUrl);
            if (cityResponse == null || cityResponse.isEmpty()) {
                showAlert("Ville introuvable.", Alert.AlertType.ERROR);
                return;
            }

            JSONObject cityData = cityResponse.getJSONObject(0);
            double latitude = cityData.getDouble("lat");
            double longitude = cityData.getDouble("lon");

            // Step 2: Fetch weather data using latitude and longitude with OpenWeatherMap API
            String weatherApiUrl = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%.4f&lon=%.4f&appid=%s&units=metric",
                    latitude, longitude, API_KEY);
            JSONObject weatherResponse = fetchApiData(weatherApiUrl);
            if (weatherResponse == null) {
                showAlert("Les données météorologiques ne sont pas disponibles.", Alert.AlertType.ERROR);
                return;
            }

            // Step 3: Parse weather data
            JSONObject main = weatherResponse.getJSONObject("main");
            double temperature = main.getDouble("temp");
            double humidity = main.getDouble("humidity");

            JSONArray weatherArray = weatherResponse.getJSONArray("weather");
            JSONObject weather = weatherArray.getJSONObject(0);
            String description = weather.getString("description");

            // Step 4: Display weather data
            weatherLabel.setText(String.format("Ville: %s\nTempérature: %.2f°C\nHumidité: %.2f%%\nConditions: %s",
                    cityName, temperature, humidity, description));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de la récupération des données météorologiques.", Alert.AlertType.ERROR);
        }
    }

    private JSONObject fetchApiData(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Check HTTP status code
            int status = conn.getResponseCode();
            if (status != 200) {
                System.err.println("API Error: HTTP " + status + " for URL: " + apiUrl);
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // Print the raw response for debugging
            System.out.println("API Response: " + content.toString());

            return new JSONObject(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONArray fetchApiDataArray(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Check HTTP status code
            int status = conn.getResponseCode();
            if (status != 200) {
                System.err.println("API Error: HTTP " + status + " for URL: " + apiUrl);
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // Print the raw response for debugging
            System.out.println("API Response: " + content.toString());

            return new JSONArray(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}