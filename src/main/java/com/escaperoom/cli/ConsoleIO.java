package com.escaperoom.cli;

import java.util.Scanner;

/**
 * A single shared Scanner so every Command reads from the same
 * System.in stream without each one opening its own Scanner
 * (which can cause input to be consumed unpredictably).
 */
public class ConsoleIO {
    private static final Scanner scanner = new Scanner(System.in);

    private ConsoleIO() {}

    public static String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public static int promptInt(String message) {
        while (true) {
            String input = prompt(message);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a whole number.");
            }
        }
    }
}
