package com;

import com.GameLoop.GameLoop;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Main m_instance;
    private Stage m_mainStage;

    public Main() {
        m_instance = this;
    }

    public static Main getInstance() {
        return m_instance;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getStage() {
        return m_mainStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        m_mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../com/UI/MainWindow.fxml"));
        primaryStage.setTitle("PacManFX");
        primaryStage.setScene(new Scene(root, 400, 600));
        primaryStage.getIcons().add(new Image("file:icon.png"));
        m_mainStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop(){
        GameLoop.getInstance().stop();
    }

    public void exit() {
        GameLoop.getInstance().stop();
        Platform.exit();
    }

    public void goToMainWindow() {
        try {
            replaceSceneContent("../com/UI/MainWindow.fxml", 400, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToLeaderboard() {
        try {
            replaceSceneContent("../com/UI/Leaderboard.fxml", 400, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToBoard() {
        try {
            replaceSceneContent("../com/UI/Board.fxml", 840, 1080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceSceneContent(String fxml, double width, double height) throws Exception {
        Parent page = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(page, width, height);
        m_mainStage.setScene(scene);
        m_mainStage.sizeToScene();
    }
}
