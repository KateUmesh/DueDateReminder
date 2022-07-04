package com.duedatereminder.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.ClientsList
import com.duedatereminder.utils.Constant
import com.duedatereminder.view.activities.ViewClientProfileActivity
import java.util.*
import kotlin.collections.ArrayList


class ClientDetailsToSendNotificationAdapter(var context: Context, private var items: ArrayList<ClientsList>,var listItem:ClientItemOnClickListener)
    : RecyclerView.Adapter<ClientDetailsToSendNotificationAdapter.ClientDetailsToSendNotificationViewHolder>(),
    Filterable {

    var row_index = 0
    private var itemsFiltered = items
    var select:Int=0
    private  var clearAll: String =""

    var cardViewList= ArrayList<CheckBox>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientDetailsToSendNotificationViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_client_details,parent,false)
        return ClientDetailsToSendNotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientDetailsToSendNotificationViewHolder, position: Int) {
        val filteredList= itemsFiltered[position]

        if (!cardViewList.contains(holder.cbClient)) {
            cardViewList.add(holder.cbClient);
        }

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

        /*Item Click*/
        holder.itemView.setOnClickListener {

            val intent = Intent(context, ViewClientProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.ID_CLIENT,filteredList.id_client)
            intent.putExtra(Constant.NAME,filteredList.name)
            intent.putExtra(Constant.MENU_STATE,"HIDE_MENU")
            context.startActivity(intent)
        }

        if (filteredList.isSelected) {
            holder.cbClient.isChecked = true;
        }
        else {
            holder.cbClient.isChecked = false;
        }

        holder.cbClient.setOnClickListener {
            listItem.onClientItemClickListener(position, filteredList,holder.cbClient)
            if (filteredList.isSelected) {
                holder.cbClient.isChecked=false
                filteredList.isSelected=false
            }
            else {
                holder.cbClient.isChecked = true;
                filteredList.isSelected=true
            }
            notifyDataSetChanged()
        }





       /* if(row_index==position){
            holder.cbClient.isChecked=true
        }
        else
        {
            holder.cbClient.isChecked=false

        }*/



        /*if(clearAll=="1"){
            holder.cbClient.isChecked=true
        }else{
            holder.cbClient.isChecked=false
        }*/



    }

    override fun getItemCount(): Int {
       return itemsFiltered.size
    }

    fun setList(clientList: ArrayList<ClientsList>) {
        this.items.addAll(clientList)
        notifyDataSetChanged()
    }

    fun deselectAllCheckbox( clearAll: String){
        this.clearAll = clearAll
        notifyDataSetChanged()
    }

    fun selectAllCheckbox( clearAll: String){
        this.clearAll = clearAll
        notifyDataSetChanged()
    }

    class ClientDetailsToSendNotificationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var tvName:TextView  = itemView.findViewById(R.id.tvName)
         var tvMobileNumber:TextView  = itemView.findViewById(R.id.tvMobileNumber)
         var tvEmail:TextView  = itemView.findViewById(R.id.tvEmail)
         var tvAddress:TextView  = itemView.findViewById(R.id.tvAddress)
         var tvFirstLetter:TextView  = itemView.findViewById(R.id.tvFirstLetter)
         var cbClient:CheckBox  = itemView.findViewById(R.id.cbClient)

    }

    fun setSelected(select:Int){
        this.select=select
        notifyDataSetChanged()
    }

    interface SendSmsClickListener{
        fun sendSmsClick(position:Int,items:ArrayList<ClientsList>)
    }

    interface SendEmailClickListener{
        fun sendEmailClick(position:Int,items:ArrayList<ClientsList>)
    }

    interface ClientItemOnClickListener
    {
        fun onClientItemClickListener(position:Int,item:ClientsList,checkBox: CheckBox)
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