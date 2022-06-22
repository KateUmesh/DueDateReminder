package com.duedatereminder.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.NotificationTemplates
import com.duedatereminder.utils.Constant
import com.duedatereminder.view.activities.SendMessageActivity

class NotificationTemplatesAdapter(var context: Context, private var items: List<NotificationTemplates>,var idNotificationCategory:String,var SEND_SMS_DETAILS:String,var SEND_EMAIL_DETAILS:String): RecyclerView.Adapter<NotificationTemplatesAdapter.NotificationTemplatesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationTemplatesViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_notification_templates,parent,false)
        return NotificationTemplatesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationTemplatesViewHolder, position: Int) {
        holder.tvNotificationTemplates.text = items[position].message


        /*Item Click*/
        holder.itemView.setOnClickListener {
            /*val intent = Intent(context, ClientDetailsToSendNotificationsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.ID_NOTIFICATION,items[position].id_notification)
            intent.putExtra(Constant.ID_DUE_DATE_CATEGORY,idNotificationCategory)
            context.startActivity(intent)*/

            val intent = Intent(context, SendMessageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.ID_NOTIFICATION, items[position].id_notification)
            intent.putExtra(Constant.ID_DUE_DATE_CATEGORY, idNotificationCategory)
            intent.putExtra(Constant.TEMPLATE, items[position].message)
            intent.putExtra(Constant.SEND_SMS_DETAILS, SEND_SMS_DETAILS)
            intent.putExtra(Constant.SEND_EMAIL_DETAILS,SEND_EMAIL_DETAILS)
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