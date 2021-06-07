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
 * Helper class holding instances of in-game objects.
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
    private static String m_leaderboardFilename = "highscores";
    private static ArrayList<LeaderboardPosition> m_leaderboardPositions;

    public static String getLeaderboardFilenmae() {
        return m_leaderboardFilename;
    }

    /**
     * @return current player score
     */
    public static int getScore() {
        return m_score;
    }

    /**
     * Adds given value to the overall score and updates the score on board
     * controller thread, to reflect the changes on screen
     * 
     * @param value
     */
    public static void addScore(int value) {
        m_score += value;
        // Debug.LogWarning("dodano scorea:" + m_score);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boardController.setScore(m_score);

            }
        });
    }

    /**
     * Clears all data from global reference manager
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
     * Method saves given leaderboard position into the file specified in variable
     * LeaderboardFilename
     * 
     * @param name - name to save in the file
     */
    public static void saveLeaderboardPosition(String name){
        var position = new LeaderboardPosition();
        position.Name = name;
        position.Score = GlobalReferenceManager.getScore();
        var leaderboard = getLeaderboard();
        leaderboard.add(position);
        try{
            FileOutputStream fout = new FileOutputStream(m_leaderboardFilename);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(leaderboard);
            oos.close();
            fout.close();
        }catch
        (IOException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<LeaderboardPosition> getLeaderboard(){
        if(m_leaderboardPositions == null){
            try{
            FileInputStream fin = new FileInputStream(m_leaderboardFilename);
            ObjectInputStream ois = new ObjectInputStream(fin);
            m_leaderboardPositions = (ArrayList<LeaderboardPosition>) ois.readObject();
            ois.close();
            fin.close();
            }
            catch(Exception e){
                m_leaderboardPositions = new ArrayList<>();
            }
        }
        return m_leaderboardPositions;
    }
}
