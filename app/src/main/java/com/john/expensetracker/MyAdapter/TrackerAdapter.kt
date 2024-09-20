package com.john.expensetracker.MyAdapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.john.expensetracker.ROOM.Tracker
import com.john.expensetracker.View.UpdateActivity
import com.john.expensetracker.databinding.ItemDesignBinding

class TrackerAdapter(val context : Context) : RecyclerView.Adapter<TrackerAdapter.viewHolder>() {

    var myTransactions : List<Tracker> = ArrayList()

    class viewHolder(val itemDesignBinding: ItemDesignBinding) : RecyclerView.ViewHolder(itemDesignBinding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = ItemDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myTransactions.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemDesignBinding.textViewTransactionDescription.text = myTransactions[position].description
        if (myTransactions[position].label.trim().replaceFirstChar(Char::titlecase) == "Budget"){
            holder.itemDesignBinding.textViewTransactionAmount.text = "+$ " + myTransactions[position].amount
            holder.itemDesignBinding.textViewTransactionAmount.setTextColor(Color.GREEN)
        }
        else if(myTransactions[position].label.trim().replaceFirstChar(Char::titlecase) == "Expense"){
            holder.itemDesignBinding.textViewTransactionAmount.text = "-$ " + myTransactions[position].amount
            holder.itemDesignBinding.textViewTransactionAmount.setTextColor(Color.RED)
        }

        holder.itemDesignBinding.itemLayout.setOnClickListener {
            val intent = Intent(context,UpdateActivity::class.java)
            intent.putExtra("tracked_label",myTransactions[position].label)
            intent.putExtra("tracked_amount",myTransactions[position].amount)
            intent.putExtra("tracked_description",myTransactions[position].description)
            intent.putExtra("id",myTransactions[position].trackerId)
            context.startActivity(intent)
        }
    }

    fun setNote(tracker: List<Tracker>){
        this.myTransactions = tracker
        notifyDataSetChanged()

    }

    fun getItemAtPostition(position: Int) : Tracker{
        return myTransactions[position]
    }

}