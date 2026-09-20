# Attendix

Attendix is an Android attendance management application built with **Kotlin and Jetpack Compose**. It is designed to help manage student attendance through a clean, structured, and locally persistent Android application.

The project demonstrates practical Android development using **Jetpack Compose, MVVM, Room Database, Repository Pattern, Kotlin Coroutines, and Navigation**.

## Features

- Student attendance management
- Faculty-oriented attendance workflow
- Login and session management
- Dashboard for attendance information
- Mark and manage attendance
- Local persistence using Room Database
- Offline access to stored attendance data
- Compose-based UI
- Multi-screen navigation
- State-driven UI
- MVVM architecture
- Repository-based data access
- Notification support

## Tech Stack

| Technology | Usage |
|---|---|
| **Kotlin** | Primary programming language |
| **Jetpack Compose** | UI development |
| **Material 3** | UI components and theming |
| **MVVM** | Application architecture |
| **Room Database** | Local data persistence |
| **SQLite** | Local database underlying Room |
| **Navigation Compose** | Screen navigation |
| **Kotlin Coroutines** | Asynchronous database operations |
| **ViewModel** | UI state and business actions |
| **Gradle Kotlin DSL** | Build configuration |
| **Android Studio** | Development environment |

## Architecture

Attendix follows an **MVVM-based layered architecture** with a separate data layer.

```text
┌────────────────────────────────────┐
│               UI                   │
│          Jetpack Compose           │
│                                    │
│  Screens • Components • Navigation │
└─────────────────┬──────────────────┘
                  │
                  │ UI Events / State
                  ▼
┌────────────────────────────────────┐
│            ViewModels              │
│                                    │
│     Screen State & Business        │
│             Actions                │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│            Repository              │
│                                    │
│       Data-access interface        │
└─────────────────┬──────────────────┘
                  │
                  ▼
┌────────────────────────────────────┐
│               Data                 │
│                                    │
│  Room Database • DAO • Repository  │
│        Implementations             │
└─────────────────┬──────────────────┘
                  │
                  ▼
             SQLite Database
```

The UI is responsible for displaying state and sending user events. ViewModels handle screen state and business actions, while repositories provide a clean boundary between the application logic and data sources.

## Project Structure

The project contains a single Android application module.

```text
AttendixAPP/
├── Gradle / project configuration
│
├── app/                              ← Android application module
│   ├── build.gradle.kts              ← Android configuration + dependencies
│   │
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml   ← App registration and permissions
│       │   │
│       │   ├── java/com/arpit/attendixapp/
│       │   │   ├── MainActivity.kt
│       │   │   ├── AttendixApplication.kt
│       │   │   │
│       │   │   ├── ui/               ← Compose UI and navigation
│       │   │   ├── viewmodels/       ← Screen state and business actions
│       │   │   ├── repository/       ← Data-access interfaces
│       │   │   ├── data/             ← Room database and implementations
│       │   │   ├── model/            ← Application/domain data classes
│       │   │   └── util/             ← Session and notification helpers
│       │   │
│       │   └── res/                  ← Android resources
│       │
│       ├── test/                     ← Local JVM unit tests
│       │
│       └── androidTest/              ← Instrumented/device tests
│
└── gradle/                           ← Gradle Wrapper and version catalog
```

## Data Layer

Attendix separates data-access responsibilities from the UI layer.

The data flow follows:

```text
Compose UI
    ↓
ViewModel
    ↓
Repository
    ↓
Room / DAO
    ↓
SQLite
```

The repository layer provides an abstraction between ViewModels and the underlying data implementation.

This structure makes the application easier to maintain because UI components do not directly depend on database implementation details.

## Room Database

Attendix uses **Room Database** for local persistence.

Attendance-related information is stored locally so that the application can retain records beyond the lifetime of a particular screen or application session.

Room provides an abstraction layer over SQLite and allows Android applications to work with structured local data.

### Main Room Components

```text
Entity
  ↓
DAO
  ↓
Room Database
  ↓
Repository Implementation
  ↓
ViewModel
  ↓
Compose UI
```

This allows attendance operations to remain separated from the presentation layer.

## State Management

ViewModels are responsible for managing screen state and handling business actions.

The general interaction is:

```text
User Interaction
       ↓
Compose Screen
       ↓
ViewModel Action
       ↓
Repository
       ↓
Database
       ↓
Updated State
       ↓
Compose UI
```

Compose then reacts to the updated state and recomposes the relevant UI.

## Session Management

Attendix contains utility components for managing application sessions.

Session-related responsibilities are kept outside the UI layer, helping screens remain focused on presentation and user interaction.

## Notifications

The project also contains notification-related utilities for handling application notifications.

Notification logic is separated into the `util` layer rather than being embedded directly inside individual Compose screens.

## Testing

The project contains two testing areas:

### Unit Tests

Located under:

```text
app/src/test/
```

These are local JVM tests for testing application logic without requiring a physical Android device.

### Instrumented Tests

Located under:

```text
app/src/androidTest/
```

These tests run in an Android environment and can be used for testing Android-specific functionality and UI behavior.

## Key Android Concepts Demonstrated

Attendix demonstrates practical implementation of:

- Kotlin
- Jetpack Compose
- Material 3
- MVVM architecture
- ViewModel
- UI state management
- State-driven UI
- Navigation Compose
- Room Database
- SQLite
- DAO
- Repository Pattern
- Kotlin Coroutines
- Local data persistence
- Session management
- Android notifications
- Unit testing
- Instrumented testing
- Separation of concerns

## Offline-First Approach

One of the core goals of Attendix is reliable local attendance management.

Instead of depending entirely on temporary application state, attendance information is persisted using Room Database.

This means previously stored attendance information can remain available even after the application is closed and reopened.

## Why I Built Attendix

Attendix was built as a practical Android application to explore how a real-world attendance management workflow can be implemented using modern Android development practices.

The project helped me understand how to combine:

- Compose UI
- ViewModel state management
- Repository architecture
- Room persistence
- Local database operations
- Navigation
- Session handling
- Notifications

into a single Android application.

## Future Improvements

Potential improvements include:

- Cloud synchronization
- Improved authentication
- Attendance analytics and statistics
- Attendance report generation
- Exporting attendance data
- More comprehensive automated testing
- Dependency injection with Hilt
- Improved adaptive UI

## Requirements

- Android Studio
- Android SDK
- JDK compatible with the project's Gradle/Android Gradle Plugin configuration
- Android device or emulator

## Getting Started

Clone the repository:

```bash
git clone https://github.com/Arpitsavaliya19/Attendix.git
```

Open the project in **Android Studio**.

Allow Gradle to sync the project, then run the application on an Android emulator or physical Android device.

## Project Status

**Completed**

Attendix is an Android portfolio project demonstrating practical application development with Kotlin, Jetpack Compose, MVVM, Room Database, and local data persistence.

## Author

**Arpit Savaliya**

Android Developer | Kotlin | Jetpack Compose

---

### Technologies

`Kotlin` `Jetpack Compose` `Material 3` `MVVM` `Room` `SQLite` `ViewModel` `Coroutines` `Navigation Compose` `Android`
