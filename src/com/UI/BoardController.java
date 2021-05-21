package com.UI;

import com.Board.PredefinedBoard;
import com.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable {

    public PredefinedBoard PreBoard;
    @FXML
    private GridPane BoardGridPane;

    public BoardController() {
        PreBoard = new PredefinedBoard();
    }

    public void setUpBoard() {
        PreBoard.loadFromFile(PredefinedBoard.randFile());
    }

    public void openBoard() throws Exception {
        String filePath = "file:TERRAIN/";
        for(int i =0; i<PreBoard.BoardPathTypes.length;i++){
            for(int j =0;j<PreBoard.BoardPathTypes[i].length;j++){
                String fileName = filePath + PreBoard.BoardPathTypes[i][j] + ".png";
                Image image = new Image(fileName,20,20,true,true);
                BoardGridPane.add(new ImageView(image),j,i);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setUpBoard();
            openBoard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
