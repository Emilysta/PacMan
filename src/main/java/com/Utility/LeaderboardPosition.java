package com.Utility;

import java.io.Serializable;
/**
 * Serializowalna klasa pozwalajaca na przechowywanie wynikow w tabeli wynikow
 */
public class LeaderboardPosition implements Serializable {
    public int Score;
    public String Name;

    @Override
    public String toString() {
        return Name + " : " +Score +" points";
    }
}