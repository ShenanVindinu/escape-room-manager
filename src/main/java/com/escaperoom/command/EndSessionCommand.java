package com.escaperoom.command;

import com.escaperoom.manager.VenueManager;
import com.escaperoom.cli.ConsoleIO;
import com.escaperoom.cli.ConsoleView;
import com.escaperoom.exception.RoomNotAvailableException;
import com.escaperoom.exception.InvalidBookingException;

public class EndSessionCommand implements Command {

    private final ConsoleView view = new ConsoleView();

    @Override
    public void execute() {
        VenueManager venue = VenueManager.getInstance();
        String bookingId = ConsoleIO.prompt("Booking ID to end: ");
        String solvedInput = ConsoleIO.prompt("Did they solve it? (y/n): ");
        boolean solved = solvedInput.equalsIgnoreCase("y");

        try {
            venue.endSession(bookingId, solved);
            view.showMessage("Session ended. Room now cleaning - use 'Finish cleaning' when ready.");
        } catch (RoomNotAvailableException | InvalidBookingException e) {
            view.showError("Could not end session: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "End a live session";
    }
}
