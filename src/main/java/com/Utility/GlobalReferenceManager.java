package com.Utility;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.GameObjects.Coin.Coin;
import com.GameObjects.PacMan.PacMan;
import com.UI.BoardController;

import javafx.application.Platform;

public class GlobalReferenceManager {
    public static PacMan pacMan;
    public static Queue<Coin> Coins = new ConcurrentLinkedQueue<Coin>();

    public static BoardController boardController;

    private static int m_score = 0;

    public static int getScore() {
        return m_score;
    }

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
}
