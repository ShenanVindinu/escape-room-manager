package com.escaperoom.model;

/**
 * Simple value object representing the person making a booking.
 */
public class Customer {
    private final String name;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
