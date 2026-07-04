package com.escaperoom.model;

import com.escaperoom.state.RoomState;
import com.escaperoom.state.AvailableState;
import com.escaperoom.exception.RoomNotAvailableException;

/**
 * Represents a single escape room at the venue.
 *
 * Room does NOT contain any "if state == X" branching - all of that
 * responsibility is delegated to the current RoomState object
 * (see the state package). This is the core of the State pattern.
 */
public class Room {
    private final String name;
    private final String theme;
    private final int difficulty; // 1 (easy) - 5 (hard)
    private final int maxGroupSize;

    private RoomState state;

    public Room(String name, String theme, int difficulty, int maxGroupSize) {
        this.name = name;
        this.theme = theme;
        this.difficulty = difficulty;
        this.maxGroupSize = maxGroupSize;
        this.state = new AvailableState(); // every room starts Available
    }

    // --- State delegation ---

    public void book() throws RoomNotAvailableException {
        state.book(this);
    }

    public void startSession() throws RoomNotAvailableException {
        state.startSession(this);
    }

    public void endSession() throws RoomNotAvailableException {
        state.endSession(this);
    }

    public void cancelBooking() throws RoomNotAvailableException {
        state.cancelBooking(this);
    }

    public void finishCleaning() throws RoomNotAvailableException {
        state.finishCleaning(this);
    }

    /** Package-visible-in-spirit: only RoomState implementations should call this. */
    public void setState(RoomState state) {
        this.state = state;
    }

    public String getStateName() {
        return state.getName();
    }

    // --- Plain getters ---

    public String getName() {
        return name;
    }

    public String getTheme() {
        return theme;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    @Override
    public String toString() {
        return String.format("%-12s | %-16s | difficulty %d/5 | max %d people | [%s]",
                name, theme, difficulty, maxGroupSize, getStateName());
    }
}
