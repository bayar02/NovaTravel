package tn.esprit.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.controllers.HomeController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    private static MyDataBase instance;
    private Connection connection;
    
    private static final String URL = "jdbc:mysql://localhost:3306/nova_travel";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private MyDataBase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to Database!");
        } catch (SQLException e) {
            System.err.println("Error connecting to Database: " + e.getMessage());
        }
    }
    
    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String email) {
        Parent root = null;
        
        try {
            FXMLLoader loader = new FXMLLoader(MyDataBase.class.getResource("/" + fxmlFile));
            root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
