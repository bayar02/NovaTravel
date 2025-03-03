package tn.esprit.controllers;

import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import tn.esprit.services.ServiceUser;
import tn.esprit.utils.GoogleAuthService;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;
import tn.esprit.services.ValidationService;
import tn.esprit.utils.EncryptionUtil;
import tn.esprit.utils.SessionManager;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;

public class SigninController {

    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLabel;
    @FXML
    private CheckBox rememberMe;
    @FXML
    private WebView webView;

    private final UserService userService;
    private final Preferences preferences;
    private final GoogleAuthService googleAuthService;
    private final ServiceUser serviceUser;
    public SigninController() {
        userService = new UserService();
        preferences = Preferences.userNodeForPackage(SigninController.class);
        googleAuthService = new GoogleAuthService();
        serviceUser = new ServiceUser();
    }

    @FXML
    public void initialize() {
        loadSavedCredentials();
    }

    private void loadSavedCredentials() {
        String savedEmail = preferences.get("email", "");
        String savedPassword = preferences.get("password", "");
        boolean isRemembered = preferences.getBoolean("rememberMe", false);

        if (isRemembered && !savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            email.setText(savedEmail);
            password.setText(EncryptionUtil.decrypt(savedPassword));
            rememberMe.setSelected(true);
        }
    }

    @FXML
    public void login(ActionEvent event) {
        String userEmail = email.getText().trim();
        String userPassword = password.getText().trim();

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        if (!ValidationService.isValidEmail(userEmail)) {
            showError("Format d'email invalide");
            return;
        }

        try {
            User user = userService.authenticate(userEmail, userPassword);
            if (user != null) {
                handleSuccessfulLogin(user, userPassword);
            } else {
                showError("Email ou mot de passe incorrect");
            }
        } catch (SQLException e) {
            showError("Erreur de connexion à la base de données");
            e.printStackTrace();
        }
    }

    private void handleSuccessfulLogin(User user, String userPassword) {
        if (rememberMe.isSelected()) {
            preferences.put("email", user.getMail());
            preferences.put("password", EncryptionUtil.encrypt(userPassword));
            preferences.putBoolean("rememberMe", true);
        } else {
            preferences.remove("email");
            preferences.remove("password");
            preferences.putBoolean("rememberMe", false);
        }

        SessionManager.getInstance().setCurrentUser(user);
        navigateToDashboard(user);
    }

    private void navigateToDashboard(User user) {
        String targetFxml = switch (user.getRole()) {
            case ADMIN -> "/fxml/acceuil_admin.fxml";
            case REGULAR_USER -> "/fxml/user_dashboard.fxml";
            default -> "/fxml/home.fxml";
        };

        navigateTo(targetFxml);
    }

    @FXML
    public void goToSignup(ActionEvent event) {
        navigateTo("/fxml/signup.fxml");
    }

    @FXML
    public void openForgotPassword(ActionEvent event) {
        navigateTo("/fxml/ForgotPassword.fxml");
    }

    private void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) email.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors de la navigation");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    // Google Authentication
    @FXML
    public void loginWithGoogle() {
        try {
            String authUrl = googleAuthService.getAuthorizationUrl();
            openAuthWindow(authUrl, "Connexion avec Google");
        } catch (Exception e) {
            showError("Erreur lors de la connexion avec Google");
            e.printStackTrace();
        }
    }
    public void loginWithFacebook() {
    }

    private void openAuthWindow(String url, String title) {
        try {
            WebView authWebView = new WebView();
            authWebView.getEngine().load(url);

            // Listen for URL changes in the WebView
            authWebView.getEngine().locationProperty().addListener((observable, oldValue, newValue) -> {
                // Replace "YOUR_REDIRECT_URI" with your actual redirect URI
                if(newValue.startsWith("http://localhost:8080/callback")) {
                    // Extract the 'code' parameter from the URL
                    String authCode = extractCodeFromUrl(newValue);
                    // Close the authentication window
                    Stage stage = (Stage) authWebView.getScene().getWindow();
                    stage.close();
                    // Call the callback method with the auth code
                    handleGoogleCallback(authCode);
                }
            });

            Stage authStage = new Stage();
            authStage.setScene(new Scene(authWebView, 600, 500));
            authStage.setTitle(title);
            authStage.show();
        } catch (Exception e) {
            showError("Erreur d'affichage de la fenêtre d'authentification");
            e.printStackTrace();
        }
    }

    // Helper method to extract the "code" parameter from the URL

    private String extractCodeFromUrl(String url) {
        try {
            System.out.println("URL de redirection : " + url); // Log pour vérifier l'URL
            java.net.URL urlObj = new java.net.URL(url);
            String query = urlObj.getQuery();
            if (query != null) {
                String[] params = query.split("&");
                for (String param : params) {
                    if (param.startsWith("code=")) {
                        String authCode = param.substring("code=".length());
                        authCode = URLDecoder.decode(authCode, StandardCharsets.UTF_8); // Décoder le code
                        System.out.println("Code d'autorisation extrait et décodé : " + authCode); // Log pour vérifier le code
                        return authCode;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void handleGoogleCallback(String authCode) {
        try {
            // Vérifier que le code d'autorisation est valide
            if (authCode == null || authCode.isEmpty()) {
                showError("Erreur : Code d'autorisation invalide.");
                return;
            }

            // Décoder le code d'autorisation
            authCode = URLDecoder.decode(authCode, StandardCharsets.UTF_8);

            // Échanger le code d'autorisation contre un jeton d'accès
            var accessToken = googleAuthService.getAccessToken(authCode);

            // Récupérer les informations de l'utilisateur
            String userInfoJson = googleAuthService.getUserProfile(accessToken);
            System.out.println("Réponse JSON de Google : " + userInfoJson); // Log pour inspecter la réponse JSON

            JsonObject userJson = JsonParser.parseString(userInfoJson).getAsJsonObject();

            // Vérifier que les champs requis existent
            if (!userJson.has("given_name") || !userJson.has("family_name")) {
                showError("Erreur : Réponse JSON invalide. Champs manquants.");
                return;
            }

            // Extraire les données de la réponse JSON
            String givenName = userJson.get("given_name").getAsString(); // Prénom
            String familyName = userJson.get("family_name").getAsString(); // Nom
            String email = userJson.has("email") ? userJson.get("email").getAsString() : givenName.toLowerCase() + "." + familyName.toLowerCase() + "@google.com"; // E-mail
            String picture = userJson.has("picture") ? userJson.get("picture").getAsString() : null; // Photo de profil

            // Vérifier si les données sont valides
            if (givenName == null || givenName.isEmpty() || familyName == null || familyName.isEmpty()) {
                showError("Erreur : Prénom ou nom manquant.");
                return;
            }

            if (email == null || email.isEmpty() || !ValidationService.isValidEmail(email)) {
                showError("Erreur : E-mail invalide.");
                return;
            }

            // Vérifier si l'utilisateur existe déjà
            User user = serviceUser.getUserByEmail(email);
            if (user == null) {
                // Créer un nouvel utilisateur avec les données de Google
                user = new User();
                user.setNom(familyName); // Nom de famille
                user.setPrenom(givenName); // Prénom
                user.setMail(email); // E-mail
                user.setCin("N/A"); // Valeur par défaut pour le CIN
                user.setTel("N/A"); // Valeur par défaut pour le téléphone
                user.setPassword("N/A"); // Valeur par défaut pour le mot de passe
                user.setRole(User.Role.REGULAR_USER); // Rôle par défaut

                // Ajouter l'utilisateur à la base de données
                try {
                    serviceUser.ajouter(user);
                    System.out.println("Utilisateur ajouté avec succès : " + user.getMail());
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
                    e.printStackTrace();
                    showError("Erreur : Données utilisateur invalides.");
                    return;
                }
            }

            // Naviguer vers le tableau de bord
            SessionManager.getInstance().setCurrentUser(user);
            navigateToDashboard(user);
        } catch (IOException | ExecutionException | InterruptedException e) {
            showError("Erreur lors de la connexion avec Google : " + e.getMessage());
            e.printStackTrace();
        } catch (OAuth2AccessTokenErrorResponse e) {
            showError("Erreur OAuth2 : " + e.getErrorDescription());
            e.printStackTrace();
        } catch (NullPointerException e) {
            showError("Erreur : Réponse JSON invalide. Champs manquants.");
            e.printStackTrace();
        }
    }

}
