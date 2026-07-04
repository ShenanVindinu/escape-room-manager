package com.escaperoom.command;

import com.escaperoom.manager.VenueManager;
import com.escaperoom.model.Booking;
import com.escaperoom.stats.StatsService;
import com.escaperoom.cli.ConsoleIO;
import com.escaperoom.cli.ConsoleView;

import java.util.List;

public class SearchBookingsCommand implements Command {

    private final ConsoleView view = new ConsoleView();

    @Override
    public void execute() {
        StatsService stats = new StatsService(VenueManager.getInstance());
        String query = ConsoleIO.prompt("Search by customer name: ");

        List<Booking> results = stats.searchByCustomerName(query);
        view.showSearchResults(results, query);
    }

    @Override
    public String getDescription() {
        return "Search bookings by customer name";
    }
}
