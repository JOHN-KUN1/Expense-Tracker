package com.john.expensetracker.ROOM

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface TrackerDao {

    @Insert
    suspend fun insert(tracker: Tracker)

    @Update
    suspend fun update(tracker: Tracker)

    @Delete
    suspend fun delete(tracker: Tracker)

    @Query("SELECT * FROM tracker_table ORDER BY trackerId ASC")
    fun getAllTransactions() : LiveData<List<Tracker>>

}