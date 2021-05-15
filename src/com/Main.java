package com;

import com.GameLoop.GameLoop;
import com.GameLoop.InputManager;
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

    public Stage getStage() {return m_mainStage;}

    public static void main(String[] args) {
        launch(args);
    }

    public void exit() {
        GameLoop.getInstance().stop();
        Platform.exit();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        m_mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../com/UI/MainWindow.fxml"));
        primaryStage.setTitle("PacManFX");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.getIcons().add(new Image("file:icon.png"));
        m_mainStage.setResizable(false);
        primaryStage.show();
    }

    public void startGame(){
        try {
            replaceSceneContent("../com/UI/GameWindow.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToMainWindow() {
        try {
            replaceSceneContent("../com/UI/MainWindow.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToLeaderboard() {
        try {
            replaceSceneContent("../com/UI/Leaderboard.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Parent replaceSceneContent(String fxml) throws Exception {
        Parent page = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = m_mainStage.getScene();
        if (scene == null) {
            scene = new Scene(page, 700, 450);
            m_mainStage.setScene(scene);
        } else {
            m_mainStage.getScene().setRoot(page);
        }
        m_mainStage.sizeToScene();
        return page;
    }
}