package com.escaperoom.state;

import com.escaperoom.model.Room;
import com.escaperoom.exception.RoomNotAvailableException;

public class BookedState implements RoomState {

    @Override
    public void book(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " is already booked.");
    }

    @Override
    public void startSession(Room room) {
        room.setState(new InSessionState());
    }

    @Override
    public void endSession(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " has no active session yet - it's only booked.");
    }

    @Override
    public void cancelBooking(Room room) {
        room.setState(new AvailableState());
    }

    @Override
    public void finishCleaning(Room room) throws RoomNotAvailableException {
        throw new RoomNotAvailableException(
            room.getName() + " is not being cleaned.");
    }

    @Override
    public String getName() {
        return "BOOKED";
    }
}
