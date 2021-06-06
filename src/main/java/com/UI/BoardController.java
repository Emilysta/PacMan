package com.UI;

import com.Board.PathTypes;
import com.Main;
import com.Board.PredefinedBoard;
import com.GameLoop.GameLoop;
import com.GameObjects.Coin.Coin;
import com.GameObjects.Ghosts.Ghost;
import com.GameObjects.Ghosts.GhostType;
import com.GameObjects.PacMan.PacMan;
import com.Utility.GlobalReferenceManager;
import com.Utility.Sprite;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class BoardController {

    public StringProperty scoreText = new SimpleStringProperty();

    @FXML
    public Canvas mainCanvas;
    @FXML
    private GridPane BoardGridPane;

    private GameLoop m_mainGameLoop;
    private PredefinedBoard m_gameBoard;

    public void initializeGame() {
        try {
            GlobalReferenceManager.clearData();
            GlobalReferenceManager.boardController = this;
            m_gameBoard = new PredefinedBoard();
            setUpBoard();
            openBoard();
            m_mainGameLoop = GameLoop.getInstance();
            m_mainGameLoop.clearData();
            m_mainGameLoop.setBoard(m_gameBoard);
            setUpGame();

            addVisuals();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setScore(int score) {
        scoreText.set("Score: " + score);
    }

    public void loseGame() {
        m_mainGameLoop.stop();
        Platform.runLater(() -> {
            mainCanvas.setMouseTransparent(true);
            Button btn = new Button("Try again!");
            Button btn2 = new Button("Go back to main menu");
            BoardGridPane.add(btn, 12, 6, 12, 4);
            BoardGridPane.add(btn2, 12, 12, 12, 4);

            btn.setOnAction((event) ->{
                initializeGame();
                BoardGridPane.getChildren().remove(btn2);
                BoardGridPane.getChildren().remove(btn);
            } );
            btn2.setOnAction((event) -> Main.getInstance().goToMainWindow());
        });
    }

    private void setUpGame() {
        var x = 0;
        Sprite coin = new Sprite(new Image("/coin.png"), 20, 20);
        for (int[] i : m_gameBoard.BoardsPaths) {
            var y = 0;
            for (int j : i) {
                if (j == 1) {
                    GlobalReferenceManager.Coins.add(new Coin(coin, y, x));
                }
                y++;
            }
            x++;
        }
        GlobalReferenceManager.pacMan = new PacMan(new Sprite(new Image("/icon.png"), 30, 30));
        GlobalReferenceManager.blinky = new Ghost(new Sprite(new Image("/red.png"), 30, 30), GhostType.Blinky);
        GlobalReferenceManager.inky = new Ghost(new Sprite(new Image("/blue.png"), 30, 30), GhostType.Inky);
        GlobalReferenceManager.pinky = new Ghost(new Sprite(new Image("/pink.png"), 30, 30), GhostType.Pinky);
        GlobalReferenceManager.clyde = new Ghost(new Sprite(new Image("/orange.png"), 30, 30), GhostType.Clyde);
        m_mainGameLoop.setGraphicContext(mainCanvas.getGraphicsContext2D(), mainCanvas.getWidth(),
                mainCanvas.getHeight());
        m_mainGameLoop.start();
    }

    private void setUpBoard() {
        m_gameBoard.loadFromFile(PredefinedBoard.randFile());
    }

    private void openBoard() throws Exception {
        BoardGridPane.setStyle("-fx-background-color: #000000;");
        String filePath = "file:TERRAIN/";
        for (int i = 0; i < m_gameBoard.BoardPathTypes.length; i++) {
            for (int j = 0; j < m_gameBoard.BoardPathTypes[i].length; j++) {
                if(m_gameBoard.BoardPathTypes[i][j]!= PathTypes.EMPTY) {
                    String fileName = filePath + m_gameBoard.BoardPathTypes[i][j] + ".png";
                    Image image = new Image(fileName, 30, 30, true, true);
                    BoardGridPane.add(new ImageView(image), j, i);
                }
            }
        }
    }

    private void addVisuals() {
        Label lbl = new Label();
        BoardGridPane.add((lbl), 0, 32, 12, 1);
        lbl.textProperty().bind(scoreText);
    }
}
