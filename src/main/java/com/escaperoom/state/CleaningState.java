package com.escaperoom.state;

import com.escaperoom.model.Room;
import com.escaperoom.exception.RoomNotAvailableException;

public class CleaningState implements RoomState {

    @Override
    public void book(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " is being cleaned/reset - try again shortly.");
    }

    @Override
    public void startSession(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " is being cleaned - cannot start a session.");
    }

    @Override
    public void endSession(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " has no active session (it's being cleaned).");
    }

    @Override
    public void cancelBooking(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " has no booking to cancel (it's being cleaned).");
    }

    @Override
    public void finishCleaning(Room room) {
        room.setState(new AvailableState());
    }

    @Override
    public String getName() {
        return "CLEANING";
    }
}
