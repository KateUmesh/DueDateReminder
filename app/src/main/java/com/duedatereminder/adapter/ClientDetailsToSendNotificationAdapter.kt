package com.duedatereminder.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.ClientsList
import com.duedatereminder.model.DueDateCategories
import com.duedatereminder.model.NotificationTemplates
import com.duedatereminder.utils.Constant
import com.duedatereminder.view.activities.ImportClientCsvFileActivity
import com.duedatereminder.view.activities.NotificationCategoriesActivity

class ClientDetailsToSendNotificationAdapter(var context: Context,
                                             private var items: ArrayList<ClientsList>,
                                             var sendSmsClickListener:SendSmsClickListener,
                                             var sendEmailClickListener:SendEmailClickListener): RecyclerView.Adapter<ClientDetailsToSendNotificationAdapter.ClientDetailsToSendNotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientDetailsToSendNotificationViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_notification_templates,parent,false)
        return ClientDetailsToSendNotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientDetailsToSendNotificationViewHolder, position: Int) {
        /*Set Client Name*/
        holder.tvName.text = items[position].name

        /*Set Client Mobile Number*/
        holder.tvMobileNumber.text = items[position].mobile


        /*Set Client Email*/
        holder.tvEmail.text = items[position].email

        /*Set Client Address*/
        holder.tvAddress.text = items[position].address


        /*Send SMS Button Click*/
        holder.btnSendSms.setOnClickListener {
            sendSmsClickListener.sendSmsClick(position,items)
        }

        /*Send Email Button Click*/
        holder.btnSendEmail.setOnClickListener {
            sendEmailClickListener.sendEmailClick(position, items)
        }
    }

    override fun getItemCount(): Int {
       return items.size
    }


    class ClientDetailsToSendNotificationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var tvName:TextView  = itemView.findViewById(R.id.tvName)
         var tvMobileNumber:TextView  = itemView.findViewById(R.id.tvMobileNumber)
         var tvEmail:TextView  = itemView.findViewById(R.id.tvEmail)
         var tvAddress:TextView  = itemView.findViewById(R.id.tvAddress)
         var btnSendSms: Button = itemView.findViewById(R.id.btnSendSms)
         var btnSendEmail:Button  = itemView.findViewById(R.id.btnSendEmail)
    }

    interface SendSmsClickListener{
        fun sendSmsClick(position:Int,items:ArrayList<ClientsList>)
    }

    interface SendEmailClickListener{
        fun sendEmailClick(position:Int,items:ArrayList<ClientsList>)
    }
}