package com.duedatereminder.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.model.AllClients
import com.duedatereminder.model.Blogs
import com.duedatereminder.utils.Constant
import com.duedatereminder.view.activities.EditClientActivity
import java.util.*

class BlogAdapter(var context: Context, private var items: List<Blogs>): RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_blog_layout,parent,false)
        return BlogViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {

        /*Set Blog Name*/
        if(items[position].blog_name.isNotEmpty()){
            holder.tvBlogName.text = items[position].blog_name
        }

        /*Set Blog Description*/
        if(items[position].blog_description.isNotEmpty()){
            holder.tvBlogDescription.text = items[position].blog_description
        }

        /*Set Blog Date*/
        if(items[position].date.isNotEmpty()){
            holder.tvBlogDate.text = items[position].date
        }


    }

    override fun getItemCount(): Int {
       return items.size
    }


    class BlogViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
         var tvBlogName:TextView  = itemView.findViewById(R.id.tvBlogName)
         var tvBlogDescription:TextView  = itemView.findViewById(R.id.tvBlogDescription)
         var tvBlogDate: TextView = itemView.findViewById(R.id.tvBlogDate)
    }
}