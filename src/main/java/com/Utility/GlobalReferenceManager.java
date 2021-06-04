package com.Utility;

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

    /**
     * @return current player score
     */
    public static int getScore() {
        return m_score;
    }
    /**
     * Adds given value to the overall score and updates the score on board
     * controller thread, to reflect the changes on screen
     * @param value
     */
    public static void addScore(int value) {
        m_score += value;
        Debug.LogWarning("dodano scorea:" + m_score);
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
    public static void clearData(){
        pacMan=null;
        pinky=null;
        inky=null;
        blinky=null;
        clyde=null;
        Coins=new ConcurrentLinkedQueue<Coin>();
        m_score=0;
    }
}
