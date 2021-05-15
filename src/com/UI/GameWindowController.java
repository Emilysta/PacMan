package com.UI;

import com.GameLoop.GameLoop;
import com.GameObjects.PacMan.PacMan;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GameWindowController implements Initializable {
    @FXML
    private Canvas MainCanvas;
    private PacMan pacman;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GraphicsContext gc = MainCanvas.getGraphicsContext2D();
        Image pac = new Image("file:icon.png");
        gc.drawImage(pac,0,0);
        pacman = new PacMan();
        GameLoop.getInstance().start();
    }
}
