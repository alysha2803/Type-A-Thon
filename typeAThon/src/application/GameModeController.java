package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class GameModeController {

    private Main mainApp;
    private PlayerProfile currentPlayer;
    
    @FXML
    private Button normalButton;

    @FXML
    private Button playerProfileButton;

    @FXML
    private Button randomWordsButton;

    @FXML
    private Button randomQuotesButton;

    @FXML
    private Button suddenDeathButton;
    
    @FXML
    private Button correctionButton;

    @FXML
    private Button profileButton;
    
    @FXML
    private Button leaderboardButton;

    @FXML
    private ComboBox<Integer> timerComboBox; // Added ComboBox for timer selection
    
    

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void setCurrentPlayer(PlayerProfile currentPlayer) {
        this.currentPlayer = currentPlayer;
        button();
    }

    @FXML
    private void button() {
    	correctionButtonVisibality();
        normalButton.setOnAction(event -> showTypingGameScene(false,4));
        randomWordsButton.setOnAction(event -> showTypingGameScene(false,0));
        randomQuotesButton.setOnAction(event -> showTypingGameScene(false,1)); // Implement logic for this button if needed
        suddenDeathButton.setOnAction(event -> showTypingGameScene(true,3));
        correctionButton.setOnAction(event -> showTypingGameScene(false,2));
        profileButton.setOnAction(event -> showPlayerProfileScene());
        leaderboardButton.setOnAction(event -> showLeaderboardScene());

        // Populate the ComboBox with timer options
        timerComboBox.getItems().addAll(15, 30, 45, 60);
        timerComboBox.setValue(30); // Set default value
    }

    private void correctionButtonVisibality() {
    	int wordLimit=0;
    	try {
   		 Scanner in = new Scanner(new FileInputStream("mispelled.txt"));
   		 while (in.hasNextLine()) {
		    	String parts[]=(in.nextLine()).split(" ");
		    	String username=currentPlayer.getUsername();
		    	if(parts.length>=2){
		    		if (username.equals(parts[0]))
		    			wordLimit++;}
		    
   		 }in.close();
    }catch(IOException e) {
    	System.out.println("Problem with file output");
    	}
    	if (wordLimit>10){
    		correctionButton.setVisible(true);
    	}else {
    		correctionButton.setVisible(false);
    	}
    	
    }
    
	private void showTypingGameScene(boolean suddenDeathMode, int text) {
        int selectedTimer = timerComboBox.getValue(); // Retrieve the selected timer value
        mainApp.showTypingGameScene(currentPlayer, suddenDeathMode, selectedTimer, text);
    }
    
    public void showPlayerProfileScene() {
        mainApp.showPlayerProfileScene(currentPlayer);
    }
    
    private void showLeaderboardScene() {
   	 mainApp.showLeaderboardScene();
	}




}