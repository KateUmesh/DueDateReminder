package com.duedatereminder.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.AllClients
import com.duedatereminder.model.DueDateCategories
import com.duedatereminder.utils.Constant
import com.duedatereminder.view.activities.EditClientActivity
import com.duedatereminder.view.activities.LoginActivity

class AllClientsAdapter(var context: Context, private var items: List<AllClients>): RecyclerView.Adapter<AllClientsAdapter.AllClientsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllClientsViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_all_clients_layout,parent,false)
        return AllClientsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllClientsViewHolder, position: Int) {

        /*Set Name*/
        if(items[position].name.isNotEmpty()){
            holder.tvName.text = items[position].name
        }

        /*Set Mobile*/
        if(items[position].mobile.isNotEmpty()){
            holder.tvMobile.text = items[position].mobile
        }

        /*Set Email*/
        if(items[position].email.isNotEmpty()){
            holder.tvEmail.text = items[position].email
        }

        /*Set Address*/
        if(items[position].address.isNotEmpty()){
            holder.tvAddress.text = items[position].address
        }

        /*More Click*/
        holder.ivMore.setOnClickListener {
            val intent = Intent(context, EditClientActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.ID_CLIENT,items[position].id_client)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
       return items.size
    }


    class AllClientsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var tvName:AppCompatTextView  = itemView.findViewById(R.id.tvName)
         var tvMobile:AppCompatTextView  = itemView.findViewById(R.id.tvMobile)
         var tvEmail:AppCompatTextView  = itemView.findViewById(R.id.tvEmail)
         var tvAddress:AppCompatTextView  = itemView.findViewById(R.id.tvAddress)
         var ivMore:ImageView  = itemView.findViewById(R.id.ivMore)
    }
}