package com.duedatereminder.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.DueDateCategories
import com.duedatereminder.model.NotificationTemplates
import com.duedatereminder.utils.Constant
import com.duedatereminder.view.activities.ImportClientCsvFileActivity
import com.duedatereminder.view.activities.NotificationCategoriesActivity

class NotificationTemplatesAdapter(var context: Context, private var items: List<NotificationTemplates>): RecyclerView.Adapter<NotificationTemplatesAdapter.NotificationTemplatesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationTemplatesViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_notification_templates,parent,false)
        return NotificationTemplatesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationTemplatesViewHolder, position: Int) {
        holder.tvNotificationTemplates.text = items[position].message


        /*Item Click*/
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ImportClientCsvFileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.ID_NOTIFICATION,items[position].id_notification)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return items.size
    }


    class NotificationTemplatesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var tvNotificationTemplates:TextView  = itemView.findViewById(R.id.tvNotificationTemplates)
    }
}