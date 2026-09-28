package com.example.d308vacationplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

// The tables in the application database
@Database(
        entities = {Vacation.class, Excursion.class},
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract VacationDao vacationDao();

    public abstract ExcursionDao excursionDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "vacation_planner_database"
                            )
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE vacations ADD COLUMN startAlert INTEGER NOT NULL DEFAULT 0"
            );

            database.execSQL(
                    "ALTER TABLE vacations ADD COLUMN endAlert INTEGER NOT NULL DEFAULT 0"
            );
        }
    };
}
