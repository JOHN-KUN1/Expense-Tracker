package com.john.expensetracker.View

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.john.expensetracker.MyApplication.MyApplication
import com.john.expensetracker.R
import com.john.expensetracker.ROOM.Tracker
import com.john.expensetracker.ViewModel.MyViewModel
import com.john.expensetracker.ViewModel.TotalTransactionsViewModel
import com.john.expensetracker.ViewModel.TrackerViewModelFactory
import com.john.expensetracker.databinding.ActivityInsertBinding

class InsertActivity : AppCompatActivity() {

    lateinit var insertBinding: ActivityInsertBinding
    lateinit var viewModel: MyViewModel
    var validLabel  = arrayListOf("Expense","Budget")
    lateinit var totalTransactionsViewModel: TotalTransactionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        insertBinding = ActivityInsertBinding.inflate(layoutInflater)
        val view = insertBinding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val app = application as MyApplication

        val myFactory = TrackerViewModelFactory((application as MyApplication).repository)
        viewModel = ViewModelProvider(this,myFactory).get(MyViewModel::class.java)

        totalTransactionsViewModel = ViewModelProvider(app.viewModelStore,ViewModelProvider.NewInstanceFactory()).get(TotalTransactionsViewModel::class.java)

        insertBinding.insertButtonSubmit.setOnClickListener {
            val userLabel = insertBinding.editTextLabel.text.toString().trim().replaceFirstChar(Char::titlecase)
            val userDescription = insertBinding.editTextDescription.text.toString()
            if (userLabel in validLabel){
                if(userLabel == "Expense"){
                    val amount = insertBinding.editTextAmount.text.toString().toInt()
                    totalTransactionsViewModel.decreaseBalance(amount)
                    totalTransactionsViewModel.updateExpense(amount)
                    val userAmount = insertBinding.editTextAmount.text.toString()
                    getAndSaveData(userLabel,userAmount,userDescription)
                    finish()
                }
                else if(userLabel == "Budget"){
                    val amount = insertBinding.editTextAmount.text.toString().toInt()
                    totalTransactionsViewModel.increaseBalance(amount)
                    totalTransactionsViewModel.updateBudget(amount)
                    val userAmount = insertBinding.editTextAmount.text.toString()
                    getAndSaveData(userLabel,userAmount,userDescription)
                    finish()
                }

            }
            else{
                Toast.makeText(applicationContext,"The label must either be an Expense or Budget",Toast.LENGTH_LONG).show()
            }

        }

    }

    fun getAndSaveData(userLabel : String, userAmount : String, userDescription : String){

        val newTransaction = Tracker(userLabel,userAmount,userDescription)

        viewModel.Insert(newTransaction)

    }

    override fun onPause() {
        super.onPause()
        val sharedPreference = this.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        totalTransactionsViewModel.totalBalance.value?.let { editor.putInt("total_balance", it) }
        totalTransactionsViewModel.currentBudget.value?.let { editor.putInt("current_budget", it) }
        totalTransactionsViewModel.currentExpenses.value?.let { editor.putInt("current_expense", it) }
        editor.apply()
    }
}