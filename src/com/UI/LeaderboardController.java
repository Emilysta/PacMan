package com.UI;

import com.Main;
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


    public void loadLeaderboard() {
        leaderboardPositions = FXCollections.observableArrayList();
        File file = new File(fileName);
        try {
            if (!file.createNewFile()) {
                FileInputStream fileStream = new FileInputStream(fileName);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);
                boolean isEndOfFile = false;
                while (!isEndOfFile) {
                    LeaderboardPosition position = (LeaderboardPosition) objectStream.readObject();
                    if (position != null)
                        leaderboardPositions.add(position);
                    else
                        isEndOfFile = true;
                }
                objectStream.close();
                fileStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        var pos = new LeaderboardPosition();
        pos.Name = "Majkel";
        pos.Score = 999;
        leaderboardPositions.add(pos);
        LeaderboardListView.setItems(leaderboardPositions);
        //LeaderboardList.setListData(leaderboardPositions.toArray());
    }

    public void BackButtonClicked(MouseEvent mouseEvent) {
        Main.getInstance().goToMainWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLeaderboard();
    }
}
