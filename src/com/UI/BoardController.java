package com.UI;

import com.Board.PredefinedBoard;
import com.GameLoop.GameLoop;
import com.GameObjects.PacMan.PacMan;
import com.Utility.Sprite;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    @FXML
    public Canvas mainCanvas;
    @FXML
    private GridPane BoardGridPane;

    private GameLoop m_mainGameLoop;
    private PredefinedBoard m_gameBoard;

    public BoardController() {
        m_gameBoard = new PredefinedBoard();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setUpBoard();
            openBoard();
            m_mainGameLoop = GameLoop.getInstance();
            m_mainGameLoop.setBoard(m_gameBoard);
            setUpGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpGame() {
        PacMan m_pacMan = new PacMan(new Sprite(new Image("file:icon.png"), 30, 30));
        m_mainGameLoop.setGraphicContext(mainCanvas.getGraphicsContext2D(), mainCanvas.getWidth(), mainCanvas.getHeight());
        m_mainGameLoop.start();
    }

    private void setUpBoard() {
        m_gameBoard.loadFromFile(PredefinedBoard.randFile());
    }

    private void openBoard() throws Exception {
        String filePath = "file:TERRAIN/";
        for (int i = 0; i < m_gameBoard.BoardPathTypes.length; i++) {
            for (int j = 0; j < m_gameBoard.BoardPathTypes[i].length; j++) {
                String fileName = filePath + m_gameBoard.BoardPathTypes[i][j] + ".png";
                Image image = new Image(fileName, 30, 30, true, true);
                BoardGridPane.add(new ImageView(image), j, i);
            }
        }
    }
}
