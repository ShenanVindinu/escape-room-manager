package com.escaperoom;

import com.escaperoom.manager.VenueManager;
import com.escaperoom.factory.RoomFactory;
import com.escaperoom.factory.RoomType;
import com.escaperoom.cli.CommandLineInterface;

public class Main {
    public static void main(String[] args) {
        seedVenue();

        CommandLineInterface cli = new CommandLineInterface();
        cli.run();
    }

    /** Populates the venue with a few rooms so the CLI is usable immediately. */
    private static void seedVenue() {
        VenueManager venue = VenueManager.getInstance();
        venue.addRoom(RoomFactory.createRoom(RoomType.HAUNTED_ASYLUM, "Asylum-1"));
        venue.addRoom(RoomFactory.createRoom(RoomType.BANK_HEIST, "Vault-1"));
        venue.addRoom(RoomFactory.createRoom(RoomType.ALIEN_LAB, "Lab-1"));
        venue.addRoom(RoomFactory.createRoom(RoomType.PIRATE_TREASURE, "Cove-1"));
    }
}
