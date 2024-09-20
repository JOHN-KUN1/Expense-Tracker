package com.john.expensetracker.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.john.expensetracker.ROOM.Tracker
import com.john.expensetracker.Repository.TrackerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(val repository: TrackerRepository) : ViewModel() {

    val allTransactions : LiveData<List<Tracker>> = repository.allTransactions

    fun Insert(tracker: Tracker) = viewModelScope.launch(Dispatchers.IO){
        repository.Insert(tracker)
    }

    fun Update(tracker: Tracker) = viewModelScope.launch(Dispatchers.IO){
        repository.Update(tracker)
    }

    fun Delete(tracker: Tracker) = viewModelScope.launch(Dispatchers.IO){
        repository.Delete(tracker)
    }

}

class TrackerViewModelFactory(val repository: TrackerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown view model")
        }
    }


}