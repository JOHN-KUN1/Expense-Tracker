package com.john.expensetracker.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalTransactionsViewModel : ViewModel() {

    var totalBalance  = MutableLiveData<Int>()

    var currentBudget  = MutableLiveData<Int>()

    var currentExpenses = MutableLiveData<Int>()

    init {
        totalBalance.value = 1000
        currentExpenses.value = 0
        currentBudget.value = 0
    }

    fun increaseBalance(amount : Int){
        totalBalance.value = (totalBalance.value)?.plus(amount)
    }

    fun decreaseBalance(amount: Int){
        totalBalance.value = (totalBalance.value)?.minus(amount)
    }

    fun updateBudget(amount:Int){
        currentBudget.value = (currentBudget.value)?.plus(amount)
    }

    fun updateExpense(amount: Int){
        currentExpenses.value = (currentExpenses.value)?.plus(amount)
    }

    fun setTotalBalanceValue(amount: Int){
        totalBalance.value = amount
    }

    fun setCurrentBudgetValue(amount: Int){
        currentBudget.value = amount
    }

    fun setCurrentExpenseValue(amount: Int){
        currentExpenses.value = amount
    }


}