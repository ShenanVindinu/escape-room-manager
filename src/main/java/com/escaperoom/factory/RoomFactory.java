package com.escaperoom.factory;

import com.escaperoom.model.Room;

/**
 * FACTORY METHOD PATTERN.
 *
 * Centralises the "default recipe" for each room theme (difficulty,
 * max group size) so calling code just says "give me a Bank Heist room
 * called Vault-1" without needing to know the theme's default stats.
 */
public class RoomFactory {

    public static Room createRoom(RoomType type, String roomName) {
        switch (type) {
            case HAUNTED_ASYLUM:
                return new Room(roomName, "Haunted Asylum", 4, 6);
            case BANK_HEIST:
                return new Room(roomName, "Bank Heist", 3, 8);
            case ALIEN_LAB:
                return new Room(roomName, "Alien Lab", 5, 5);
            case PIRATE_TREASURE:
                return new Room(roomName, "Pirate Treasure", 2, 10);
            default:
                throw new IllegalArgumentException("Unknown room type: " + type);
        }
    }
}
