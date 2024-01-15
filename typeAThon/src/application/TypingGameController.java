package application;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class TypingGameController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label tx;

    @FXML
    private TextArea typingArea;

    @FXML
    private TextFlow promptTextFlow;
    
    @FXML
    private Label timeElapsedLabel;  // Added for Stopwatch
    
    private boolean suddenDeathMode;
    private int suddenDeathScore = 0;
    private int textType;

    private List<String> words = new ArrayList<>();
    private List<String> correctionFacilityWords = new ArrayList<>();

    private String currentWord;
    private int wordLimit=0;
    private boolean gameActive = false;
    private boolean isFirstLetterTyped = false;
    private int totalTypedChars = 0;
    private int totalMistakes = 0;
    private int numOfWords;
    
    private List<String> mostMisspelledWords = new ArrayList<>();
    private final int MAX_MISSED_WORDS = 10;

    private int startTime;
    
    private boolean useCountdownTimer = true;
    
    private int timerDuration;
    
    private Timeline time;
    private Integer seconds = startTime;

    private String promptText;
    
    private String typedText;
    
    private Main mainApp;
    
    private PlayerProfile currentPlayer;
    
    private LoginController loginController;
    
    
    private long startTimeMillis;
    
    private Timeline stopwatchTimeline;

  
    public void setCurrentPlayer(PlayerProfile currentPlayer) {
    	this.currentPlayer = currentPlayer;

    	}

    	public void setTextType(int b) {
    		textType = b;
    		chooseNumberOfWords();
    	}

    	public void setSuddenDeathMode(boolean a) {
    	  suddenDeathMode = a;
    	}
    	public void setMainApp(Main mainApp) {
    	this.mainApp = mainApp;
    	}
    	
    	public void chooseNumberOfWords() {
    		if (textType==0) {
    	    Alert numberOfWords = new Alert(Alert.AlertType.INFORMATION);
    	    numberOfWords.setTitle("Alternate Gamemodes");
    	    numberOfWords.setHeaderText(null);

    	    numberOfWords.setContentText("Choose number of texts");

    	ButtonType Button10 = new ButtonType("10 Words");
    	ButtonType Button25 = new ButtonType("25 Words");
    	ButtonType Button50 = new ButtonType("50 Words");
    	ButtonType Button100 = new ButtonType("100 Words");

    	numberOfWords.getButtonTypes().setAll(Button10, Button25, Button50, Button100);

    	numberOfWords.showAndWait().ifPresent(buttonType -> {
            handleButton(buttonType);
            formingPromptText();
        });
    		}else if (textType == 1) {
		      numOfWords = 1;
		        formingPromptText();
		
    		} 
    		else if (textType == 2) {
  		      numOfWords = 150;
  		        formingPromptText();
  		
      		}else if (textType == 3) { 
    	        // Sudden Death mode, set numOfWords to 300 directly
    	        numOfWords = 300;
    	        formingPromptText(); // Proceed to form the prompt text
    	    }
      		else if (textType == 4) { 

    	        numOfWords = 500;
    	        formingPromptText(); 
    	    }

    	}
    
public void formingPromptText() {
	Random r = new Random();

 if(textType==0) {
	 try {
         Scanner in = new Scanner(new FileInputStream("common-words.txt"));
         List<String> wordsList = new ArrayList<>();

         while (in.hasNextLine()) {
             String line = in.nextLine().trim();  // Trim removes leading and trailing spaces
             if (!line.isEmpty()) {
                 wordsList.add(line);
             }
         }

         in.close();

         if (wordsList.isEmpty()) {
             System.out.println("No valid words found in the file.");
             return;  // Exit if no valid words are found
         }

         String[] words1 = new String[wordsList.size()];
         wordsList.toArray(words1);

         promptText = words1[r.nextInt(wordsList.size())];

        

         for (int i = 0; i < numOfWords - 1; i++) {
             int choose = r.nextInt(wordsList.size());
             System.out.println(words1[choose]);
             promptText += " " + words1[choose];
         }
     } catch (FileNotFoundException e) {
         System.out.println("File was not found.");
     } catch (IOException e) {
         System.out.println("Problem with file output");
     }
 }else if (textType == 1) {
     // Random quote mode with 1 line
     try {
         Scanner in = new Scanner(new FileInputStream("txt.txt"));
         List<String> quotesList = new ArrayList<>();

         while (in.hasNextLine()) {
             String line = in.nextLine().trim();
             if (!line.isEmpty()) {
                 quotesList.add(line);
             }
         }

         in.close();

         if (quotesList.isEmpty()) {
             System.out.println("No valid quotes found in the file.");
             return;
         }

         promptText = quotesList.get(r.nextInt(quotesList.size()));
         System.out.println(promptText);

         
     } catch (FileNotFoundException e) {
         System.out.println("File was not found.");
     } catch (IOException e) {
         System.out.println("Problem with file output");
     }
 } else if (textType == 2) {
     try {
         Scanner in = new Scanner(new FileInputStream("mispelled.txt"));
         while (in.hasNextLine()) {
             String line = in.nextLine();
             String[] parts = line.split(" ");
             String username = currentPlayer.getUsername();
             if (parts.length >= 2 && username.equals(parts[0])) {
                 correctionFacilityWords.add(parts[1]);
             }
         }
         in.close();

         // Randomly choose 150 misspelled words from the list
         Random random = new Random();
         int wordsToChoose = Math.min(150, correctionFacilityWords.size());
         for (int i = 0; i < wordsToChoose; i++) {
             String randomMisspelledWord = correctionFacilityWords.remove(random.nextInt(correctionFacilityWords.size()));
             promptText += " " + randomMisspelledWord;
         }
     } catch (IOException e) {
         e.printStackTrace();
     }
 }

 else if (textType == 3) {
	        // default 300 words
	        try {
	            Scanner in = new Scanner(new FileInputStream("common-words.txt"));
	            List<String> wordsList = new ArrayList<>();

	            while (in.hasNextLine()) {
	                String line = in.nextLine().trim();
	                if (!line.isEmpty()) {
	                    wordsList.add(line);
	                }
	            }

	            in.close();

	            if (wordsList.isEmpty()) {
	                System.out.println("No valid words found in the file.");
	                return;
	            }

	            String[] words1 = new String[wordsList.size()];
	            wordsList.toArray(words1);

	            promptText = words1[r.nextInt(wordsList.size())];

	            System.out.println(promptText);

	            for (int i = 0; i < 499; i++) { // 299 more words to make it 300
	                int choose = r.nextInt(wordsList.size());
	                System.out.println(words1[choose]);
	                promptText += " " + words1[choose];
	            }
	        } catch (FileNotFoundException e) {
	            System.out.println("File was not found.");
	        } catch (IOException e) {
	            System.out.println("Problem with file output");
	        }
	    }
 else if (textType == 4) {
     // default 300 words
     try {
         Scanner in = new Scanner(new FileInputStream("common-words.txt"));
         List<String> wordsList = new ArrayList<>();

         while (in.hasNextLine()) {
             String line = in.nextLine().trim();
             if (!line.isEmpty()) {
                 wordsList.add(line);
             }
         }

         in.close();

         if (wordsList.isEmpty()) {
             System.out.println("No valid words found in the file.");
             return;
         }

         String[] words1 = new String[wordsList.size()];
         wordsList.toArray(words1);

         promptText = words1[r.nextInt(wordsList.size())];

         System.out.println(promptText);

         for (int i = 0; i < 299; i++) { // 299 more words to make it 300
             int choose = r.nextInt(wordsList.size());
             System.out.println(words1[choose]);
             promptText += " " + words1[choose];
         }
     } catch (FileNotFoundException e) {
         System.out.println("File was not found.");
     } catch (IOException e) {
         System.out.println("Problem with file output");
     }
 }
 
	    displayPromptText();
	    typingArea.textProperty().addListener((observable, oldValue, newValue) -> handleTyping(newValue));
        
	    if (textType == 0 || textType == 1) {
	        // Only start the stopwatch for random word and random quote modes
	        initializeStopwatch();
	        stopwatchTimeline.setCycleCount(Animation.INDEFINITE);
	        stopwatchTimeline.play();
	    }

	}
 
private void initializeStopwatch() {
    stopStopwatch(); // Stop the existing stopwatch if running
    startTimeMillis = System.currentTimeMillis(); // Set the start time to the current time
    timeElapsedLabel.setText("Stopwatch: 0 seconds");

    stopwatchTimeline = new Timeline(
            new KeyFrame(Duration.seconds(0), event -> updateElapsedTime())
    );
}


    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    private void displayPromptText() {
        promptTextFlow.getChildren().clear(); // Clear previous content if any
        for (char c : promptText.toCharArray()) {
            Text character = new Text(String.valueOf(c));
            promptTextFlow.getChildren().add(character);
        }
    }
    
    private void handleTyping(String typedText) {
    	if (!isFirstLetterTyped&& !typedText.isEmpty()) {
            if (useCountdownTimer && (textType == 2 || textType == 3 || textType == 4)) {
                startTimer();
            } else {
            	initializeStopwatch();
            	startStopwatch();
            }
            isFirstLetterTyped = true;
        }


	 
      totalTypedChars++;
        for (int i = 0; i < promptTextFlow.getChildren().size(); i++) {
            Text character = (Text) promptTextFlow.getChildren().get(i);
            if (i < typedText.length()) {
              //System.out.println(totalTypedChars);
                if (typedText.charAt(i) == promptText.charAt(i)) {
                    character.setFill(Color.GREEN);
                    
                } else {
                	character.setFill(Color.RED);
                    int startOfWord = i;
                    int endOfWord = i;
                    while (startOfWord > 0 && promptText.charAt(startOfWord) != ' ') {
                        startOfWord--;
                    }
                    while (endOfWord < promptText.length() && promptText.charAt(endOfWord) != ' ') {
                        endOfWord++;
                    }

                    String wrongWord = promptText.substring(startOfWord, endOfWord);
                    if (!mostMisspelledWords.contains(wrongWord)) {
                        mostMisspelledWords.add(wrongWord);
                        if(!(wrongWord.equals(null)&&wrongWord.equals(" ")&&wrongWord.equals(""))){
                        String trimmedWrongWord = wrongWord.trim();
                        writeMispelledToFile(trimmedWrongWord);}
                    }
                    totalMistakes++;
                    
                    if (suddenDeathMode== true) {
                      stopTimer();
                       //endGame();
                        showResults(); }
                    
                    String word = promptText.substring(i, i + 1);
                    if (!mostMisspelledWords.contains(word)) {

                    }
                }
            } else {
                character.setFill(Color.GREY);
            }
        }
        
        
        if (typedText.equals(promptText)) {
            stopStopwatch();
            showResults();
            return;  // Add this line to exit the method after stopping the stopwatch
        }
    }
    
public void handleButton(ButtonType buttonType) {
	switch (buttonType.getText()) {
    case "10 Words":
    	numOfWords=10;
        break;
    case "25 Words":
    	numOfWords=25;
        break;
    case "50 Words":
    	numOfWords=50;
        break;
    case "100 Words":
    	numOfWords=100;
        break;
    default:
        System.out.println("Unknown button action");
        break;
}
}
public void setTimerDuration(int timerDuration) {
    this.timerDuration = timerDuration;
    this.startTime = timerDuration; // Set the timer duration
    this.seconds = timerDuration; // Update the initial seconds value
    tx.setText("Timer: " + seconds.toString());
}

private void startTimer() {
    time = new Timeline();
    KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {
        if (isFirstLetterTyped) {
            seconds--;
            tx.setText("Timer: " + seconds.toString());
        }

        double progress = (double) seconds / startTime;
        progressBar.setProgress(progress);

        if (seconds <= 0) {
            stopTimer();
            if (useCountdownTimer) {
                // If countdown timer is used, show results
                Platform.runLater(this::showResults);
            } else {
                // If stopwatch is used, stop the stopwatch and show results
                stopStopwatch();
                Platform.runLater(this::showResults);
            }
        }
    });

    time.setCycleCount(startTime);
    time.getKeyFrames().add(frame);
    time.play();
}


private void stopTimer() {
    if (time != null) {
        time.stop();
    }
}



private void startStopwatch() {
    if (stopwatchTimeline == null || stopwatchTimeline.getStatus() == Animation.Status.STOPPED) {
        if (startTimeMillis == 0) {
            startTimeMillis = System.currentTimeMillis();
            timeElapsedLabel.setText("Stopwatch: 0 seconds"); // Set initial label
        }
        stopwatchTimeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> updateElapsedTime())
        );
        stopwatchTimeline.setCycleCount(Animation.INDEFINITE);
        stopwatchTimeline.play();
    }
}



private void stopStopwatch() {

    timeElapsedLabel.setText("");
    if (stopwatchTimeline != null) {
        stopwatchTimeline.stop();
    }
}

private void updateElapsedTime() {
    Platform.runLater(() -> {
        long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        long elapsedSeconds = elapsedTimeMillis / 1000;
        timeElapsedLabel.setText("Stopwatch: " + elapsedSeconds + " seconds");
    });
}


    private void showResults() {
        double wpm = 0;
        double accuracy =0;
        double suddenDeathWPM = 0;

        if (suddenDeathMode) {
            suddenDeathWPM = (totalTypedChars / 5.0) / ((double) (startTime - seconds) / 60.0);
            wpm = suddenDeathWPM; // Assign the sudden death WPM for display
        } else {
        	 stopStopwatch(); // Stop the timer
            accuracy = calculateAccuracy();
            wpm = calculateWPM();
        }
        
        // Additional code to write results to a text file
        writeUserResultsToFile(wpm, accuracy);

        DecimalFormat df = new DecimalFormat("#.##");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Times Up!");
        alert.setHeaderText(null);
        String resultText;

        if (suddenDeathMode) {
            resultText = "WPM: " + df.format(suddenDeathWPM) +
                         "\nYou ended the game in " + (startTime - seconds) + " seconds";
        } else {
            accuracy = calculateAccuracy();
            wpm = calculateWPM();
            resultText = "WPM: " + df.format(wpm) + "\nAccuracy: " + df.format(accuracy) + "%";
        }

        alert.setContentText(resultText);
        ButtonType profileButtonType = new ButtonType("Profile");
        ButtonType leaderboardButtonType = new ButtonType("LeaderBoard");
        ButtonType restartButtonType = new ButtonType("Restart");
        ButtonType closeButtonType = new ButtonType("Close");
        ButtonType otherModesButtonType = new ButtonType("Other Modes");
        alert.getButtonTypes().setAll(leaderboardButtonType,profileButtonType,restartButtonType, closeButtonType, otherModesButtonType);

        alert.showAndWait().ifPresent(buttonType -> {
            Platform.runLater(() -> {
            	 stopStopwatch();
                handleButtonAction(buttonType);
            });
        });
    }
    
    private void writeUserResultsToFile(double wpm, double accuracy) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("userscore.txt", true))) {
            // Append user results to the file
            String resultLine = currentPlayer.getUsername() + "\tWPM: " + wpm + "\tAccuracy: " + accuracy + "%";
            writer.write(resultLine);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void writeMispelledToFile(String wrongWord) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("mispelled.txt", true))) {
            // Append user results to the file
        	if(!(wrongWord.equals(null)&&wrongWord.equals(" ")&&wrongWord.equals(""))) {
            String resultLine = currentPlayer.getUsername() + " " +wrongWord;
            writer.write(resultLine);
            writer.newLine();}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleButtonAction(ButtonType buttonType) {
        if (buttonType == null) {
            return; // Handle unexpected cases or null actions
        }

        switch (buttonType.getText()) {
            case "Restart":
                restartGame();
                break;
            case "Close":
                Platform.exit();
                break;
            case "Other Modes":
                if (mainApp != null) {
                    mainApp.showGameModeScene(currentPlayer);
                } else {
                    System.err.println("Error: Main is null");
                }
                break;
            case "Profile":
                if (mainApp != null) {
                    mainApp.showPlayerProfileScene(currentPlayer);
                } else {
                    System.err.println("Error: Main is null");
                }
                break;
            case "LeaderBoard":
                if (mainApp != null) {
                    mainApp.showLeaderboardScene();
                } else {
                    System.err.println("Error: Main is null");
                }
                break;
            default:
                System.out.println("Unknown button action");
                break;
        }
    }

    
    private double calculateWPM() {
        // Ensure totalTypedChars is not zero to prevent division by zero
        if (totalTypedChars == 0) {
            return 0.0;
        }

        if (useCountdownTimer) {
            // Calculate WPM for countdown timer mode
            if (totalMistakes > totalTypedChars) {
                return 0.0; // To avoid negative WPM in case of more mistakes than typed characters
            }

            // Count the number of correctly typed characters
            int correctlyTypedChars = totalTypedChars - totalMistakes;

            // WPM formula: (correctlyTypedChars / 5) normalized by 60 seconds
            return Math.max(0.0, (correctlyTypedChars / 5.0) / ((double) startTime / 60.0));
        } else {
            // Calculate WPM for modes without countdown timer
            if (startTime == 0 || startTimeMillis == 0) {
                return 0.0;
            }

            // Count the number of correctly typed characters
            int correctlyTypedChars = totalTypedChars - totalMistakes;

            // Calculate the duration in minutes
            double durationInMinutes = (System.currentTimeMillis() - startTimeMillis) / 60000.0;

            // WPM formula: (correctlyTypedChars / 5) normalized by the duration in minutes
            return Math.max(0.0, (correctlyTypedChars / 5.0) / durationInMinutes);
        }
    }


    private double calculateAccuracy() {
        // Ensure totalTypedChars is not zero to prevent division by zero
        if (totalTypedChars == 0) {
            return 0.0;
        }

        // Calculate accuracy based on correctly typed characters divided by total typed characters
        double accuracy = Math.max(0.0, ((double) (totalTypedChars - totalMistakes) / totalTypedChars) * 100.0);
        return accuracy;
    }

    public void restartGame() {
        isFirstLetterTyped = false;
        totalTypedChars = 0;
        totalMistakes = 0;
        seconds = startTime;

        // Clear text areas
        typingArea.clear();
        tx.setText("Timer: " + seconds.toString());

        // Stop the existing stopwatch if running
        stopStopwatch();

        // Explicitly set stopwatchTimeline to null before re-initializing
        stopwatchTimeline = null;

        // Reset the prompt text display
        wordLimit = 0;


        // Update the startTimeMillis to the current time
        startTimeMillis = System.currentTimeMillis();

        // Update the timeElapsedLabel to display "Stopwatch: 0 seconds"
        timeElapsedLabel.setText("Stopwatch: 0 seconds");
    }


    
    public void setUseCountdownTimer(boolean useCountdownTimer) {
        this.useCountdownTimer = useCountdownTimer;
    }    
}