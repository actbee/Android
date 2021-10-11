package com.example.xuedan_zou_myrun2

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ExerciseEntryViewModel(private val repository: ExerciseEntryRepository): ViewModel() {
    fun insert(exerciseEntry:ExerciseEntry){
        repository.insert(exerciseEntry)
    }

    fun delete(id: Long){
        repository.delete(id)
    }
}

// view model factory is almost the same as the sample code
class ExerciseEntryViewModelFactory(private val repository: ExerciseEntryRepository) : ViewModelProvider.Factory{
    override fun<T: ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(ExerciseEntryViewModel::class.java))
            return ExerciseEntryViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel Class!")
    }
}