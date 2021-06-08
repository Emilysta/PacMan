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

    /**
     * Metoda pozwala na rozpoczenie nowej gry.
     * @param mouseEvent
     */
    public void StartGameAction(MouseEvent mouseEvent) {
        Main.getInstance().goToBoard();
    }

    /**
     * Metoda pozwala na wyjscie z aplikacji
     * @param mouseEvent
     */
    public void ExitAction(MouseEvent mouseEvent) {
        Main.getInstance().exit();
    }

    /**
     * Metoda pozwala na przejscie do tablicy wynikow
     * @param mouseEvent
     */
    public void ShowLeaderboardClicked(MouseEvent mouseEvent) {
        Main.getInstance().goToLeaderboard();
    }
}
