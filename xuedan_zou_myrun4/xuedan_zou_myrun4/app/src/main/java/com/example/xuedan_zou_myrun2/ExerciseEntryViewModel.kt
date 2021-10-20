package com.example.xuedan_zou_myrun2

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ExerciseEntryViewModel(private val repository: ExerciseEntryRepository): ViewModel() {

    val allExerciseEntryLiveData : LiveData<List<ExerciseEntry>> = repository.allExerciseEntry.asLiveData()

    fun insert(exerciseEntry:ExerciseEntry){
        repository.insert(exerciseEntry)
    }

    fun delete(id: Long){
        repository.delete(id)
    }

    /*
    // try to get the id of the last database but seems no use
    fun getID():Long{
        val exerciseEntryList = allExerciseEntryLiveData.value
        if (exerciseEntryList != null && exerciseEntryList.size > 0) {
            val id = exerciseEntryList[exerciseEntryList.size - 1].id
            return id
        }
        return -1
    }
     */

}

// view model factory is almost the same as the sample code
class ExerciseEntryViewModelFactory(private val repository: ExerciseEntryRepository) : ViewModelProvider.Factory{
    override fun<T: ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(ExerciseEntryViewModel::class.java))
            return ExerciseEntryViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel Class!")
    }
}