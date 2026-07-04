# Escape Room Booking & Session Manager 🧟⚔️🌿🍀🥬

A CLI-based back-office system for running an escape room venue: booking
rooms, running live sessions, tracking cleaning turnaround, dynamic
pricing, and reporting — built as a showcase of core software design
principles (SOLID, design patterns, reflection, functional programming).

## How to run 🏃‍➡️

Requires JDK 17+.

Simply Run **Main.java** file in your IDE (I Used IntelliJ)


## What This Project does ❔

- **Book / cancel bookings** for a room, customer, group size, and time slot
- **Run live sessions**: start a session, track hints, end it as
  solved/failed
- **Cleaning buffer**: after a session ends, the room isn't instantly
  available again — it must be marked as finished cleaning first
  (a realistic constraint that makes the State pattern meaningful)
- **Dynamic pricing**: price is calculated at booking time using whichever
  pricing strategy is currently active
- **Notifications**: key events (new booking, session started/ended, room
  available again) are broadcast to any subscribed observer
- **Reports**: revenue totals/by day, bookings per room, most popular room,
  average solve time, solve rate — all computed with Java Streams
- **Search**: find bookings by (partial, case-insensitive) customer name

## How to use it (important — read before trying it) 🐺

The exact text you type matters. This section exists so nobody gets stuck
wondering why an input "doesn't work" — in every case below, it's working
correctly and rejecting bad input on purpose (that's the point of the
validation/error handling).

### The 4 rooms that exist by default 🏡

| Room name | Theme | Difficulty | Max group size |
|---|---|---|---|
| `Asylum-1` | Haunted Asylum | 4/5 | 6 |
| `Vault-1` | Bank Heist | 3/5 | 8 |
| `Lab-1` | Alien Lab | 5/5 | 5 |
| `Cove-1` | Pirate Treasure | 2/5 | 10 |

- **Room names are case-sensitive and must match exactly.** Typing `vault-1`,
  `Vault1`, or `Hex` (anything not in the table above) will fail with
  `No such room: <what you typed>`. Use option **7 (View all rooms)** first
  if you're unsure what rooms currently exist.
- These are the *only* rooms that exist — there's no way to create a new
  room from the CLI itself (room creation only happens in code, via
  `RoomFactory`, at startup in `Main.java`).

### Date/time format is strict 📅

When prompted for a time slot, you **must** type it exactly as:

```
yyyy-MM-dd HH:mm
```

Example: `2026-07-06 18:00` (24-hour clock, dash-separated date, space
before the time, colon in the time). Typing `06/07/2026 6pm` or
`2026-07-06` (missing the time) will show:
`Invalid date/time format. Expected yyyy-MM-dd HH:mm`

### Group size must fit the room 🧑‍🤝‍🧑

Each room has a max group size (see table above). Booking `Lab-1` (max 5)
with a group size of `10` will fail with a clear error telling you the max.
Group size must also be `1` or higher.

### You need a Booking ID for options 2 and 3 (not a room name) 🆔

Options **2 (Cancel)** and **3 (End session)** both need a **Booking ID** —
NOT a room name or customer name. The Booking ID is the short code shown
in `[square brackets]` after a successful booking, e.g.:

```
Booked! [e7a52197] Shenan booked 'Vault-1' for 4 people at ... - $92.00
                    ^^^^^^^^
                    This is the Booking ID you'll need later.
```

**Write it down or scroll back to find it** — there's currently no "list
all booking IDs" view; option 5 (Search) or 7 (View rooms) don't show IDs
directly for existing bookings other than through search results.

Option **4 (Finish cleaning)** is different — it asks for a **room name**
(e.g. `Vault-1`), not a booking ID, since cleaning is tied to the room
itself rather than any specific booking.

### Rooms must go through the correct sequence — you can't skip steps 🙅‍♀️

A room follows a strict lifecycle, and trying to skip a step is *meant*
to fail:

```
AVAILABLE → (book) → BOOKED → (start session) → IN_SESSION
    ↑                                                  |
    |                                            (end session)
(finish cleaning) ← CLEANING ←————————————————————————┘
```

Concretely, this means:
- You **cannot start a session** on a room that hasn't been booked yet
  (option 6 will say *"cannot start a session - it hasn't been booked yet"*)
- You **cannot end a session** that was never started (option 3 will say
  *"no active session to end"*)
- You **cannot cancel** a booking that's already in session or already
  canceled
- After a session **ends**, the room goes to `CLEANING`, not straight back
  to `AVAILABLE` — you must run option 4 (Finish cleaning) before it can
  be booked again. This is intentional (see "Cleaning buffer" below), not
  a bug.
- **Check a room's current status anytime with option 7** if you're not
  sure what state it's in.

### Search (option 5) matches partial names, and includes canceled bookings 🔎

Typing `nadia` will match `Nadia`, `Nadia Fernando`, etc. (case-insensitive,
partial match). Canceled bookings still show up in search results, tagged
`(CANCELLED)` — this is deliberate, since search is a full booking history
lookup, not just "active" bookings.

### Quick example walkthrough (copy-paste friendly) 🥬

```
1                          → Book a room
Vault-1                    → Room name
Shenan                     → Customer name
4                          → Group size
2026-07-06 18:00           → Time slot
                            → Note the Booking ID shown, e.g. e7a52197

6                          → Start a live session
e7a52197                   → (use YOUR actual booking ID)

3                          → End a live session
e7a52197
y                          → Solved? yes

4                          → Mark cleaning finished
Vault-1

7                          → View all rooms (confirm Vault-1 is AVAILABLE again)

8                          → View statistics and reports
```

## Design patterns used, and where 📏

| Pattern | Class(es) | Why it's there |
|---|---|---|
| **Singleton** | `manager.VenueManager` | Single shared source of truth for all rooms/bookings/sessions |
| **Factory Method** | `factory.RoomFactory`, `factory.RoomType` | Creates pre-configured rooms per theme without callers knowing the defaults |
| **Strategy** | `pricing.PricingStrategy` + 4 implementations | Interchangeable pricing rules, swappable at runtime via `VenueManager.setPricingStrategy()` |
| **State** | `state.RoomState` + `AvailableState`/`BookedState`/`InSessionState`/`CleaningState` | Room legality rules (what can happen next) live in the state, not in `if/else` chains inside `Room` |
| **Observer** | `observer.VenueEventPublisher`, `observer.VenueObserver`, `observer.StaffNotifier` | Decouples "something happened" from "who reacts to it" |
| **Command** | `command.Command` + 8 concrete commands | Every CLI action is an object; enables the reflection-based menu below, and each command self-contains its own input handling |

### Reflection 

`cli.CommandLineInterface` does **not** hardcode a menu. At startup it
scans the `com.escaperoom.command` package on the classpath, finds every
class implementing `Command`, and instantiates each one via
`Class.getDeclaredConstructor().newInstance()`. To add a new CLI action,
you only need to add one new class implementing `Command` — nothing in
the CLI itself changes.

### Presentation separated from control flow

Commands don't call `System.out.println` directly. Each one calls
`cli.ConsoleView`, which owns all formatting/display logic. `StatsService`
returns a `StatsReport` value object rather than pre-formatted strings, so
the "compute the numbers" and "display the numbers" responsibilities stay
separate (this isn't full MVC - there's no independent, self-updating View
component reacting to Model change events - but it gives the same core
benefit: presentation logic is isolated from control flow, and it means
Commands could be unit-tested without capturing stdout).

### Functional programming

`stats.StatsService` does all its reporting with Streams/lambdas —
`filter`, `map`, `collect(Collectors.groupingBy(...))`, `mapToDouble`,
`average()` — rather than manual loops. See `getRevenueByDay()` and
`getBookingCountByRoom()` for the clearest examples.

## SOLID in this project

- **S**ingle Responsibility — `Room` only knows how to delegate to its
  state; pricing math lives entirely in `pricing.*`; reporting lives
  entirely in `StatsService`
- **O**pen/Closed — new room themes (`RoomFactory`), new pricing rules
  (`PricingStrategy`), and new CLI actions (`Command`) can all be added
  without modifying existing classes
- **L**iskov Substitution — any `RoomState`, `PricingStrategy`, or
  `Command` implementation can be swapped in without breaking callers
- **I**nterface Segregation — `Command`, `RoomState`, `PricingStrategy`,
  `VenueObserver` are all small, single-purpose interfaces
- **D**ependency Inversion — `VenueManager` depends on the `PricingStrategy`
  and `VenueObserver` interfaces, never on concrete implementations

## Project structure 🐫

```
src/main/java/com/escaperoom/
├── Main.java
├── model/          Room, Booking, Session, Customer
├── state/          State pattern (room lifecycle)
├── factory/        Factory Method (room creation)
├── pricing/        Strategy pattern (pricing rules)
├── observer/       Observer pattern (event notifications)
├── command/        Command pattern (CLI actions)
├── manager/        VenueManager (Singleton)
├── cli/            CommandLineInterface (reflection) + ConsoleIO + ConsoleView
├── stats/          StatsService + StatsReport (functional programming / streams)
└── exception/      Custom checked exceptions
```

## Troubleshooting 🫎

**Menu shows "(0 commands auto-discovered via reflection)" and only option
0 (Exit) appears.**

This means the reflection-based scan in `CommandLineInterface` couldn't
find the compiled `Command` classes on the classpath. Known cause: if the
project sits under a folder path containing spaces (e.g. `OneDrive`,
`Program Files`, or any multi-word folder name), an older version of this
code could fail to resolve the path correctly. This is already fixed in
the current version (`CommandLineInterface.discoverCommands()` uses
`resource.toURI()`), so if you see this, make sure you're running the
latest compiled classes — do a full **Rebuild Project** in your IDE, or
delete the `out`/`target` folder and recompile from scratch.
