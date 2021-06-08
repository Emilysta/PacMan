package com.UI;

import com.Board.PathTypes;

import java.util.Random;

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
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class BoardController {

    public StringProperty scoreText = new SimpleStringProperty();
    public Region blendCanvas = new Region();
    @FXML
    public Canvas mainCanvas;
    @FXML
    private GridPane BoardGridPane;
    @FXML
    private GridPane BoardGridPane2;
    @FXML
    private Button tryAgainButton;
    @FXML
    private Button goToMainWindowButton;

    private GameLoop m_mainGameLoop;
    private PredefinedBoard m_gameBoard;

    private boolean m_isInGame;

    /**
     * Metoda ustawia aktualny wynik gracza do wyswietlenia, oraz sprawdza,
     * czy na danej planszy są jeszcze pieniążki, jeśli ich nie ma to tworzy nowa grę i przypisuje pieniążki do nowej gry.
     *
     * 
     * @param score - wynik gracza
     */
    public void setScore(int score) {
        scoreText.set("Score: " + score);
        if(GlobalReferenceManager.Coins.size()==0)
        {
            m_mainGameLoop.stop();
            var dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setTitle("Play next level");
            dialog.setContentText("Do you want to play next level?");
            var result = dialog.showAndWait();
            if (result.get() == ButtonType.OK) {
                initializeGame(score);
            }
            else {
                Main.getInstance().goToMainWindow();
            }
        }
    }

    /**
     * Metoda inicjalizująca nowa gre. Ustawia wszystkie potrzebne zmienne, wczytuje
     * plansze, tworzy obiekty w grze i rozpoczyna glowna petle gry.
     */
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
            m_isInGame = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda inicjalizująca nowa gre. Ustawia wszystkie potrzebne zmienne, wczytuje nową
     * plansze, tworzy obiekty w grze i rozpoczyna glowna petle gry, ustawia wynik z poprzedniego poziomu
     */
    public void initializeGame(int score) {
        try {
            GlobalReferenceManager.clearData(score);
            GlobalReferenceManager.boardController = this;
            m_gameBoard = new PredefinedBoard();
            setUpBoard();
            openBoard();
            m_mainGameLoop = GameLoop.getInstance();
            m_mainGameLoop.clearData();
            m_mainGameLoop.setBoard(m_gameBoard);
            setUpGame();
            addVisuals();
            m_isInGame = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda przerywa gre, wyswietla powiadomienie o zapisaniu wyniku do tablicy
     * oraz daje wybór ponownego zagrania w gre, lub wyjscia do menu głownego
     */
    public void loseGame() {
        if (m_isInGame) {
            m_isInGame = false;
            //m_mainGameLoop.stop();
            Platform.runLater(() -> {
                var dialog = new TextInputDialog("User");
                dialog.setTitle("Save highscore");
                dialog.setHeaderText("Please enter your name for the leaderboard.\n Your score: "
                        + GlobalReferenceManager.getScore());
                dialog.setContentText("Leaderboard name:");
                var result = dialog.showAndWait();
                if (result.isPresent())
                    GlobalReferenceManager.saveLeaderboardPosition(result.get());
                mainCanvas.setMouseTransparent(true);
                BoardGridPane2.setStyle("-fx-background-color: #0000007D;");
                goToMainWindowButton.setVisible(true);
                tryAgainButton.setVisible(true);
                goToMainWindowButton.setDisable(false);
                tryAgainButton.setDisable(false);
                blendCanvas.setVisible(true);
                tryAgainButton.toFront();
                goToMainWindowButton.toFront();

            });
            m_mainGameLoop.stop();
        }
    }

    /**
     * Metoda tworzy nowe obiekty do zjedzenia, pacmana oraz duchy i ustawia zmienne
     * w glownej petli gry.
     */
    private void setUpGame() {
        var rand = new Random();
        var powerUpsCount = 3;
        var x = 0;
        Sprite coin = new Sprite(new Image("/coin.png"), 20, 20);
        Sprite power = new Sprite(new Image("/power.png"), 20, 20);
        for (int[] i : m_gameBoard.BoardsPaths) {
            var y = 0;
            for (int j : i) {
                if (j == 1) {
                    if (rand.nextFloat() > 0.1f)
                        GlobalReferenceManager.Coins.add(new Coin(coin, y, x, false));
                    else
                        GlobalReferenceManager.Coins.add(new Coin(power, y, x, true));
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

    /**
     * Metoda wczytuje nowa plansze z losowego pliku.
     */
    private void setUpBoard() {
        m_gameBoard.loadFromFile(PredefinedBoard.randFile());
    }

    /**
     * Metoda uklada odpowiednie grafiki na planszy.
     * 
     * @throws Exception jesli plik nie zostanie znaleziony
     */
    private void openBoard() throws Exception {
        BoardGridPane.setStyle("-fx-background-color: #000000;");
        BoardGridPane.getChildren().removeIf(x -> x.getClass().equals(ImageView.class));
        String filePath = "/";
        for (int i = 0; i < m_gameBoard.BoardPathTypes.length; i++) {
            for (int j = 0; j < m_gameBoard.BoardPathTypes[i].length; j++) {
                if (m_gameBoard.BoardPathTypes[i][j] != PathTypes.EMPTY) {
                    String fileName = filePath + m_gameBoard.BoardPathTypes[i][j] + ".png";
                    Image image = new Image(fileName, 30, 30, true, true);
                    BoardGridPane.add(new ImageView(image), j, i);
                }
            }
        }
    }

    /**
     * Metoda dodaje wizualne elementy na scenie.
     */
    private void addVisuals() {
        Label lbl = new Label();
        BoardGridPane.add((lbl), 0, 32, 12, 1);
        lbl.textProperty().bind(scoreText);
        goToMainWindowButton.setVisible(false);
        tryAgainButton.setVisible(false);
        tryAgainButton.toFront();
        goToMainWindowButton.toFront();
        tryAgainButton.setOnAction(e -> {
            tryAgainButton.setDisable(true);
            goToMainWindowButton.setDisable(true);
            tryAgainButton.setVisible(false);
            goToMainWindowButton.setVisible(false);
            blendCanvas.setVisible(false);
            BoardGridPane2.setStyle("-fx-background-color: #00000000;");
            initializeGame();
        });
        goToMainWindowButton.setOnAction((event) -> Main.getInstance().goToMainWindow());
    }
}
