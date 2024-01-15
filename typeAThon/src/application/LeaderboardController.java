package application;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;


public class LeaderboardController {

    @FXML
    private Label leaderboardTitleLabel;

    @FXML
    private VBox leaderboardContainer;
    
    @FXML
    private ListView<String> leaderboardListView;

    private final Map<String, PriorityQueue<Double>> userScoresMap = new HashMap<>();
    
    private Main mainApp;

    private GameModeController gameModeController;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    @FXML
    private void initialize() {
        loadUserScores();
        updateLeaderboard();
        
    }
    
   
    @FXML
    private void goToGameMode() {
        // Create a dummy PlayerProfile or obtain it based on your requirements
        PlayerProfile dummyPlayer = new PlayerProfile("dummy", "password");
        mainApp.showGameModeScene(dummyPlayer);
    }
    
  
    private void loadUserScores() {
        try (BufferedReader br = new BufferedReader(new FileReader("userscore.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");

                if (parts.length >= 3) {
                    String username = parts[0].trim();
                    double wpm = extractNumericValue(parts[1]);

                    userScoresMap.putIfAbsent(username, new PriorityQueue<>());
                    userScoresMap.get(username).add(wpm);

                    if (userScoresMap.get(username).size() > 10) {
                        userScoresMap.get(username).poll(); // Keep only last 10 games
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void updateLeaderboard() {
        leaderboardListView.getItems().clear(); // Clear existing items

        // Sort entries based on average WPM in descending order
        List<Map.Entry<String, PriorityQueue<Double>>> sortedEntries = userScoresMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Double.compare(calculateAverage(entry2.getValue()), calculateAverage(entry1.getValue())))
                .collect(Collectors.toList());

        int rank = 1;

        // Add a custom cell factory to handle hyperlink action
        leaderboardListView.setCellFactory(param -> new ListCell<>() {
            private final Hyperlink hyperlink = new Hyperlink();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    hyperlink.setText(item);
                    setGraphic(hyperlink);

                    // Set the action only once
                    hyperlink.setOnAction(event -> {
                        String username = item.split(" ")[3];
                        System.out.println("Clicked on user: " + username);
                        redirectToUserProfile(username);
                    });
                }
            }
        });

        for (Map.Entry<String, PriorityQueue<Double>> entry : sortedEntries) {
            String username = entry.getKey();
            double avgWpm = calculateAverage(entry.getValue());

            String leaderboardEntry = "RANK " + rank + "\t User: " + username + " scores: " + String.format("%.2f", avgWpm);
            leaderboardListView.getItems().add(leaderboardEntry);

            rank++;
        }
    }


    private void redirectToUserProfile(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
            Parent root = loader.load();

            PlayerProfile player = loader.getController();
            player.setUserData(username, ""); // Assuming you don't need the password for the profile

            mainApp.showPlayerProfileScene(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private double calculateAverage(PriorityQueue<Double> scores) {
        return scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private double extractNumericValue(String input) {
        try {
            return Double.parseDouble(input.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
 
    
}