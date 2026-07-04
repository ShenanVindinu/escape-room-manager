package com.escaperoom.cli;

import com.escaperoom.model.Room;
import com.escaperoom.model.Booking;
import com.escaperoom.stats.StatsReport;

import java.util.List;
import java.util.Map;

/**
 * Thin presentation layer. Commands decide WHAT happened (by calling
 * VenueManager/StatsService); ConsoleView decides HOW it's displayed.
 *
 * This keeps each Command focused on a single responsibility - reading
 * input and invoking domain logic - rather than also owning formatting
 * and println calls. It also means the display format could be swapped
 * (e.g. for a GUI or for tests) without touching any Command class.
 */
public class ConsoleView {

    public void showMessage(String message) {
        System.out.println("  " + message);
    }

    public void showError(String message) {
        System.out.println("  " + message);
    }

    public void showRooms(List<Room> rooms) {
        System.out.println("  Current room status:");
        for (Room room : rooms) {
            System.out.println("    " + room);
        }
    }

    public void showBooking(String prefix, Booking booking) {
        System.out.println("  " + prefix + " " + booking);
    }

    public void showSearchResults(List<Booking> results, String query) {
        if (results.isEmpty()) {
            System.out.println("  No bookings found for '" + query + "'.");
            return;
        }
        System.out.println("  Found " + results.size() + " booking(s):");
        for (Booking b : results) {
            System.out.println("    " + b);
        }
    }

    public void showStats(StatsReport report) {
        System.out.printf("  Total revenue: $%.2f%n", report.getTotalRevenue());
        System.out.println("  Most popular room: " + report.getMostPopularRoom());

        System.out.println("  Bookings per room:");
        for (Map.Entry<String, Long> entry : report.getBookingCountByRoom().entrySet()) {
            System.out.println("    " + entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("  Revenue by day:");
        report.getRevenueByDay().forEach((day, revenue) ->
                System.out.printf("    %s: $%.2f%n", day, revenue));

        if (report.getAverageSolveTimeSeconds().isPresent()) {
            System.out.printf("  Average solve time: %.0f seconds%n",
                    report.getAverageSolveTimeSeconds().getAsDouble());
        } else {
            System.out.println("  Average solve time: no completed sessions yet");
        }

        System.out.printf("  Solve rate: %.0f%%%n", report.getSolveRate() * 100);
    }
}
