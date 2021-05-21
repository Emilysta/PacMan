package com.UI;

import com.Board.PredefinedBoard;
import com.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardManager implements Initializable {
    public PredefinedBoard preBoard;

    @FXML
    private GridPane pane;

    public BoardManager() {
        preBoard = new PredefinedBoard();
    }

    public void setUpBoard() {
        preBoard.loadFromFile(PredefinedBoard.randFile());
    }

    public void openBoard() throws Exception {
        Main.getInstance().replaceSceneContent("elko");
        
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
