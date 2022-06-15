package com.duedatereminder.docpicker.ui.components.file

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.docpicker.core.DocPickerConfig
import com.duedatereminder.docpicker.labels.DocLabelSet
import com.duedatereminder.docpicker.widget.ImageViewCheckable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.File



class DocAdapter: RecyclerView.Adapter<DocAdapter.DocVewHolder>(), AnkoLogger {
    private var mData: MutableList<DocModel> = arrayListOf()
    private var mOnDocClickListener: OnDocClickListener? = null
    private var mPickerConfig: DocPickerConfig = DocPickerConfig()
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocVewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tb_doc_picker_item_doc, parent, false)
        return DocVewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: DocVewHolder, position: Int) {
        holder.bind(mData[position])
    }


    fun setListener(listener : OnDocClickListener){
        mOnDocClickListener = listener
    }

    fun setData(pData: MutableList<DocModel>){
        mData = pData
        notifyDataSetChanged()
    }

    fun updateIsSelected(pData: DocModel){
        val pos = mData.indexOf(pData)
        if(pos>=0){
            mData[pos] = pData
            notifyItemChanged(pos)
        }
    }

    fun setPickerConfig(config: DocPickerConfig){
        mPickerConfig = config
    }
    inner class DocVewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var item_doc_doc_name: TextView = itemView.findViewById(R.id.item_doc_doc_name)
        var item_doc_doc_size: TextView = itemView.findViewById(R.id.item_doc_doc_size)
        var item_doc_file_ext: TextView = itemView.findViewById(R.id.item_doc_file_ext)
        var item_doc_ivc: ImageViewCheckable = itemView.findViewById(R.id.item_doc_ivc)
        fun bind(pData: DocModel){

            setDrawableForMime(itemView, pData.mimeType,pData.filePath)

           item_doc_ivc.isChecked = pData.isSelected
            val size = (pData.size/1000).toString()

            item_doc_doc_name.text = File(pData.filePath).name //to include name with extensions
            item_doc_doc_size.text = "$size KB"

            itemView.setOnClickListener {
                mOnDocClickListener?.onDocCheck(pData)
            }

            if(!mPickerConfig.mAllowMultiSelection){
                item_doc_ivc.visibility = View.GONE
            }
            else{
                item_doc_ivc.visibility = View.VISIBLE
            }
        }

        private fun setDrawableForMime(itemView: View, mimeType: String?, filePath: String) {

            var extType = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)

            if(extType==null){
                val i = filePath.lastIndexOf('.')
                if (i > 0 && i< filePath.length-1) {
                    extType = filePath.substring(i + 1)
                }
            }

            val mDrawable = ContextCompat.getDrawable(mContext, R.drawable.tb_doc_picker_dr_rect_round_red_doc_background)

            info { "extType: $extType" }
            info { "res: ${DocLabelSet().getLabelForExt(extType!!)}" }

            val label = mPickerConfig.mDocLabelSet.getLabelForExt(extType!!)
            mDrawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(mContext,label.colorRes), PorterDuff.Mode.SRC)
           item_doc_file_ext.text = label.text
            item_doc_file_ext.background = mDrawable
        }
    }


    interface OnDocClickListener {
        fun onDocCheck(pData: DocModel)
    }
}