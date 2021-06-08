package com.Utility;

import java.time.format.DateTimeFormatter;
/**
 * Klasa pomocnicza pozwalajaca na wypisanie wiadomosci na konsole
 */
public class Debug {

    /**
     * Wypisuje wiadomosc na konsole, z aktualnym czasem
     * @param message - wiadomosc do wyswietlenia
     */
    public static void Log(String message) {
        System.out.println("(Debug.Log)"+FormatMessage(message));
    }
    /**
     * Wypisuje wiadomosc na konsole, z aktualnym czasem w kolorze zoltym
     * @param message - wiadomosc do wyswietlenia
     */
    public static void LogWarning(String message) {
        System.out.println(ConsoleColors.YELLOW + "(Debug.LogWarning)"+ FormatMessage(message) + ConsoleColors.RESET);
    }

    /**
     * Wypisuje wiadomosc na konsole, z aktualnym czasem w kolorze czerwonym
     * @param message - wiadomosc do wyswietlenia
     */
    public static void LogError(String message) {
        System.out.println(ConsoleColors.RED + "(Debug.LogError)"+ FormatMessage(message) + ConsoleColors.RESET);
    }

    /**
     * Formatuje wiadomosc dodajac na poczatek aktualny czas
     * @param message - oryginalna wiadomosc
     * @return sformatowana wiadomosc
     */
    private static String FormatMessage(String message){
        String time = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return time + " : " + message;
    }
}
