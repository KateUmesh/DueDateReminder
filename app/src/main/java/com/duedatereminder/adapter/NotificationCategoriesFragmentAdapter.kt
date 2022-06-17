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
import com.duedatereminder.utils.Constant
import com.duedatereminder.view.activities.ClientDetailsToSendNotificationsActivity
import com.duedatereminder.view.activities.ImportClientCsvFileActivity
import com.duedatereminder.view.activities.NotificationCategoriesActivity
import com.duedatereminder.view.activities.NotificationTemplatesActivity

class NotificationCategoriesFragmentAdapter(var context: Context, private var items: List<DueDateCategories>): RecyclerView.Adapter<NotificationCategoriesFragmentAdapter.NotificationCategoriesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationCategoriesViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_notification_categories,parent,false)
        return NotificationCategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationCategoriesViewHolder, position: Int) {
        holder.tvNotificationCategories.text = items[position].category_name

        /*Set First Character*/
        val charArray = items[position].category_name.toCharArray()
        holder.tvFirstLetter.text = charArray[0].toString()

        /*Item Click*/
        holder.itemView.setOnClickListener {
           /* val intent = Intent(context, NotificationTemplatesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.ID_DUE_DATE_CATEGORY,items[position].id_due_date_category)
            context.startActivity(intent)*/

            val intent = Intent(context, ClientDetailsToSendNotificationsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.ID_DUE_DATE_CATEGORY,items[position].id_due_date_category)
            intent.putExtra(Constant.CATEGORY_NAME,items[position].category_name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return items.size
    }


    class NotificationCategoriesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var tvNotificationCategories:TextView  = itemView.findViewById(R.id.tvNotificationCategories)
        var tvFirstLetter: AppCompatTextView = itemView.findViewById(R.id.tvFirstLetter)
    }
}