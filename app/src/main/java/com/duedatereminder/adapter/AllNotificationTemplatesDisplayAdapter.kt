package com.duedatereminder.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.AllNotificationTemplates


class AllNotificationTemplatesDisplayAdapter(var context: Context,
                                             private var items: List<AllNotificationTemplates>,var flag:Int,var onChatClickListener: OnChatClickListener): RecyclerView.Adapter<AllNotificationTemplatesDisplayAdapter.AllNotificationTemplatesDisplayViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllNotificationTemplatesDisplayViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_notification_templates,parent,false)
        return AllNotificationTemplatesDisplayViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllNotificationTemplatesDisplayViewHolder, position: Int) {
        holder.tvNotificationTemplates.text = items[position].message

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", holder.tvNotificationTemplates.text)
        clipboard.setPrimaryClip(clip)

        /*holder.tvNotificationTemplates.setOnClickListener {
            context.copyToClipboard( holder.tvNotificationTemplates.text)
        }*/

        if(flag==1){
            holder.ivChat.visibility = View.VISIBLE
            holder.ivCopy.visibility = View.GONE
        }else{
            holder.ivChat.visibility = View.GONE
            holder.ivCopy.visibility = View.VISIBLE
        }

        /*Chat Click*/
        holder.ivChat.setOnClickListener {
            onChatClickListener.onChatItemClick(items[position].message)
        }

        /*Copy Click*/
        holder.ivCopy.setOnClickListener {
            onChatClickListener.onChatItemClick(items[position].message)
        }

    }

    override fun getItemCount(): Int {
       return items.size
    }


    class AllNotificationTemplatesDisplayViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var tvNotificationTemplates:TextView  = itemView.findViewById(R.id.tvNotificationTemplates)
         var ivChat:ImageView  = itemView.findViewById(R.id.ivChat)
         var ivCopy:ImageView  = itemView.findViewById(R.id.ivCopy)
    }
    fun Context.copyToClipboard(text: CharSequence){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Template copied", Toast.LENGTH_SHORT).show()
    }

    interface OnChatClickListener{
        fun onChatItemClick(template:String)
    }
}