package tn.esprit.monji.controller;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class NinjasAPI {
    private static final String API_KEY = "XV0dPbUC59pi4KpJ450New==ILzTWWsCUOXK3VwI"; // Replace with your API key
    private static final String BASE_URL = "https://api.api-ninjas.com/v1/airports?name=";

    public static String getAirportData(String airportName) {
        try {
            String url = BASE_URL + airportName.replace(" ", "%20"); // Handle spaces
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-Api-Key", API_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body(); // Returns JSON response
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching airport data";
        }
    }

    public static void main(String[] args) {
        System.out.println(getAirportData("Paris"));
    }
}

