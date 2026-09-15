package com.example.d308vacationplanner.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface VacationDao {

    // Creates a new vacation
    @Insert
    long insert(Vacation vacation);

    // Saves changes to an existing vacation
    @Update
    int update(Vacation vacation);

    // Deletes a vacation
    @Delete
    int delete(Vacation vacation);

    // Retrieves the vacation list
    @Query("SELECT * FROM vacations ORDER BY startDate ASC")
    List<Vacation> getAllVacations();

    // Retrieves one vacation
    @Query("SELECT * FROM vacations WHERE id = :vacationId LIMIT 1")
    Vacation getVacationById(int vacationId);

    // Checks whether excursions are attached
    @Query("SELECT COUNT(*) FROM excursions WHERE vacationId = :vacationId")
    int getExcursionCount(int vacationId);
}
