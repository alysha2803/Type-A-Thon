package application;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.text.DecimalFormat;

public class PlayerProfile {

    @FXML
    private ComboBox<String> userComboBox;
    @FXML
    private Label allTimeWpmLabel;
    @FXML
    private Label allTimeAccuracyLabel;
    @FXML
    private Label last10GamesWpmLabel;
    @FXML
    private Label last10GamesAccuracyLabel;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label mostMisspelledWordsLabel;
    
    
    private String username;
    private String password;
    private Queue<Double> last10GamesWPMQueue = new LinkedList<>();
    private Queue<Double> last10GamesAccuracyQueue = new LinkedList<>();
    private double allTimeWpmSum = 0;
    private double allTimeAccuracySum = 0;
    private Main mainApp;

    public PlayerProfile() {
    }

    public PlayerProfile(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }


    public void setUserData(String username, String password) {
        this.username = username;
      //  this.password = password;
        loadUserScores(username); // Call loadUserScores after setting username
        updateStatsLabels();
        loadMostMisspelledWords(username);

    }
    public void initialize() {
        loadUsernames();
    }

    private void loadUsernames() {
        try (BufferedReader br = new BufferedReader(new FileReader("userdata.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    userComboBox.getItems().add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUserSelection() {
        // Handle the event when a user is selected from the ComboBox
        String selectedUser = userComboBox.getValue();
        if (selectedUser != null) {
            // Load scores for the selected user
            username = selectedUser;
            loadUserScores(selectedUser);
            loadMostMisspelledWords(selectedUser);
            updateStatsLabels();
        }
    }
    
 private void loadUserScores(String username) {
     try (BufferedReader br = new BufferedReader(new FileReader("userscore.txt"))) {
         String line;
         int gamesCount = 0;
         double allTimeWpmSum = 0;
         double allTimeAccuracySum = 0;

         while ((line = br.readLine()) != null) {
             String[] parts = line.split("\t");

             if (parts.length >= 3 && parts[0].trim().equals(username.trim())) {
                 // Extract numeric part of WPM and accuracy
                 double wpm = extractNumericValue(parts[1]);
                 double accuracy = extractNumericValue(parts[2]);

                 // Update all-time stats
                 allTimeWpmSum += wpm;
                 allTimeAccuracySum += accuracy;

                 gamesCount++;
             }
         }

         if (gamesCount > 0) {
             // Calculate averages for all-time
             double allTimeAvgWPM = allTimeWpmSum / gamesCount;
             double allTimeAvgAccuracy = allTimeAccuracySum / gamesCount;

             // Format averages to 2 decimal points
             DecimalFormat decimalFormat = new DecimalFormat("#.##");
             allTimeAvgWPM = Double.parseDouble(decimalFormat.format(allTimeAvgWPM));
             allTimeAvgAccuracy = Double.parseDouble(decimalFormat.format(allTimeAvgAccuracy));

             // Calculate averages for last 10 games
             double last10GamesAvgWPM = allTimeAvgWPM; // Set default value
             double last10GamesAvgAccuracy = allTimeAvgAccuracy; // Set default value

             // Update labels with formatted averages
             allTimeWpmLabel.setText("All-Time Avg WPM: " + allTimeAvgWPM);
             allTimeAccuracyLabel.setText("All-Time Avg Accuracy: " + allTimeAvgAccuracy);
             last10GamesWpmLabel.setText("Last 10 Games Avg WPM: " + last10GamesAvgWPM);
             last10GamesAccuracyLabel.setText("Last 10 Games Avg Accuracy: " + last10GamesAvgAccuracy);
         } else {
             // No games found, set default values
             allTimeWpmLabel.setText("All-Time Avg WPM: 0");
             allTimeAccuracyLabel.setText("All-Time Avg Accuracy: 0");
             last10GamesWpmLabel.setText("Last 10 Games Avg WPM: 0");
             last10GamesAccuracyLabel.setText("Last 10 Games Avg Accuracy: 0");
         }
     } catch (IOException | NumberFormatException e) {
         e.printStackTrace();
     }
 }
 
 
 private void loadMostMisspelledWords(String username) {
	    try (BufferedReader br = new BufferedReader(new FileReader("mispelled.txt"))) {
	        String line;
	        int count = 0;
	        StringBuilder mostMisspelledWords = new StringBuilder();

	        while ((line = br.readLine()) != null) {
	            String[] parts = line.split(" ");
	            if (parts.length >= 2 && parts[0].trim().equals(username.trim())) {
	                // Extract the misspelled word
	                String misspelledWord = parts[1].trim();

	                // Append the misspelled word to the StringBuilder with a comma for separation
	                mostMisspelledWords.append(misspelledWord).append(", ");

	                count++;
	                if (count >= 10) {
	                    break; // Stop after finding 10 most misspelled words
	                }
	            }
	        }

	        // Set the text of the label with the most misspelled words, removing the trailing comma
	        mostMisspelledWordsLabel.setText("Most Misspelled Words : " + " " + mostMisspelledWords.toString().replaceAll(", $", ""));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


    private double extractNumericValue(String input) {
        // Extract numeric part of the input string
        try {
            return Double.parseDouble(input.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            // Handle parsing errors or return a default value
            return 0.0;
        }
    }

    private void updateStatsLabels() {
        welcomeLabel.setText("Profile : " + username);
    }
    
    @FXML
    private void goToGameMode() {

        mainApp.showGameModeScene(this);
    }


}