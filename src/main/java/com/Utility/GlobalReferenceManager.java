package com.Utility;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.GameObjects.Coin.Coin;
import com.GameObjects.PacMan.PacMan;

public class GlobalReferenceManager {
    public static PacMan pacMan;
    public static Queue<Coin> Coins = new ConcurrentLinkedQueue();

    private static int m_score = 0;

    public static int getScote() {
        return m_score;
    }

    public static void addScore(int value) {
        m_score += value;
        Debug.LogWarning("dodano scorea:" + m_score);
    }
}
