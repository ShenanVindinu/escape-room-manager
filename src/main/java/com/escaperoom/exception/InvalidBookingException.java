package com.escaperoom.exception;

/**
 * Thrown when a booking request fails validation
 * (e.g. group size out of range, bad time slot, unknown room).
 */
public class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}
