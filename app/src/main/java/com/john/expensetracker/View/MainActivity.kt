package com.john.expensetracker.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.john.expensetracker.MyAdapter.TrackerAdapter
import com.john.expensetracker.MyApplication.MyApplication
import com.john.expensetracker.R
import com.john.expensetracker.ViewModel.MyViewModel
import com.john.expensetracker.ViewModel.TotalTransactionsViewModel
import com.john.expensetracker.ViewModel.TrackerViewModelFactory
import com.john.expensetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding : ActivityMainBinding
    lateinit var viewModel : MyViewModel
    lateinit var transactionsViewModel: TotalTransactionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val adapter = TrackerAdapter(this)
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemRemoved = adapter.getItemAtPostition(viewHolder.adapterPosition)
                viewModel.Delete(adapter.getItemAtPostition(viewHolder.adapterPosition))

                Snackbar.make(mainBinding.main,"Your Item has been deleted!",Snackbar.LENGTH_LONG)
                    .setAction("UNDO", View.OnClickListener {
                        viewModel.Insert(itemRemoved)
                    }).show()


            }

        }).attachToRecyclerView(mainBinding.recyclerView)

        val app = application as MyApplication

        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mainBinding.recyclerView.adapter = adapter

        val trackerFactory = TrackerViewModelFactory((application as MyApplication).repository)
        viewModel = ViewModelProvider(this,trackerFactory).get(MyViewModel::class.java)
        viewModel.allTransactions.observe(this, Observer { trackedTransactions ->
            //update the UI
            adapter.setNote(trackedTransactions)
        })

        transactionsViewModel = ViewModelProvider(app.viewModelStore,ViewModelProvider.NewInstanceFactory()).get(TotalTransactionsViewModel::class.java)
        transactionsViewModel.totalBalance.observe(this, Observer {
            mainBinding.textViewTotalBalance.text = "$ " + it.toString()
        })

        transactionsViewModel.currentBudget.observe(this, Observer {
            mainBinding.textViewCurrentBudget.text = "+$ " + it.toString()
        })

        transactionsViewModel.currentExpenses.observe(this, Observer {
            mainBinding.textViewCurrentExpenses.text = "-$ " + it.toString()
        })

        mainBinding.floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, InsertActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        val sharedPreference = this.getSharedPreferences("shared_pref",Context.MODE_PRIVATE)
        val total_balance = sharedPreference.getInt("total_balance",0)
        val current_budget = sharedPreference.getInt("current_budget",0)
        val current_expense = sharedPreference.getInt("current_expense",0)

        transactionsViewModel.setTotalBalanceValue(total_balance)
        transactionsViewModel.setCurrentBudgetValue(current_budget)
        transactionsViewModel.setCurrentExpenseValue(current_expense)
    }

}