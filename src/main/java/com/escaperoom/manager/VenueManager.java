package com.escaperoom.manager;

import com.escaperoom.model.Room;
import com.escaperoom.model.Booking;
import com.escaperoom.model.Customer;
import com.escaperoom.model.Session;
import com.escaperoom.pricing.PricingStrategy;
import com.escaperoom.pricing.WeekdayPricing;
import com.escaperoom.observer.VenueEventPublisher;
import com.escaperoom.observer.StaffNotifier;
import com.escaperoom.exception.RoomNotAvailableException;
import com.escaperoom.exception.InvalidBookingException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SINGLETON PATTERN.
 *
 * The single source of truth for all rooms, bookings and sessions.
 * Everything else in the system (Commands, CLI, StatsService) talks
 * to the venue through this one shared instance rather than passing
 * a web of objects around.
 */
public class VenueManager {
    private static VenueManager instance;

    private final Map<String, Room> rooms = new HashMap<>();
    private final List<Booking> bookings = new ArrayList<>();
    private final List<Session> sessions = new ArrayList<>();
    private final Map<String, Session> activeSessions = new HashMap<>(); // bookingId -> Session

    private final VenueEventPublisher eventPublisher = new VenueEventPublisher();
    private PricingStrategy pricingStrategy = new WeekdayPricing(); // sensible default

    private VenueManager() {
        eventPublisher.subscribe(new StaffNotifier());
    }

    public static synchronized VenueManager getInstance() {
        if (instance == null) {
            instance = new VenueManager();
        }
        return instance;
    }

    // --- Room management ---

    public void addRoom(Room room) {
        rooms.put(room.getName(), room);
    }

    public Optional<Room> findRoom(String name) {
        return Optional.ofNullable(rooms.get(name));
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    // --- Pricing strategy (swap at runtime) ---

    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
        eventPublisher.publish("Pricing strategy switched to: " + strategy.getName());
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    // --- Booking workflow ---

    public Booking bookRoom(String roomName, String customerName, int groupSize,
                             LocalDateTime timeSlot)
            throws RoomNotAvailableException, InvalidBookingException {

        Room room = findRoom(roomName)
                .orElseThrow(() -> new InvalidBookingException("No such room: " + roomName));

        if (groupSize < 1 || groupSize > room.getMaxGroupSize()) {
            throw new InvalidBookingException(
                    "Group size " + groupSize + " invalid for " + roomName
                            + " (max " + room.getMaxGroupSize() + ")");
        }

        room.book(); // delegates to RoomState - throws if not Available

        double price = pricingStrategy.calculatePrice(room, groupSize, timeSlot);
        Booking booking = new Booking(room, new Customer(customerName), groupSize, timeSlot, price);
        bookings.add(booking);

        eventPublisher.publish("New booking: " + booking);
        return booking;
    }

    public void cancelBooking(String bookingId) throws RoomNotAvailableException, InvalidBookingException {
        Booking booking = findBookingById(bookingId);
        booking.getRoom().cancelBooking(); // delegates to RoomState
        booking.markCancelled();
        eventPublisher.publish("Booking cancelled: " + booking.getId() + " (" + booking.getRoom().getName() + " is available again)");
    }

    public Session startSession(String bookingId) throws RoomNotAvailableException, InvalidBookingException {
        Booking booking = findBookingById(bookingId);
        booking.getRoom().startSession(); // delegates to RoomState

        Session session = new Session(booking);
        sessions.add(session);
        activeSessions.put(bookingId, session);

        eventPublisher.publish("Session started for booking " + bookingId
                + " in " + booking.getRoom().getName());
        return session;
    }

    public Session endSession(String bookingId, boolean solved) throws RoomNotAvailableException, InvalidBookingException {
        Session session = activeSessions.remove(bookingId);
        if (session == null) {
            throw new InvalidBookingException("No active session for booking " + bookingId);
        }
        Room room = session.getRoom();
        room.endSession(); // delegates to RoomState -> moves to Cleaning
        session.finish(solved);

        eventPublisher.publish(room.getName() + " session ended ("
                + (solved ? "SOLVED" : "FAILED") + ") - room now cleaning");
        return session;
    }

    public void finishCleaning(String roomName) throws RoomNotAvailableException, InvalidBookingException {
        Room room = findRoom(roomName)
                .orElseThrow(() -> new InvalidBookingException("No such room: " + roomName));
        room.finishCleaning();
        eventPublisher.publish(room.getName() + " has finished cleaning and is now AVAILABLE");
    }

    // --- Lookups used by commands/stats ---

    public Booking findBookingById(String bookingId) throws InvalidBookingException {
        return bookings.stream()
                .filter(b -> b.getId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new InvalidBookingException("No booking with id " + bookingId));
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public List<Session> getAllSessions() {
        return new ArrayList<>(sessions);
    }

    public VenueEventPublisher getEventPublisher() {
        return eventPublisher;
    }
}
