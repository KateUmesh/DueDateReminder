package com.duedatereminder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.ClientsList

class ClientDetailsToSendNotificationAdapter(var context: Context,
                                             private var items: ArrayList<ClientsList>): RecyclerView.Adapter<ClientDetailsToSendNotificationAdapter.ClientDetailsToSendNotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientDetailsToSendNotificationViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_client_details,parent,false)
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


    }

    override fun getItemCount(): Int {
       return items.size
    }


    class ClientDetailsToSendNotificationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var tvName:TextView  = itemView.findViewById(R.id.tvName)
         var tvMobileNumber:TextView  = itemView.findViewById(R.id.tvMobileNumber)
         var tvEmail:TextView  = itemView.findViewById(R.id.tvEmail)
         var tvAddress:TextView  = itemView.findViewById(R.id.tvAddress)

    }

    interface SendSmsClickListener{
        fun sendSmsClick(position:Int,items:ArrayList<ClientsList>)
    }

    interface SendEmailClickListener{
        fun sendEmailClick(position:Int,items:ArrayList<ClientsList>)
    }
}