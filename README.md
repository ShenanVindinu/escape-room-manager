# Escape Room Booking & Session Manager

A CLI-based back-office system for running an escape room venue: booking
rooms, running live sessions, tracking cleaning turnaround, dynamic
pricing, and reporting — built as a showcase of core software design
principles (SOLID, design patterns, reflection, functional programming).

## How to run

Requires JDK 17+.

```bash
# Compile
find src -name "*.java" > sources.txt
javac -d out @sources.txt

# Run
java -cp out com.escaperoom.Main
```

The venue starts pre-seeded with 4 rooms (Asylum-1, Vault-1, Lab-1, Cove-1)
so you can use the CLI immediately. The menu is generated automatically —
see "How the CLI menu is built" below.

## What it does

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

## Design patterns used, and where

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

## Project structure

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
├── cli/            CommandLineInterface (reflection) + ConsoleIO
├── stats/          StatsService (functional programming / streams)
└── exception/      Custom checked exceptions
```
