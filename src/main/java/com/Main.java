package com;

import com.GameLoop.GameLoop;
import com.UI.BoardController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Glowna klasa aplikacji, zarzadzajaca aktualna scena. Implementuje wzorzec
 * singletona, w celu mozliwosci zmiany aktualnie wyswietlanej sceny.
 */
public class Main extends Application {

    private static Main m_instance;
    private Stage m_mainStage;

    public Main() {
        m_instance = this;
    }

    /**
     * metoda zwracajaca singleton
     * 
     * @return Main - aktualncja instancja klasy
     */
    public static Main getInstance() {
        return m_instance;
    }

    /**
     * Glowna metoda programu - wywoluje odpalanie UI JavaFX
     * 
     * @param args - argumenty lini komend
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Metoda zwraca aktualny stage
     * 
     * @return Stage - aktywny stage
     */
    public Stage getStage() {
        return m_mainStage;
    }

    /**
     * Nadpisana metoda z JavaFX. Jest wywołana na poczatku aplikacji, i laduje
     * ekran MainWindow oraz ikone.
     * 
     * @param primaryStage - stage na ktorym aplikacja rozpoczyna prace
     * @throws IOException - jestli MainWindow.fxml nie znajduje sie w aplikacji.
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
     * Metoda wywoluje sie przy kazdym wyjsciu aplikacji
     */
    @Override
    public void stop() {
        GameLoop.getInstance().stop();
    }

    /**
     * Metoda pozwala na reczne wyjscie z aplikacji
     */
    public void exit() {
        GameLoop.getInstance().stop();
        Platform.exit();
    }

    /**
     * Metoda pozwala na zmiane aktualnej sceny na MainWindow
     */
    public void goToMainWindow() {
        try {
            replaceSceneContent("MainWindow.fxml", 400, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pozwala na zmiane aktualnej sceny na Leaderboard
     */
    public void goToLeaderboard() {
        try {
            replaceSceneContent("Leaderboard.fxml", 400, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Metoda pozwala na zmiane aktualnej sceny na scene z gra
     */
    public void goToBoard() {
        try {
            replaceSceneContent("Board.fxml", 840, 1080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pozwala na zmiane sceny na dowolna, okreslona przez sciezkie pliku.
     * 
     * @param fxml   - plik fxml ze scena
     * @param width  - szerokosc sceny
     * @param height - wysokosc sceny
     * @throws Exception - jesli plik fxml nie zostanie znaleziony
     */
    private void replaceSceneContent(String fxml, double width, double height) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxml));
        Parent page = (Parent) loader.load();
        Scene scene = new Scene(page, width, height);
        m_mainStage.setScene(scene);
        m_mainStage.sizeToScene();
        if (fxml.equals("Board.fxml")) {
            var controller = (BoardController) loader.getController();
            controller.initializeGame();
        }
    }
}
