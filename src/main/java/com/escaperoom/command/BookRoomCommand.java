package com.escaperoom.command;

import com.escaperoom.manager.VenueManager;
import com.escaperoom.model.Booking;
import com.escaperoom.cli.ConsoleIO;
import com.escaperoom.cli.ConsoleView;
import com.escaperoom.exception.RoomNotAvailableException;
import com.escaperoom.exception.InvalidBookingException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BookRoomCommand implements Command {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final ConsoleView view = new ConsoleView();

    @Override
    public void execute() {
        VenueManager venue = VenueManager.getInstance();

        String roomName = ConsoleIO.prompt("Room name: ");
        String customerName = ConsoleIO.prompt("Customer name: ");
        int groupSize = ConsoleIO.promptInt("Group size: ");
        String timeStr = ConsoleIO.prompt("Time slot (yyyy-MM-dd HH:mm): ");

        try {
            LocalDateTime timeSlot = LocalDateTime.parse(timeStr, FORMAT);
            Booking booking = venue.bookRoom(roomName, customerName, groupSize, timeSlot);
            view.showBooking("Booked!", booking);
        } catch (DateTimeParseException e) {
            view.showError("Invalid date/time format. Expected yyyy-MM-dd HH:mm");
        } catch (RoomNotAvailableException | InvalidBookingException e) {
            view.showError("Booking failed: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "Book a room";
    }
}
