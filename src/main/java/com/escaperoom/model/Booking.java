package com.escaperoom.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A booking ties a Customer to a Room for a specific time slot,
 * and records the price calculated at booking time (via a PricingStrategy
 * chosen elsewhere - Booking itself doesn't know how price was worked out).
 */
public class Booking {
    private final String id;
    private final Room room;
    private final Customer customer;
    private final int groupSize;
    private final LocalDateTime timeSlot;
    private final double price;
    private boolean cancelled = false;

    public Booking(Room room, Customer customer, int groupSize,
                    LocalDateTime timeSlot, double price) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.room = room;
        this.customer = customer;
        this.groupSize = groupSize;
        this.timeSlot = timeSlot;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public LocalDateTime getTimeSlot() {
        return timeSlot;
    }

    public double getPrice() {
        return price;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void markCancelled() {
        this.cancelled = true;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s booked '%s' for %d people at %s - $%.2f%s",
                id, customer.getName(), room.getName(), groupSize, timeSlot,
                price, cancelled ? " (CANCELLED)" : "");
    }
}
