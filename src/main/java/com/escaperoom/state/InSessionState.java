package com.escaperoom.state;

import com.escaperoom.model.Room;
import com.escaperoom.exception.RoomNotAvailableException;

public class InSessionState implements RoomState {

    @Override
    public void book(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " is currently in a live session.");
    }

    @Override
    public void startSession(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " already has a session in progress.");
    }

    @Override
    public void endSession(Room room) {
        // Real props need resetting before the next group - hence Cleaning,
        // not straight back to Available.
        room.setState(new CleaningState());
    }

    @Override
    public void cancelBooking(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " has a session already running - can't cancel now.");
    }

    @Override
    public void finishCleaning(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " is not being cleaned.");
    }

    @Override
    public String getName() {
        return "IN_SESSION";
    }
}
