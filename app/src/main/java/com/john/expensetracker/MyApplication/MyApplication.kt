package com.john.expensetracker.MyApplication

import android.app.Application
import androidx.lifecycle.ViewModelStore
import com.john.expensetracker.ROOM.TrackerDatabase
import com.john.expensetracker.Repository.TrackerRepository

class MyApplication : Application() {

    val database by lazy { TrackerDatabase.getDatabase(this) }
    val repository by lazy { TrackerRepository(database.getDao())}

    val viewModelStore = ViewModelStore()

}