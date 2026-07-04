package com.escaperoom.state;

import com.escaperoom.model.Room;
import com.escaperoom.exception.RoomNotAvailableException;

public class AvailableState implements RoomState {

    @Override
    public void book(Room room) {
        room.setState(new BookedState());
    }

    @Override
    public void startSession(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " cannot start a session - it hasn't been booked yet.");
    }

    @Override
    public void endSession(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " has no active session to end.");
    }

    @Override
    public void cancelBooking(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " has no booking to cancel.");
    }

    @Override
    public void finishCleaning(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " is not being cleaned.");
    }

    @Override
    public String getName() {
        return "AVAILABLE";
    }
}
