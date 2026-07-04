package com.escaperoom.command;

import com.escaperoom.manager.VenueManager;
import com.escaperoom.cli.ConsoleIO;
import com.escaperoom.cli.ConsoleView;
import com.escaperoom.exception.RoomNotAvailableException;
import com.escaperoom.exception.InvalidBookingException;

public class FinishCleaningCommand implements Command {

    private final ConsoleView view = new ConsoleView();

    @Override
    public void execute() {
        VenueManager venue = VenueManager.getInstance();
        String roomName = ConsoleIO.prompt("Room name that finished cleaning: ");

        try {
            venue.finishCleaning(roomName);
            view.showMessage(roomName + " is now AVAILABLE again.");
        } catch (RoomNotAvailableException | InvalidBookingException e) {
            view.showError("Could not finish cleaning: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "Mark a room's cleaning as finished";
    }
}
