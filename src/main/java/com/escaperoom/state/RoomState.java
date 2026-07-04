package com.escaperoom.state;

import com.escaperoom.model.Room;
import com.escaperoom.exception.RoomNotAvailableException;

/**
 * STATE PATTERN.
 *
 * Each concrete state decides what is legal to do to a Room while it is
 * in that state, and decides what the *next* state is. The Room itself
 * never contains "if state == X" logic - it just delegates here.
 */
public interface RoomState {

    /** Attempt to book the room. Only legal from Available. */
    void book(Room room) throws RoomNotAvailableException;

    /** Attempt to start a live session. Only legal from Booked. */
    void startSession(Room room) throws RoomNotAvailableException;

    /** Attempt to end a live session. Only legal from InSession -> moves to Cleaning. */
    void endSession(Room room) throws RoomNotAvailableException;

    /** Attempt to cancel a booking. Only legal from Booked -> back to Available. */
    void cancelBooking(Room room) throws RoomNotAvailableException;

    /** Called by the venue "clock" once cleaning time has elapsed. */
    void finishCleaning(Room room) throws RoomNotAvailableException;

    /** Human readable name shown in reports/CLI, e.g. "AVAILABLE". */
    String getName();
}
