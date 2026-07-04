package com.escaperoom.stats;

import com.escaperoom.model.Booking;
import com.escaperoom.model.Session;
import com.escaperoom.manager.VenueManager;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * FUNCTIONAL PROGRAMMING SHOWCASE.
 *
 * All reporting is done with Streams/Lambdas rather than hand-rolled
 * loops - this is the natural place in the project to demonstrate
 * filter/map/reduce/collect/groupingBy from the FP lectures.
 */
public class StatsService {

    private final VenueManager venue;

    public StatsService(VenueManager venue) {
        this.venue = venue;
    }

    /** Total revenue across all non-canceled bookings. */
    public double getTotalRevenue() {
        return venue.getAllBookings().stream()
                .filter(b -> !b.isCancelled())
                .mapToDouble(Booking::getPrice)
                .sum();
    }

    /** Revenue grouped by calendar day of the booking's time slot. */
    public Map<LocalDate, Double> getRevenueByDay() {
        return venue.getAllBookings().stream()
                .filter(b -> !b.isCancelled())
                .collect(Collectors.groupingBy(
                        b -> b.getTimeSlot().toLocalDate(),
                        Collectors.summingDouble(Booking::getPrice)));
    }

    /** Which room has been booked the most times. */
    public Map<String, Long> getBookingCountByRoom() {
        return venue.getAllBookings().stream()
                .filter(b -> !b.isCancelled())
                .collect(Collectors.groupingBy(
                        b -> b.getRoom().getName(),
                        Collectors.counting()));
    }

    /** Average solve time (seconds) across all completed sessions. */
    public OptionalDouble getAverageSolveTimeSeconds() {
        return venue.getAllSessions().stream()
                .filter(s -> s.getEndTime() != null)
                .mapToLong(Session::getDurationSeconds)
                .average();
    }

    /** Fraction of finished sessions that were actually solved (0.0 - 1.0). */
    public double getSolveRate() {
        List<Session> finished = venue.getAllSessions().stream()
                .filter(s -> s.getEndTime() != null)
                .collect(Collectors.toList());

        if (finished.isEmpty()) return 0.0;

        long solvedCount = finished.stream().filter(Session::isSolved).count();
        return (double) solvedCount / finished.size();
    }

    /** Bookings for a given customer name (case-insensitive, partial match). */
    public List<Booking> searchByCustomerName(String query) {
        String lower = query.toLowerCase();
        return venue.getAllBookings().stream()
                .filter(b -> b.getCustomer().getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    /** The single most popular room by booking count, if any exist. */
    public String getMostPopularRoom() {
        return getBookingCountByRoom().entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse("No bookings yet");
    }

    /** Bundles every computed stat into one snapshot for the view layer to display. */
    public StatsReport generateReport() {
        return new StatsReport(
                getTotalRevenue(),
                getMostPopularRoom(),
                getBookingCountByRoom(),
                getRevenueByDay(),
                getAverageSolveTimeSeconds(),
                getSolveRate());
    }
}
