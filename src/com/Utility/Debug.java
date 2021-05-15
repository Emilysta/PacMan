package com.Utility;

import java.time.format.DateTimeFormatter;

public class Debug {

    public static void Log(String message) {
        System.out.println("(Debug.Log)"+FormatMessage(message));
    }

    public static void LogWarning(String message) {
        System.out.println(ConsoleColors.YELLOW + "(Debug.LogWarning)"+ FormatMessage(message) + ConsoleColors.RESET);
    }

    public static void LogError(String message) {
        System.out.println(ConsoleColors.RED + "(Debug.LogError)"+ FormatMessage(message) + ConsoleColors.RESET);
    }

    private static String FormatMessage(String message){
        String time = java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return time + " : " + message;
    }
}
