# D308 Vacation Planner

## Purpose

The D308 Vacation Planner is an Android application designed to help users organize and manage vacations and their associated excursions. Users can create, view, update, and delete vacations, manage excursions for each vacation, validate dates, receive vacation and excursion reminders, and share vacation information.

The application uses a Room database to store vacation and excursion information locally on the device.

## How to Operate the Application

### Home Screen

When the application starts, the Home screen is displayed.

1. Select **View Vacations** to open the Vacation List.
2. From the Vacation List, select an existing vacation to view or edit it.
3. Select **Add Vacation** to create a new vacation.

### Vacation Management — B1

From the Vacation List:

1. Select **Add Vacation** to create a vacation.
2. Enter the vacation title, hotel/place, start date, and end date.
3. Select **Save Vacation** to store the vacation.
4. Select an existing vacation to update its information.
5. Select **Delete Vacation** to remove a vacation.
6. A vacation cannot be deleted while it has associated excursions. Delete its excursions first and then delete the vacation.
7. Multiple vacations can be created and stored.

### Vacation Details — B2

Each vacation contains:

- Vacation title
- Hotel or place
- Start date
- End date

Dates are entered using the `MM/dd/yyyy` format.

### Vacation Features — B3

From the Vacation Detail screen:

1. Create, update, or delete vacation information.
2. Enter dates using the `MM/dd/yyyy` format.
3. The application rejects invalid dates.
4. The end date must occur after the start date.
5. Save a vacation to schedule start and end date reminders.
6. Use **Share Vacation** to share the vacation details through an available sharing application, such as email, messaging, or another supported sharing option.
7. View the excursions associated with the vacation.
8. Select **Add Excursion** to create an excursion for the vacation.
9. Select an existing excursion to view or edit it.

Vacation reminders identify the vacation and whether the reminder is for the start or end of the vacation.

### Excursion Details — B4

Each excursion contains:

- Excursion title
- Excursion date

Excursions are displayed on the Vacation Detail screen for their associated vacation.

### Excursion Features — B5

From the Excursion Detail screen:

1. Create a new excursion.
2. Enter an excursion title and date.
3. Save the excursion.
4. Select an existing excursion to update it.
5. Delete an excursion when it is no longer needed.
6. Dates must use the `MM/dd/yyyy` format.
7. Invalid dates are rejected.
8. An excursion date must fall on or between the associated vacation's start and end dates.
9. An excursion cannot be saved if its date occurs before the vacation begins or after the vacation ends.
10. Save an excursion to schedule a reminder for the excursion date.

Excursion reminders identify the excursion when the reminder is displayed.

## Android Version

The signed APK is compatible with **Android 8.0 (API level 26) and newer**.

The application is configured with:

- Minimum SDK: Android 8.0 (API 26)
- Target SDK: Android API 37
- Compile SDK: Android API 37

## Git Repository

The source code, project history, and signed APK files are available in the GitLab repository:

[D308 Vacation Planner GitLab Repository](https://gitlab.com/wgu-gitlab-environment/student-repos/lbrent4/d308-mobile-application-development-android/-/tree/Working_Branch?ref_type=heads)