package com.example.xuedan_zou_myrun2

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseEntryDatabaseDao {
    @Insert
    suspend fun insert_value(exercise_entry: ExerciseEntry)

    @Query("DELETE FROM exercise_entry_table WHERE id = :key")
    suspend fun delete_value(key: Long)
}