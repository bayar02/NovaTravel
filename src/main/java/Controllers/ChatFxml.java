package Controllers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ChatFxml implements Initializable {
    private static final int MAX_ALERTS = 3;
    private static final int BAN_DURATION_MINS = 5;
    private static final String[] FORBIDDEN_WORDS = {"fuck", "shutup", "damn","ferme la"};
    @FXML
    private TextArea textchat;
    @FXML
    private TextField ASK;
    private Map<String, String> responses;
    @FXML
    private Button Home;
    private int alertCount = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        responses = new HashMap<>();
        responses.put("bonjour", "Bonjour ! Comment puis-je vous aider avec votre réclamation ?");
        responses.put("comment faire une réclamation", "Pour faire une réclamation, veuillez remplir le formulaire de réclamation et l'envoyer.");
        responses.put("où suivre ma réclamation", "Vous pouvez suivre l'état de votre réclamation dans votre espace personnel.");
        responses.put("combien de temps pour une réponse", "Notre équipe traite les réclamations sous 48 heures.");
        responses.put("comment contacter le support", "Vous pouvez contacter le support via notre page de contact ou par email.");
        responses.put("puis-je modifier ma réclamation", "Oui, vous pouvez modifier votre réclamation tant qu'elle n'est pas encore traitée.");
        responses.put("ma réclamation est refusée", "Si votre réclamation est refusée, vous pouvez faire appel en nous fournissant plus de détails.");
        responses.put("mon problème est urgent", "Pour les urgences, veuillez contacter notre service client par téléphone.");
        responses.put("où voir les réponses aux réclamations", "Les réponses à vos réclamations sont disponibles dans votre espace personnel.");
        responses.put("j'ai une question", "Bien sûr ! Je suis ici pour vous aider. Quelle est votre question ?");

    }


    @FXML
    private void UserA(ActionEvent event) throws Exception {
        String input = ASK.getText();
        String response = responses.getOrDefault(input, "désolé j'ai pas la réponse ");
        for (String forbiddenWord : FORBIDDEN_WORDS) {
            if (input.contains(forbiddenWord)) {
                alertCount++;
                if (alertCount == MAX_ALERTS) {
                    // Ban the user for 5 minutes
                    ASK.setDisable(true);
                    Alert alert = new Alert(AlertType.ERROR, "Vous avez été banni ");
                    alert.showAndWait();
                    Platform.exit();
                } else {
                    Alert alert = new Alert(AlertType.WARNING, "Le message contient un gros mot. Attention!");
                    alert.showAndWait();
                }
                ASK.clear();
                return;
            }
        }
        textchat.appendText("User: " + input + "\n");
        textchat.appendText("Chatbot: " + response + "\n\n");
        ASK.clear();
    }

    @FXML
    private void OnHomeClicked(ActionEvent event) {
       /* try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BackHome.fxml"));
            Parent root = loader.load();
            Home.getScene().setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(AjouterReservationController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

}
