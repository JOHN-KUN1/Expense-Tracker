package com.john.expensetracker.ROOM

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracker_table")
class Tracker(
    var label : String,
    var amount : String,
    var description : String
){
    @PrimaryKey(autoGenerate = true)
    var trackerId : Int = 0
}