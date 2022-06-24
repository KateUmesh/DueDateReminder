package com.duedatereminder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.ClientsList
import java.util.*
import kotlin.collections.ArrayList

class ClientDetailsToSendNotificationAdapter(var context: Context, private var items: ArrayList<ClientsList>)
    : RecyclerView.Adapter<ClientDetailsToSendNotificationAdapter.ClientDetailsToSendNotificationViewHolder>(),
    Filterable {

    private var itemsFiltered = items


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientDetailsToSendNotificationViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_client_details,parent,false)
        return ClientDetailsToSendNotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientDetailsToSendNotificationViewHolder, position: Int) {
        val filteredList= itemsFiltered[position]
        /*Set Client Name*/
        holder.tvName.text = filteredList.name

        /*Set First Character*/
        val charArray = filteredList.name.toCharArray()
        holder.tvFirstLetter.text = charArray[0].toString()

        /*Set Client Mobile Number*/
        holder.tvMobileNumber.text = filteredList.mobile


        /*Set Client Email*/
        holder.tvEmail.text = filteredList.email

        /*Set Client Address*/
        holder.tvAddress.text = filteredList.address


    }

    override fun getItemCount(): Int {
       return itemsFiltered.size
    }


    class ClientDetailsToSendNotificationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var tvName:TextView  = itemView.findViewById(R.id.tvName)
         var tvMobileNumber:TextView  = itemView.findViewById(R.id.tvMobileNumber)
         var tvEmail:TextView  = itemView.findViewById(R.id.tvEmail)
         var tvAddress:TextView  = itemView.findViewById(R.id.tvAddress)
         var tvFirstLetter:TextView  = itemView.findViewById(R.id.tvFirstLetter)

    }

    interface SendSmsClickListener{
        fun sendSmsClick(position:Int,items:ArrayList<ClientsList>)
    }

    interface SendEmailClickListener{
        fun sendEmailClick(position:Int,items:ArrayList<ClientsList>)
    }

    override fun getFilter(): Filter {

        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString:String = constraint.toString()

                itemsFiltered = if(charString.isEmpty()){
                    items
                }else{
                    val filterList = ArrayList<ClientsList>()

                    for(s in items) {

                        if(s.name?.toLowerCase(Locale.ROOT)?.contains(charString.toLowerCase(
                                Locale.ROOT
                            )
                            )!!
                        )
                            filterList.add(s)
                    }
                    filterList
                }
                val filterResult  = FilterResults()
                filterResult.values = itemsFiltered
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemsFiltered = results!!.values as ArrayList<ClientsList>
                notifyDataSetChanged()
            }

        }
    }
}