package com.duedatereminder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.Blogs
import com.duedatereminder.model.RemiderReport

class ReminderReportAdapter(var context: Context, private var items: List<RemiderReport>): RecyclerView.Adapter<ReminderReportAdapter.BlogViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_reminder_report_layout,parent,false)
        return BlogViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {

        /*Set notification type*/
        if(items[position].notification_type.isNotEmpty()){
            holder.tvNotificationType.text = items[position].notification_type
        }

        /*Set message sent*/
        if(items[position].message_sent.isNotEmpty()){
            holder.tvMessageSent.text = items[position].message_sent
        }

        /*Set Reminder Date*/
        if(items[position].date.isNotEmpty()){
            holder.tvReminderDate.text = items[position].date
        }


    }

    override fun getItemCount(): Int {
       return items.size
    }


    class BlogViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var tvNotificationType:TextView  = itemView.findViewById(R.id.tvNotificationType)
         var tvMessageSent:TextView  = itemView.findViewById(R.id.tvMessageSent)
         var tvReminderDate: TextView = itemView.findViewById(R.id.tvReminderDate)
    }
}