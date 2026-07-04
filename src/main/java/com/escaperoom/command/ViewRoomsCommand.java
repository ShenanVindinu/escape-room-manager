package com.escaperoom.command;

import com.escaperoom.manager.VenueManager;
import com.escaperoom.cli.ConsoleView;

public class ViewRoomsCommand implements Command {

    private final ConsoleView view = new ConsoleView();

    @Override
    public void execute() {
        VenueManager venue = VenueManager.getInstance();
        view.showRooms(venue.getAllRooms());
    }

    @Override
    public String getDescription() {
        return "View all rooms and their status";
    }
}
