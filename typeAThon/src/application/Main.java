package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        showStartScene();
    }

    public void showStartScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
              Parent root = loader.load();

              StartScreenController startScreenController = loader.getController();
              startScreenController.setMainApp(this);

              Scene scene = new Scene(root);
              primaryStage.setScene(scene);
              primaryStage.setTitle("Start");
              primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            loginController.setMainApp(this);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGameModeScene(PlayerProfile player) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameMode.fxml"));
            Parent root = loader.load();

            GameModeController gameModeController = loader.getController();
            gameModeController.setMainApp(this);
            gameModeController.setCurrentPlayer(player);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Game Modes");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showTypingGameScene(PlayerProfile player, boolean suddenDeathMode, int timerDuration, int text) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TypingGame.fxml"));
            Parent root = loader.load();

            TypingGameController typingGameController = loader.getController();
            typingGameController.setMainApp(this);
            typingGameController.setSuddenDeathMode(suddenDeathMode);
            typingGameController.setCurrentPlayer(player);
            typingGameController.setTimerDuration(timerDuration);
            typingGameController.setTextType(text);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Typing Game");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   
    public void showPlayerProfileScene(PlayerProfile player) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
            Parent root = loader.load();

            PlayerProfile profileController = loader.getController();
            profileController.setMainApp(this); // Set the reference to Main
            profileController.setUserData(player.getUsername(), player.getPassword());

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Player Profile");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    

    public void showLeaderboardScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Leaderboard.fxml"));
            Parent root = loader.load();

            LeaderboardController leaderboardController = loader.getController();
            leaderboardController.setMainApp(this);  // Set the reference to Main

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Leaderboard");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}