package com.Utility;

import java.io.Serializable;
/**
 * Serializable class to hold instances of player scores which should be saved
 * into file
 */
public class LeaderboardPosition implements Serializable {
    public int Score;
    public String Name;

    @Override
    public String toString() {
        return "LeaderboardPosition{" +
                "Score=" + Score +
                ", Name=" + Name +
                '}';
    }
}