package com.escaperoom.command;

import com.escaperoom.manager.VenueManager;
import com.escaperoom.cli.ConsoleIO;
import com.escaperoom.cli.ConsoleView;
import com.escaperoom.exception.RoomNotAvailableException;
import com.escaperoom.exception.InvalidBookingException;

public class StartSessionCommand implements Command {

    private final ConsoleView view = new ConsoleView();

    @Override
    public void execute() {
        VenueManager venue = VenueManager.getInstance();
        String bookingId = ConsoleIO.prompt("Booking ID to start: ");

        try {
            venue.startSession(bookingId);
            view.showMessage("Session started for booking " + bookingId + ".");
        } catch (RoomNotAvailableException | InvalidBookingException e) {
            view.showError("Could not start session: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "Start a live session";
    }
}
