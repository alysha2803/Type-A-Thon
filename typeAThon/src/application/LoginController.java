package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Main mainApp;

    private List<PlayerProfile> playerProfiles;

    private final String USER_DATA_FILE = "userdata.txt";

    public LoginController() {
        // Initialize an empty list of player profiles
        playerProfiles = new ArrayList<>();
        loadPlayerProfiles();
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        PlayerProfile loggedInPlayer = findPlayer(username, password);
        
        if (loggedInPlayer != null) {

            
            mainApp.showGameModeScene(loggedInPlayer); // Show the game mode scene in Main
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }


    @FXML
    private void register() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if username or password is empty
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Registration Failed", "Username or password cannot be empty.");
            return;
        }

        if (isUsernameTaken(username)) {
            showAlert("Registration Failed", "Username is already taken. Please choose a different username.");
        } else {
            // Create a new player profile
            PlayerProfile newPlayer = new PlayerProfile(username, password);
            playerProfiles.add(newPlayer);
            savePlayerProfiles(); // Save the updated list of profiles to the file

            //String gameWelcomeMessage = getGameWelcomeMessage(newPlayer);
          //  showAlert("Registration Successful", "Welcome to the Game, " + newPlayer.getUsername() + "!\n" + gameWelcomeMessage);
        }
    }

    private PlayerProfile findPlayer(String username, String password) {
        for (PlayerProfile profile : playerProfiles) {
            if (profile.getUsername().equals(username) && profile.getPassword().equals(password)) {
                return profile;
            }
        }
        return null;
    }

    private boolean isUsernameTaken(String username) {
        for (PlayerProfile profile : playerProfiles) {
            if (profile.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

//    private String getGameWelcomeMessage(PlayerProfile player) {
//        return "Your current score: " + player.getScore() + "\nGet ready for an epic adventure!";
//    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadPlayerProfiles() {
        try (Scanner scanner = new Scanner(new File(USER_DATA_FILE))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                if (data.length == 2) {
                    playerProfiles.add(new PlayerProfile(data[0], data[1]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void savePlayerProfiles() {
        try (Writer writer = new FileWriter(USER_DATA_FILE)) {
            for (PlayerProfile profile : playerProfiles) {
                writer.write(profile.getUsername() + "," + profile.getPassword() + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}