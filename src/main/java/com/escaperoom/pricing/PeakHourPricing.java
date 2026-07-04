package com.escaperoom.pricing;

import com.escaperoom.model.Room;
import java.time.LocalDateTime;

/**
 * Evening slots (peak demand) cost more than daytime slots,
 * independent of which day of the week it is.
 */
public class PeakHourPricing implements PricingStrategy {
    private static final double BASE_RATE_PER_PERSON = 20.0;
    private static final int PEAK_START_HOUR = 17; // 5pm
    private static final int PEAK_END_HOUR = 21;    // 9pm
    private static final double PEAK_SURCHARGE = 1.4;

    @Override
    public double calculatePrice(Room room, int groupSize, LocalDateTime timeSlot) {
        double difficultyMultiplier = 1.0 + (room.getDifficulty() * 0.05);
        double price = BASE_RATE_PER_PERSON * groupSize * difficultyMultiplier;

        int hour = timeSlot.getHour();
        if (hour >= PEAK_START_HOUR && hour < PEAK_END_HOUR) {
            price *= PEAK_SURCHARGE;
        }
        return price;
    }

    @Override
    public String getName() {
        return "Peak Hour Pricing";
    }
}
