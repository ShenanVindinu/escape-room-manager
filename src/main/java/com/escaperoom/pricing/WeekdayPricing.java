package com.escaperoom.pricing;

import com.escaperoom.model.Room;
import java.time.LocalDateTime;

/**
 * Simple flat per-person rate, scaled slightly by difficulty.
 * Used as the baseline strategy for weekday bookings.
 */
public class WeekdayPricing implements PricingStrategy {
    private static final double BASE_RATE_PER_PERSON = 20.0;

    @Override
    public double calculatePrice(Room room, int groupSize, LocalDateTime timeSlot) {
        double difficultyMultiplier = 1.0 + (room.getDifficulty() * 0.05);
        return BASE_RATE_PER_PERSON * groupSize * difficultyMultiplier;
    }

    @Override
    public String getName() {
        return "Weekday Flat Rate";
    }
}
