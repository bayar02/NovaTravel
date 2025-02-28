package tn.esprit.test;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


import java.io.IOException;


public class MainFX extends Application {
        @Override
        public void start(Stage primaryStage) {
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/AddPlanning.fxml"));
            try {
                Parent root =loader.load();
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Gestion events ");
                primaryStage.show();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }


        }
    }


