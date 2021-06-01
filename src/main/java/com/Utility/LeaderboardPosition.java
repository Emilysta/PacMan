package com.Utility;

import java.io.Serializable;

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