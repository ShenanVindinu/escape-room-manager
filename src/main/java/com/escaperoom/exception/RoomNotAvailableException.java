package com.escaperoom.exception;

/**
 * Thrown when an operation requires a room to be available/bookable
 * but the room's current state does not allow it.
 */
public class RoomNotAvailableException extends Exception {
    public RoomNotAvailableException(String message) {
        super(message);
    }
}
