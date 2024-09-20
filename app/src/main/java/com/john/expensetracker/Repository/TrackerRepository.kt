package com.john.expensetracker.Repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.john.expensetracker.ROOM.Tracker
import com.john.expensetracker.ROOM.TrackerDao

class TrackerRepository(val trackerDao: TrackerDao) {

    val allTransactions : LiveData<List<Tracker>> = trackerDao.getAllTransactions()

    @WorkerThread
    suspend fun Insert(tracker: Tracker){
        trackerDao.insert(tracker)
    }

    @WorkerThread
    suspend fun Update(tracker: Tracker){
        trackerDao.update(tracker)
    }

    @WorkerThread
    suspend fun Delete(tracker: Tracker){
        trackerDao.delete(tracker)
    }

}