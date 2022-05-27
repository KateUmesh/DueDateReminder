package com.duedatereminder.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.AllClients
import com.duedatereminder.utils.Constant
import com.duedatereminder.view.activities.EditClientActivity
import java.util.*

class AllClientsAdapter(var context: Context, private var items: List<AllClients>): RecyclerView.Adapter<AllClientsAdapter.AllClientsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllClientsViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_inbox,parent,false)
        return AllClientsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllClientsViewHolder, position: Int) {

        /*Set Name*/
        if(items[position].name.isNotEmpty()){
            holder.tvName.text = items[position].name
        }

        /*Set First Character*/
        val charArray = items[position].name.toCharArray()
        holder.tvAllClient.text = charArray[0].toString()


        /*Set Email*/
        if(items[position].email.isNotEmpty()){
            holder.tvEmail.text = items[position].email
        }

        /*Set Address*/
        if(items[position].address.isNotEmpty()){
            holder.tvAddress.text = items[position].address
        }

        /*Set Card background*/
        //holder.cvAllClient.backgroundTintList= ColorStateList.valueOf(getRandomColorCode())

        /*Item Click*/
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditClientActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.ID_CLIENT,items[position].id_client)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
       return items.size
    }


    fun getRandomColorCode(): Int {
        val random = Random()
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }


    class AllClientsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var tvName:TextView  = itemView.findViewById(R.id.tvName)
         var tvAllClient:TextView  = itemView.findViewById(R.id.tvAllClient)
         var tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
         var tvAddress:TextView  = itemView.findViewById(R.id.tvAddress)
         //var cvAllClient:CardView  = itemView.findViewById(R.id.cvAllClient)
    }
}