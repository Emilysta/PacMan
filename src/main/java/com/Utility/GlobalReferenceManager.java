package com.Utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.GameObjects.Coin.Coin;
import com.GameObjects.Ghosts.Ghost;
import com.GameObjects.PacMan.PacMan;
import com.UI.BoardController;

import javafx.application.Platform;

/**
 * Klasa pomocnicza przechowujaca instancje aktualnych obiektow
 */
public class GlobalReferenceManager {
    public static PacMan pacMan;
    public static Ghost pinky;
    public static Ghost inky;
    public static Ghost clyde;
    public static Ghost blinky;
    public static Queue<Coin> Coins = new ConcurrentLinkedQueue<Coin>();

    public static BoardController boardController;

    private static int m_score = 0;
    private static int m_powerUps = 0;
    private static String m_leaderboardFilename = "highscores";
    private static ArrayList<LeaderboardPosition> m_leaderboardPositions;

    public static String getLeaderboardFilenmae() {
        return m_leaderboardFilename;
    }

    /**
     * @return aktualny wynik gracza
     */
    public static int getScore() {
        return m_score;
    }

    /**
     * Dodaje wartosc do aktualnego wyniku gracza, i powiadamia o tym klase
     * boardController w celu wyswietlenia tej wartosci na scenie.
     * 
     * @param value - wartosc do dodania
     */
    public static void addScore(int value) {
        m_score += value;
        Platform.runLater(() -> boardController.setScore(m_score));
    }

    /**
     * Czysci klase, przygotowujac ja na kolejna gre
     */
    public static void clearData() {
        pacMan = null;
        pinky = null;
        inky = null;
        blinky = null;
        clyde = null;
        Coins = new ConcurrentLinkedQueue<>();
        m_score = 0;
    }

    /**
     * Czysci klase, przygotowujac ja na kolejna gre i ustawia wynik z poprzedniej gry
     */
    public static void clearData(int score) {
        pacMan = null;
        pinky = null;
        inky = null;
        blinky = null;
        clyde = null;
        Coins = new ConcurrentLinkedQueue<>();
        m_score = score;
    }

    /**
     * Metoda zapisuje nowa pozycje z wynikiem do pliku z wynikami.
     * 
     * @param name - nazwa z jaka zapisac wynik
     */
    public static void saveLeaderboardPosition(String name) {
        var position = new LeaderboardPosition();
        position.Name = name;
        position.Score = GlobalReferenceManager.getScore();
        var leaderboard = getLeaderboard();
        leaderboard.add(position);
        try {
            FileOutputStream fout = new FileOutputStream(m_leaderboardFilename);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(leaderboard);
            oos.close();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pozwala na wczytanie pliku z wynikami do pamieci. Nazwa pliku jest
     * okreslona w polu klasy jako stala.
     * 
     * @return lista z najlepszymi wynikami, nieposortowana
     */

    public static ArrayList<LeaderboardPosition> getLeaderboard() {
        if (m_leaderboardPositions == null) {
            try {
                FileInputStream fin = new FileInputStream(m_leaderboardFilename);
                ObjectInputStream ois = new ObjectInputStream(fin);
                m_leaderboardPositions = (ArrayList<LeaderboardPosition>) ois.readObject();
                ois.close();
                fin.close();
            } catch (Exception e) {
                m_leaderboardPositions = new ArrayList<>();
            }
        }
        return m_leaderboardPositions;
    }

    /**
     * Metoda ustawia pacmana w tryb powerUp w ktorym moze zjadac duchy. Tryb
     * trwa 10 sekund od zjedzenia ostatniego ulepszonego obiektu jadalnego.
     */

    public static void makePacmanPoweredUp() {
        m_powerUps++;
        pacMan.setPowerUp(true);
        Debug.Log("Powerup on");
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                m_powerUps--;
                if (m_powerUps == 0) {
                    pacMan.setPowerUp(false);
                    Debug.Log("Powerup off");
                }
            }
        }, 10000);
    }
}
