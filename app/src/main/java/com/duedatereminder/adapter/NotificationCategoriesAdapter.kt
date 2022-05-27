package com.duedatereminder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.DueDateCategories

class NotificationCategoriesAdapter(var context: Context, private var items: List<DueDateCategories>): RecyclerView.Adapter<NotificationCategoriesAdapter.NotificationCategoriesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationCategoriesViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_notification_categories,parent,false)
        return NotificationCategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationCategoriesViewHolder, position: Int) {
        holder.tvNotificationCategories.text = items[position].category_name

        /*Set First Character*/
        val charArray = items[position].category_name.toCharArray()
        holder.tvFirstLetter.text = charArray[0].toString()
    }

    override fun getItemCount(): Int {
       return items.size
    }


    class NotificationCategoriesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var tvNotificationCategories:TextView  = itemView.findViewById(R.id.tvNotificationCategories)
        var tvFirstLetter: AppCompatTextView = itemView.findViewById(R.id.tvFirstLetter)
    }
}