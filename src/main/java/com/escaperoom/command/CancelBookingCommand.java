package com.escaperoom.command;

import com.escaperoom.manager.VenueManager;
import com.escaperoom.cli.ConsoleIO;
import com.escaperoom.cli.ConsoleView;
import com.escaperoom.exception.RoomNotAvailableException;
import com.escaperoom.exception.InvalidBookingException;

public class CancelBookingCommand implements Command {

    private final ConsoleView view = new ConsoleView();

    @Override
    public void execute() {
        VenueManager venue = VenueManager.getInstance();
        String bookingId = ConsoleIO.prompt("Booking ID to cancel: ");

        try {
            venue.cancelBooking(bookingId);
            view.showMessage("Booking " + bookingId + " cancelled.");
        } catch (RoomNotAvailableException | InvalidBookingException e) {
            view.showError("Could not cancel: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "Cancel a booking";
    }
}
