package com.escaperoom.model;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents a live escape-room attempt tied to a Booking.
 * Tracks timing, hints used, and the outcome, so StatsService
 * can later crunch this data with streams.
 */
public class Session {
    private final Booking booking;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private int hintsUsed = 0;
    private boolean solved = false;

    public Session(Booking booking) {
        this.booking = booking;
        this.startTime = LocalDateTime.now();
    }

    public void useHint() {
        hintsUsed++;
    }

    public void finish(boolean solved) {
        this.endTime = LocalDateTime.now();
        this.solved = solved;
    }

    public Booking getBooking() {
        return booking;
    }

    public Room getRoom() {
        return booking.getRoom();
    }

    public int getHintsUsed() {
        return hintsUsed;
    }

    public boolean isSolved() {
        return solved;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    /** Solve duration in seconds - used for average solve time stats. */
    public long getDurationSeconds() {
        if (endTime == null) return 0;
        return Duration.between(startTime, endTime).getSeconds();
    }

    @Override
    public String toString() {
        String outcome = endTime == null ? "IN PROGRESS"
                : (solved ? "SOLVED" : "FAILED") + " in " + getDurationSeconds() + "s";
        return String.format("Session[%s] room=%s hints=%d -> %s",
                booking.getId(), booking.getRoom().getName(), hintsUsed, outcome);
    }
}
