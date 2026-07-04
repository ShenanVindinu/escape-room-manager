package com.escaperoom.factory;

/**
 * The catalogue of themed room templates the venue offers.
 * Adding a new type here + a branch in RoomFactory is the only
 * change needed to introduce a new theme - nothing else in the
 * system needs to know about it (Open/Closed in action).
 */
public enum RoomType {
    HAUNTED_ASYLUM,
    BANK_HEIST,
    ALIEN_LAB,
    PIRATE_TREASURE
}
