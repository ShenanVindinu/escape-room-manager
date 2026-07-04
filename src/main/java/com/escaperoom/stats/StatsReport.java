package com.escaperoom.stats;

import java.time.LocalDate;
import java.util.Map;
import java.util.OptionalDouble;

/**
 * Plain data holder for a full stats snapshot. Keeps StatsService
 * focused on computing numbers (with streams) and ConsoleView focused
 * on formatting them - neither needs to know about the other's job.
 */
public class StatsReport {
    private final double totalRevenue;
    private final String mostPopularRoom;
    private final Map<String, Long> bookingCountByRoom;
    private final Map<LocalDate, Double> revenueByDay;
    private final OptionalDouble averageSolveTimeSeconds;
    private final double solveRate;

    public StatsReport(double totalRevenue, String mostPopularRoom,
                        Map<String, Long> bookingCountByRoom,
                        Map<LocalDate, Double> revenueByDay,
                        OptionalDouble averageSolveTimeSeconds,
                        double solveRate) {
        this.totalRevenue = totalRevenue;
        this.mostPopularRoom = mostPopularRoom;
        this.bookingCountByRoom = bookingCountByRoom;
        this.revenueByDay = revenueByDay;
        this.averageSolveTimeSeconds = averageSolveTimeSeconds;
        this.solveRate = solveRate;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public String getMostPopularRoom() {
        return mostPopularRoom;
    }

    public Map<String, Long> getBookingCountByRoom() {
        return bookingCountByRoom;
    }

    public Map<LocalDate, Double> getRevenueByDay() {
        return revenueByDay;
    }

    public OptionalDouble getAverageSolveTimeSeconds() {
        return averageSolveTimeSeconds;
    }

    public double getSolveRate() {
        return solveRate;
    }
}
