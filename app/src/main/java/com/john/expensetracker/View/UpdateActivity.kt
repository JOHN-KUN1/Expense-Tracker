package com.john.expensetracker.View

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.john.expensetracker.MyApplication.MyApplication
import com.john.expensetracker.R
import com.john.expensetracker.ROOM.Tracker
import com.john.expensetracker.ViewModel.MyViewModel
import com.john.expensetracker.ViewModel.TotalTransactionsViewModel
import com.john.expensetracker.ViewModel.TrackerViewModelFactory
import com.john.expensetracker.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    lateinit var updateBinding : ActivityUpdateBinding
    lateinit var viewModel : MyViewModel

    var userPreviousAmount : Int = 0
    var userNewAmount : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateBinding = ActivityUpdateBinding.inflate(layoutInflater)
        val view = updateBinding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val app = application as MyApplication
        getAndShowData()

        updateBinding.updateButtonSubmit.setOnClickListener {
            getAndUpdateData()
            finish()
        }
    }

    fun getAndShowData(){

        val label = intent.getStringExtra("tracked_label").toString()
        val amount = intent.getStringExtra("tracked_amount").toString()
        val description = intent.getStringExtra("tracked_description").toString()

        //to get the users previous amount
        userPreviousAmount = amount.toInt()

        updateBinding.editTextUpdateAmount.setText(amount)
        updateBinding.editTextUpdateLabel.setText(label)
        updateBinding.editTextUpdateDescription.setText(description)
    }

    fun getAndUpdateData(){
        val myFactory = TrackerViewModelFactory((application as MyApplication).repository)
        viewModel = ViewModelProvider(this,myFactory).get(MyViewModel::class.java)
        val transactionId = intent.getIntExtra("id",-1)

        if (transactionId != -1){
            val newLabel = updateBinding.editTextUpdateLabel.text.toString()
            val newAmount = updateBinding.editTextUpdateAmount.text.toString()
            val newDescription = updateBinding.editTextUpdateDescription.text.toString()

            //to get user new amount
            userNewAmount = newAmount.toInt()

            val newTransaction = Tracker(newLabel,newAmount,newDescription)

            newTransaction.trackerId = transactionId

            viewModel.Update(newTransaction)

            updateTotalBalance()
        }

    }

    fun updateTotalBalance(){

        val app = application as MyApplication
        val transactionsViewModel = ViewModelProvider(app.viewModelStore,ViewModelProvider.NewInstanceFactory()).get(TotalTransactionsViewModel::class.java)
        val newAmount = userPreviousAmount - userNewAmount
        transactionsViewModel.decreaseBalance(newAmount)

    }

    override fun onPause() {
        super.onPause()
        val app = application as MyApplication
        val sharedPreference = this.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val transactionsViewModel = ViewModelProvider(app.viewModelStore,ViewModelProvider.NewInstanceFactory()).get(TotalTransactionsViewModel::class.java)

        transactionsViewModel.totalBalance.value?.let { editor.putInt("total_balance",it) }
        editor.apply()
    }

}