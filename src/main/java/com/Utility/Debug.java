package com.Utility;

import java.time.format.DateTimeFormatter;
/**
 * Utility class useful for showing informations in the console
 */
public class Debug {

    /**
     * Prints the message into console with the current time
     * @param message - string to print in console
     */
    public static void Log(String message) {
        System.out.println("(Debug.Log)"+FormatMessage(message));
    }
    /**
     * Prints the message in yellow color, stating that it's a warning. Adds
     * current time
     * @param message - string to print in console
     */
    public static void LogWarning(String message) {
        System.out.println(ConsoleColors.YELLOW + "(Debug.LogWarning)"+ FormatMessage(message) + ConsoleColors.RESET);
    }

    /**
     * Prints the message in red color, stating that it's an error. Add current time
     * @param message - string to print in console
     */
    public static void LogError(String message) {
        System.out.println(ConsoleColors.RED + "(Debug.LogError)"+ FormatMessage(message) + ConsoleColors.RESET);
    }

    /**
     * Format the current string by adding time to the front of it
     * @param message - original string
     * @return formatted string
     */
    private static String FormatMessage(String message){
        String time = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return time + " : " + message;
    }
}
