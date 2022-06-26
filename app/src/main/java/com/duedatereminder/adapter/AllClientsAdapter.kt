package com.duedatereminder.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.AllClients
import com.duedatereminder.model.ClientsList
import com.duedatereminder.utils.Constant
import com.duedatereminder.view.activities.EditClientActivity
import com.duedatereminder.view.activities.ViewClientProfileActivity
import java.util.*

class AllClientsAdapter(var context: Context, private var items: List<AllClients>): RecyclerView.Adapter<AllClientsAdapter.AllClientsViewHolder>(),
    Filterable {

    private var itemsFiltered = items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllClientsViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_inbox,parent,false)
        return AllClientsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllClientsViewHolder, position: Int) {
        val filteredList= itemsFiltered[position]
        /*Set Name*/
        if(filteredList.name.isNotEmpty()){
            holder.tvName.text = filteredList.name
        }

        /*Set First Character*/
        val charArray = filteredList.name.toCharArray()
        holder.tvAllClient.text = charArray[0].toString()


        /*Set Email*/
        if(filteredList.email.isNotEmpty()){
            holder.tvEmail.text = filteredList.email
        }

        /*Set Address*/
        if(filteredList.address.isNotEmpty()){
            holder.tvAddress.text = filteredList.address
        }

        /*Set Card background*/
        //holder.cvAllClient.backgroundTintList= ColorStateList.valueOf(getRandomColorCode())

        /*Item Click*/
        holder.lyt_parent.setOnClickListener {
           /* val intent = Intent(context, EditClientActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.ID_CLIENT,filteredList.id_client)
            context.startActivity(intent)*/
            val intent = Intent(context, ViewClientProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.ID_CLIENT,filteredList.id_client)
            intent.putExtra(Constant.NAME,filteredList.name)
            intent.putExtra(Constant.MENU_STATE,"SHOW_MENU")
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
       return itemsFiltered.size
    }

    override fun getFilter(): Filter {

        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString:String = constraint.toString()

                itemsFiltered = if(charString.isEmpty()){
                    items
                }else{
                    val filterList = ArrayList<AllClients>()

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
                itemsFiltered = results!!.values as ArrayList<AllClients>
                notifyDataSetChanged()
            }

        }
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
         var lyt_parent:LinearLayout  = itemView.findViewById(R.id.lyt_parent)
         //var cvAllClient:CardView  = itemView.findViewById(R.id.cvAllClient)
    }
}