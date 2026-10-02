# Travel Planner -- Software Engineering Capstone

A native Android vacation and itinerary management application built
with Java, Room, and SQLite. The application allows users to organize
vacations and excursions, search stored vacation records, generate an
itinerary report, configure vacation alerts, and share vacation
information.

## Overview

The Travel Planner was developed as a software engineering capstone
based on an existing Android vacation-planning application. The project
focuses on extending the application with database-backed search,
reporting, validation, object-oriented design, automated testing,
configurable alerts, and release deployment.

The application stores vacation and excursion data locally using Room
over SQLite, so its core functionality does not require a cloud database
or application server.

## Features

-   Create, view, update, and delete vacations
-   Add, edit, and delete excursions associated with a vacation
-   Store vacation title, lodging, start date, and end date
-   Validate vacation titles, lodging information, and dates
-   Validate excursion dates against the associated vacation period
-   Search vacations by title or hotel
-   Return multiple matching vacation records from a database search
-   Generate an on-screen itinerary report
-   Display report generation date and time
-   Display vacation, hotel, dates, and excursion-count information in
    report columns
-   Configure vacation start and end notifications
-   Schedule Android alarms for selected vacation alerts
-   Request notification permission on supported Android versions
-   Share vacation information using Android sharing functionality
-   Protect vacation deletion when associated excursions exist
-   Persist data locally with Room/SQLite

## Technical Highlights

### Object-Oriented Design

The application uses an abstract `TravelItem` class as a common
abstraction for travel records. `Vacation` and `Excursion` extend
`TravelItem` and provide their own implementations of item type and
display date behavior.

A `TravelItemFormatter` provides reusable formatting based on the shared
abstraction.

### Database Design

Room provides the persistence layer over SQLite.

The database contains:

-   `Vacation`
-   `Excursion`

Excursions store a `vacationId` so that each excursion is associated
with its parent vacation.

The application uses DAO interfaces for database operations rather than
placing SQL operations directly in the activities.

### Search

Vacation searches are implemented through `VacationDao`. The search
checks both vacation title and hotel fields and returns all matching
records ordered by start date.

``` sql
SELECT * FROM vacations
WHERE title LIKE '%' || :searchText || '%'
   OR hotel LIKE '%' || :searchText || '%'
ORDER BY startDate ASC
```

### Reporting

The application generates a consolidated vacation report through a Room
query and `VacationReportRow` projection.

The report includes:

-   Vacation
-   Hotel
-   Start Date
-   End Date
-   Excursion Count

The report also displays the date and time at which it was generated.

### Validation

Validation logic is centralized in the final `ValidationUtils` utility
class.

Validation includes:

-   Strict `MM/dd/yyyy` date validation
-   Vacation and excursion date parsing
-   Required title validation
-   Maximum title length of 100 characters
-   Required location/hotel validation
-   Maximum location length of 100 characters

### Alerts

Vacation start and end alerts are stored with each vacation and
scheduled through Android `AlarmManager` and broadcast receivers.

The project includes:

-   `VacationAlertReceiver`
-   `ExcursionAlertReceiver`
-   Notification permission handling
-   Separate alert settings for vacation start and end dates

## Testing

JUnit tests are included for reusable validation logic.

Current unit-test coverage includes:

-   Acceptance of valid dates
-   Rejection of invalid dates
-   Acceptance of valid vacation titles
-   Rejection of titles exceeding the maximum length

The project also includes an Android instrumented test configuration
using AndroidX Test and Espresso.

## Technologies

| Technology | Purpose | 
| -------- | -------- | 
| Java 11 | Application development | 
| Android SDK | Native Android application platform |
| Android Studio | Development environment |
| Room 2.6.1 | Persistence layer |
| SQLite | Local database |
| JUnit | Unit testing |
| Gradle | Build automation |
| Git / GitHub | Version control and source hosting |

## Project Structure

``` text
app/
└── src/
    ├── main/
    │   ├── java/com/example/d308vacationplanner/
    │   │   ├── adapter/
    │   │   ├── database/
    │   │   ├── MainActivity.java
    │   │   ├── VacationListActivity.java
    │   │   ├── VacationDetailActivity.java
    │   │   ├── ExcursionDetailActivity.java
    │   │   ├── ReportActivity.java
    │   │   ├── ValidationUtils.java
    │   │   ├── TravelItemFormatter.java
    │   │   └── *AlertReceiver.java
    │   └── res/
    ├── test/
    └── androidTest/
```

## Getting Started

### Requirements

-   Android Studio
-   Java 11
-   Android SDK
-   Android emulator or compatible Android device

### Build

Clone the repository and open it in Android Studio.

Then allow Gradle to synchronize the project and run the application on
an emulator or compatible Android device.

The project uses:

``` text
minSdk 26
targetSdk 37
Room 2.6.1
```

## Release Deployment

The completed application was built as a signed Android APK and deployed
through a GitHub Pages project site.

[Travel Planner
Deployment](https://lanebrents.github.io/travel-planner-deployment/)

## Engineering Focus

This project demonstrates practical software engineering work across:

-   Android application development
-   Object-oriented programming
-   Local relational data persistence
-   DAO-based database access
-   Input validation
-   Automated testing
-   Search and reporting
-   Android notifications and alarms
-   Regression-focused enhancement of an existing application
-   Production APK creation and deployment

## Author

**Lane Brents**
