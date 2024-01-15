package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartScreenController {

    private Main mainApp;

    @FXML
    private Button testButton;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void button() {
        testButton.setOnAction(event -> showLoginScene());
    }

    private void showLoginScene() {
        mainApp.showLoginScene();
    }
}