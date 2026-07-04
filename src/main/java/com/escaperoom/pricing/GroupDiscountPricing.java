package com.escaperoom.pricing;

import com.escaperoom.model.Room;
import java.time.LocalDateTime;

/**
 * Rewards bigger groups with a per-person discount -
 * useful for venues that want to encourage larger bookings.
 */
public class GroupDiscountPricing implements PricingStrategy {
    private static final double BASE_RATE_PER_PERSON = 20.0;

    @Override
    public double calculatePrice(Room room, int groupSize, LocalDateTime timeSlot) {
        double difficultyMultiplier = 1.0 + (room.getDifficulty() * 0.05);
        double perPersonRate = BASE_RATE_PER_PERSON;

        if (groupSize >= 8) {
            perPersonRate *= 0.75; // 25% off for big groups
        } else if (groupSize >= 5) {
            perPersonRate *= 0.90; // 10% off for medium groups
        }

        return perPersonRate * groupSize * difficultyMultiplier;
    }

    @Override
    public String getName() {
        return "Group Discount";
    }
}
