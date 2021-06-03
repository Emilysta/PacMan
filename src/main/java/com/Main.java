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

/**
 * Main class managing the applicaiton visuals Implements a singleton pattern,
 * for possibility of retrievieng main stage as well as changing the current
 * scene from scene controllers
 */
public class Main extends Application {

    private static Main m_instance;
    private Stage m_mainStage;

    public Main() {
        m_instance = this;
    }

    /**
     * Singleton pattern methdo to retrieve the current instance of class
     * 
     * @return Main - current singleton instance of class Main
     */
    public static Main getInstance() {
        return m_instance;
    }

    /**
     * Main method in program - launches the javaFX application with given arguments
     * 
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method allows to retrieve current stage.
     * 
     * @return Stage - current active stage
     */
    public Stage getStage() {
        return m_mainStage;
    }

    /**
     * Overridden method from javaFX application class. Method starts the
     * application with primary stage being MainWindoww
     * 
     * @param primaryStage - the stage the application starts on
     * @throws IOException - if MainWindow.fxml is not found
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        m_mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MainWindow.fxml"));
        primaryStage.setTitle("/PacManFX");
        primaryStage.setScene(new Scene(root, 400, 600));
        Image image = new Image("/icon.png");
        primaryStage.getIcons().add(image);
        m_mainStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Method gets called when the application exits safely by user input
     */
    @Override
    public void stop() {
        GameLoop.getInstance().stop();
    }

    /**
     * Method to stop the current application, and exit completely
     */
    public void exit() {
        GameLoop.getInstance().stop();
        Platform.exit();
    }

    /**
     * Method allowing to switch the current stage to MainWindow scene
     */
    public void goToMainWindow() {
        try {
            replaceSceneContent("MainWindow.fxml", 400, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method allowing to switch the current stage to Leaderboard scene
     */
    public void goToLeaderboard() {
        try {
            replaceSceneContent("Leaderboard.fxml", 400, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method allowing to switch the current stage to Game scene
     */
    public void goToBoard() {
        try {
            replaceSceneContent("Board.fxml", 840, 1080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Flexible method allowing to change current scene into one defined in
     * <p>fxml<p> parameter.
     * @param fxml - scene filename 
     * @param width - scene width
     * @param height - scene height
     * @throws Exception - if fxml is not found
     */
    private void replaceSceneContent(String fxml, double width, double height) throws Exception {
        Parent page = FXMLLoader.load(getClass().getClassLoader().getResource(fxml));
        Scene scene = new Scene(page, width, height);
        m_mainStage.setScene(scene);
        m_mainStage.sizeToScene();
    }
}
