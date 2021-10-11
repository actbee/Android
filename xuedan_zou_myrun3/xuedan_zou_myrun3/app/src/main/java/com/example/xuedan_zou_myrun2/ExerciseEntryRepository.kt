package com.example.xuedan_zou_myrun2

import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExerciseEntryRepository(private val exerciseEntryDatabaseDao: ExerciseEntryDatabaseDao) {

    val allExerciseEntry: Flow<List<ExerciseEntry>> = exerciseEntryDatabaseDao.getAll()

    fun insert(exerciseEntry: ExerciseEntry){
        CoroutineScope(IO).launch{
            exerciseEntryDatabaseDao.insert_value(exerciseEntry)
        }
    }

    fun delete(id: Long){
        CoroutineScope(IO).launch{
            exerciseEntryDatabaseDao.delete_value(id)
        }
    }
}