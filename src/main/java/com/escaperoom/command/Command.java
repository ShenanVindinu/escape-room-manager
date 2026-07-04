package com.escaperoom.command;

/**
 * COMMAND PATTERN.
 *
 * Every CLI action is an object implementing this interface. The CLI
 * (the "Invoker") calls execute() without knowing what the command
 * actually does. This is also what makes the reflection-based menu
 * in CommandLineInterface possible - every class implementing
 * Command in this package is auto-discovered and wired into the menu.
 */
public interface Command {

    /** Runs the command, reading whatever input it needs from the console. */
    void execute();

    /** Short label shown in the CLI menu, e.g. "Book a room". */
    String getDescription();
}
