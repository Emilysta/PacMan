package com.UI;

import com.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class MainWindowController {

    @FXML
    private Button ExitButton;
    @FXML
    private Button StartGameButton;

    public void StartGameAction(MouseEvent mouseEvent) {
        Main.getInstance().startGame();
    }

    public void ExitAction(MouseEvent mouseEvent) {
        Main.getInstance().exit();
    }

    public void ShowLeaderboardClicked(MouseEvent mouseEvent) {
        Main.getInstance().goToLeaderboard();
    }
}
