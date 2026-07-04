package com.escaperoom.pricing;

import com.escaperoom.model.Room;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Weekends cost more across the board, regardless of time of day.
 */
public class WeekendPricing implements PricingStrategy {
    private static final double BASE_RATE_PER_PERSON = 20.0;
    private static final double WEEKEND_SURCHARGE = 1.25;

    @Override
    public double calculatePrice(Room room, int groupSize, LocalDateTime timeSlot) {
        double difficultyMultiplier = 1.0 + (room.getDifficulty() * 0.05);
        double price = BASE_RATE_PER_PERSON * groupSize * difficultyMultiplier;

        DayOfWeek day = timeSlot.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            price *= WEEKEND_SURCHARGE;
        }
        return price;
    }

    @Override
    public String getName() {
        return "Weekend Surcharge";
    }
}
