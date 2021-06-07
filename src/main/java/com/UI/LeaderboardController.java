package com.UI;

import com.Main;
import com.Utility.GlobalReferenceManager;
import com.Utility.LeaderboardPosition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class LeaderboardController implements Initializable {
    @FXML
    private Button BackButton;
    @FXML
    private ListView<LeaderboardPosition> LeaderboardListView;

    public void BackButtonClicked(MouseEvent mouseEvent) {
        Main.getInstance().goToMainWindow();
    }

    /**
     * On initialization of window this method loads leaderboard from file,
     * then sorts it by score;
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var list = FXCollections.observableArrayList(GlobalReferenceManager.getLeaderboard());
        list.sort((p1,p2) -> Integer.compare(p2.Score,p1.Score));
        LeaderboardListView.setItems(list);
    }
}
