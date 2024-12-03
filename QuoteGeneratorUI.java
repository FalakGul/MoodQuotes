import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import okhttp3.*;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class QuoteGeneratorUI extends Application implements Serializable {

    private boolean isLoggedIn = false;
    private String loggedInUser = null; // To track the current logged-in user
    private Stage primaryStage;

    // Store user-specific favorite quotes and custom quotes
    private final Map<String, ArrayList<String>> userFavoriteQuotes = new HashMap<>();
    private final Map<String, ArrayList<String>> userCustomQuotes = new HashMap<>();
    private Text quoteText = new Text();

    private final ArrayList<String> quotesList = new ArrayList<>(Arrays.asList(
            "The only way to do great work is to love what you do. -Unknown",
            "The greatest glory in living lies not in never falling, but in rising every time we fall. -Nelson Mandela",
            "Don't watch the clock; do what it does. Keep going. -Unknown",
            "The best way to predict the future is to create it. -Unknown",
            "Your time is limited, so don't waste it living someone else's life. Don't be trapped by dogma – which is living with the results of other people's thinking. -Steve Jobs",
            "The future belongs to those who believe in the beauty of their dreams. -Eleanor Roosevelt",
            "You may say I'm a dreamer, but I'm not the only one. I hope someday you'll join us. And the world will live as one. -John Lennon",
            "Spread love everywhere you go. Let no one ever come to you without leaving happier. -Mother Teresa",
            "It is during our darkest moments that we must focus to see the light. -Aristotle",
            "Be yourself; everyone else is already taken. -Oscar Wilde",
            "You will face many defeats in life, but never let yourself be defeated. -Maya Angelou",
            "Go confidently in the direction of your dreams! Live the life you've imagined. -Henry David Thoreau"
    ));
    private final Map<String, String> users = new HashMap<>();
    private static final String USERS_FILE = "users.dat";
    private static final String FAVORITES_FILE = "favorites.dat";
    private static final String CUSTOM_QUOTES_FILE = "custom_quotes.dat";

    private ImageView backgroundImageView;

    private final Map<String, String> users = new HashMap<>();
    private static final String USERS_FILE = "users.dat";
    private static final String FAVORITES_FILE = "favorites.dat";
    private static final String CUSTOM_QUOTES_FILE = "custom_quotes.dat";

    private void registerUser(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Registration Failed", "Username and password cannot be empty.");
            return;
        }

        if (users.containsKey(username)) {
            showAlert("Registration Failed", "Username already exists.");
        } else {
            users.put(username, hashPassword(password));
            userFavoriteQuotes.put(username, new ArrayList<>());
            userCustomQuotes.put(username, new ArrayList<>());
            saveAppData();
            showAlert("Registration Successful", "You have been registered successfully. You can now log in.");
        }
    }

    private void loginUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(hashPassword(password))) {
            isLoggedIn = true;
            loggedInUser = username;

            // Initialize lists if not already present
            userFavoriteQuotes.putIfAbsent(username, new ArrayList<>());
            userCustomQuotes.putIfAbsent(username, new ArrayList<>());

            primaryStage.setScene(showQuoteScreen());
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private void styleButton(Button button, String color) {
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 15;");
        button.setMinWidth(150);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Scene createLoginScene() {
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        usernameField.setMaxWidth(150);
        passwordField.setMaxWidth(150);

        Button loginButton = new Button("Login");
        styleButton(loginButton, "#4CAF50");

        Button registerButton = new Button("Register");
        styleButton(registerButton, "#008CBA");

        usernameField.setPromptText("Enter your username");
        passwordField.setPromptText("Enter your password");


        // Handle 'Enter' key for login (when in either field)
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SHIFT) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                loginUser(username, password);
            }
        });

        passwordField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                loginUser(username, password);
            }
        });

        // Handle down arrow key to move focus to passwordField
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                passwordField.requestFocus();
            }
        });

        // Handle up arrow key to move focus to usernameField
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                usernameField.requestFocus();
            }
        });

        Image backgroundImage = new Image("pg15.gif"); // name of image
        backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(2000);
        backgroundImageView.setFitHeight(1500);
        backgroundImageView.setPreserveRatio(true);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            loginUser(username, password);
        });

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            registerUser(username, password);
        });

        VBox loginLayout = new VBox(20);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.getChildren().addAll(
                new Text("Welcome to the MoodQuotes!") {{
                    setFill(Color.NAVY);
                    setFont(Font.font("Arial", 32));
                    setFont(Font.font("Arial", FontWeight.BOLD, 26));  // Set font to bold
                    setEffect(new DropShadow(20, Color.WHITE));  // Apply a drop shadow effect
                }},
                usernameField,
                passwordField,
                loginButton,
                registerButton
        );
