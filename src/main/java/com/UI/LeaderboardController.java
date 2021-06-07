package com.UI;

import com.Main;
import com.Utility.GlobalReferenceManager;
import com.Utility.LeaderboardPosition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;


public class LeaderboardController implements Initializable {
    private final String fileName = "highscores";
    @FXML
    private Button BackButton;
    @FXML
    private ListView<LeaderboardPosition> LeaderboardListView;
    private ObservableList<LeaderboardPosition> leaderboardPositions;

    public void BackButtonClicked(MouseEvent mouseEvent) {
        Main.getInstance().goToMainWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LeaderboardListView.setItems(FXCollections.observableArrayList(GlobalReferenceManager.getLeaderboard()));
    }
}
