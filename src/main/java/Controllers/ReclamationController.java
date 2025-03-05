package Controllers;

import Entities.Reclamation;
import Services.ReclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ReclamationController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private GridPane reclamationContainer;
    @FXML
    private ListView<Reclamation> reclamationListView;

    @FXML
    private Button excel;

    @FXML
    private TextField searchField;

    @FXML
    private Button sortDate;

    @FXML
    private Button addButton;

    private ReclamationService reclamationService = new ReclamationService();
    private ObservableList<Reclamation> reclamationsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reclamationService = new ReclamationService();
        try {
            loadReclamations();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayReclamations() throws SQLException {
       /*
        List<Reclamation> reclamations = reclamationService.afficher(); // Fetch reclamations
        reclamationListView.getItems().addAll(reclamations);

        // Custom cell factory to align data with headers
        reclamationListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reclamation reclamation, boolean empty) {
                super.updateItem(reclamation, empty);

                if (empty || reclamation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Format Date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = dateFormat.format(reclamation.getDateReclamation());

                    // Create HBox for aligned display
                    HBox row = new HBox(10);
                    row.setStyle("-fx-padding: 5; -fx-background-color: white;");

                    // Create Text elements for each column
                    Text idText = new Text(String.valueOf(reclamation.getId()));
                    idText.setWrappingWidth(100);

                    Text userText = new Text(String.valueOf(reclamation.getIdUser()));
                    userText.setWrappingWidth(111);

                    Text dateText = new Text(formattedDate);
                    dateText.setWrappingWidth(120);

                    Text typeText = new Text(reclamation.getType());
                    typeText.setWrappingWidth(120);
                    HBox.setMargin(typeText, new Insets(0, 0, 0, 150)); // Add left margin 150

                    Text messageText = new Text(reclamation.getMessage());
                    messageText.setWrappingWidth(120);
                    HBox.setMargin(messageText, new Insets(0, 0, 0, 100)); // Add left margin 100

                    // Add elements to row
                    row.getChildren().addAll(idText, userText, dateText, typeText, messageText);
                    setGraphic(row);
                }
            }
        });

        */
    }



    @FXML
    void handleLogout(ActionEvent event) {

    }

    @FXML
    void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().trim(); // Get the search text

        if (searchText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champ de recherche vide", "Veuillez entrer du texte pour la recherche.");
            return;
        }

        try {
            // Call the service to perform the search on multiple fields
            List<Reclamation> filteredList = reclamationService.searchByFields(searchText);
            updateReclamationCards(filteredList); // Update the UI with the filtered list

        } catch (SQLException e) {
            e.printStackTrace(); // Log the error
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la recherche.\n" + e.getMessage());
        }
        System.out.println("Search input: " + searchText);
    }



    private void updateReclamationCards(List<Reclamation> reclamations) {
        reclamationContainer.getChildren().clear();

        if (reclamations.isEmpty()) {
            Label noResults = new Label("Aucune réclamation trouvée.");
            reclamationContainer.add(noResults, 0, 0);
            return;
        }

        int column = 0;
        int row = 1;
        try {
            for (Reclamation reclamation : reclamations) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/reclamationCard.fxml"));
                Parent card = fxmlLoader.load();
                CardReclamationController cardController = fxmlLoader.getController();
                cardController.setData(reclamation, this);
                reclamationContainer.add(card, column, row);
                column++;
                if (column == 7) { column = 0; row++; }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    void handleAddReclamation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addReclamation.fxml"));
            Parent root = loader.load();

            AddReclamationController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Ajouter Réclamation");
            stage.setScene(new Scene(root));

            // Wait until the Add Reclamation window is closed
            stage.showAndWait();

            // After closing the window, refresh the reclamations
            loadReclamations();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*public void loadReclamations() throws SQLException {
        List<Reclamation> reclamations = reclamationService.afficher();
        int column = 0;
        int row = 1; // Start adding from row 1 to avoid header conflict

        try {
            reclamationContainer.getChildren().clear(); // Clear existing entries

            // Set spacing
            reclamationContainer.setHgap(10); // Horizontal spacing
            reclamationContainer.setVgap(10); // Vertical spacing
            reclamationContainer.setPadding(new Insets(10)); // Padding around the grid

            for (Reclamation reclamation : reclamations) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/reclamationCard.fxml"));
                Parent card = fxmlLoader.load();

                // Set data in CardReclamationController
                CardReclamationController cardController = fxmlLoader.getController();
                cardController.setData(reclamation, this);

                reclamationContainer.add(card, column, row);
                column++;

                // Adjust column/row for layout
                if (column == 7) { // Adjust this based on the number of cards per row
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */

    public void loadReclamations() throws SQLException {
        int currentUserId = 2; // 🔥 Hardcoded user ID for testing

        List<Reclamation> reclamations = reclamationService.afficherParUtilisateur(currentUserId);
        int column = 0;
        int row = 1;

        try {
            reclamationContainer.getChildren().clear();

            reclamationContainer.setHgap(10);
            reclamationContainer.setVgap(10);
            reclamationContainer.setPadding(new Insets(10));

            for (Reclamation reclamation : reclamations) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/reclamationCard.fxml"));
                Parent card = fxmlLoader.load();

                CardReclamationController cardController = fxmlLoader.getController();
                cardController.setData(reclamation, this);

                reclamationContainer.add(card, column, row);
                column++;
                if (column == 7) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handleExcel(ActionEvent event) {
        exportToExcel();
    }

    private void exportToExcel() {
        // 🔹 File Chooser Dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setTitle("Save Excel File");
        File file = fileChooser.showSaveDialog(null);

        if (file == null) {
            return; // ⛔ User canceled the save dialog
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reclamations");

            // 🔹 Header Row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "User ID", "Date", "Type", "Message"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // 🔹 Fetch Data from Database
            List<Reclamation> reclamations = getReclamations(); // 🔥 Fetch the reclamation list

            int rowNum = 1;
            for (Reclamation rec : reclamations) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rec.getId());
                row.createCell(1).setCellValue(rec.getIdUser());
                row.createCell(2).setCellValue(rec.getDateReclamation().toString());
                row.createCell(3).setCellValue(rec.getType());
                row.createCell(4).setCellValue(rec.getMessage());
            }

            // 🔹 Auto-size Columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 🔹 Save the File
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

            // 🔹 Show Success Alert (Green Alert)
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le fichier Excel a été exporté avec succès!");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'exportation:\n" + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Reclamation> getReclamations() throws SQLException {
        // 🔥 Replace with your actual data fetching logic
        ReclamationService service = new ReclamationService();
        return service.afficher(); // Assume this returns a List<Reclamation>
    }


    @FXML
    void sortByDate(ActionEvent event) {
        try {
            List<Reclamation> reclamations = reclamationService.afficher(); // Fetch all reclamations

            // 🔥 Sort by date (latest first)
            reclamations.sort((r1, r2) -> r2.getDateReclamation().compareTo(r1.getDateReclamation()));

            // Refresh UI with sorted list
            updateReclamationCards(reclamations);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de trier les réclamations.\n" + e.getMessage());
        }
    }





}