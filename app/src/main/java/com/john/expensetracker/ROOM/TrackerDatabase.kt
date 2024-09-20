package com.john.expensetracker.ROOM

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tracker::class], version = 1)
abstract class TrackerDatabase : RoomDatabase() {

    abstract fun getDao() : TrackerDao

    //Singleton

    companion object{

        @Volatile
        var INSTANCE : TrackerDatabase? = null

        fun getDatabase(context : Context) : TrackerDatabase{

            return INSTANCE?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrackerDatabase::class.java,
                    "tracker_database"
                ).build()
                INSTANCE = instance
                instance
            }

        }

    }

}