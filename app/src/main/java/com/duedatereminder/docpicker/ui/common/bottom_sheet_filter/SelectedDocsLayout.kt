package com.duedatereminder.docpicker.ui.common.bottom_sheet_filter

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.duedatereminder.docpicker.DocPicker
import com.duedatereminder.R
import com.duedatereminder.docpicker.core.DocConstants
import com.duedatereminder.docpicker.core.DocPickerConfig
//import kotlinx.android.synthetic.main.tb_doc_picker_selected_doc_layout.view.*
import org.jetbrains.anko.textColor



class SelectedDocsLayout(private val mParentView: LinearLayout, private val mSelectedFilters: ArrayList<String>,private  var mPickerConfig: DocPickerConfig) {

    private val mContext: Context = mParentView.context
    private var mLayout: View = LayoutInflater.from(mContext).inflate(R.layout.tb_doc_picker_selected_doc_layout,  null)
    var selected_doc_view_txt_0:TextView = mLayout.findViewById(R.id.selected_doc_view_txt_0)
    var selected_doc_view_txt_1:TextView = mLayout.findViewById(R.id.selected_doc_view_txt_1)
    var selected_doc_view_txt_2:TextView = mLayout.findViewById(R.id.selected_doc_view_txt_2)

    fun updateSelectedViews(){

        //always do a fresh start
        selected_doc_view_txt_0.visibility = View.GONE
        selected_doc_view_txt_1.visibility = View.GONE
        selected_doc_view_txt_2.visibility = View.GONE


        for(i in mSelectedFilters.indices){
            updateViews(i, mSelectedFilters[i])
        }

        mParentView.removeAllViews()
        mParentView.addView(mLayout)
    }

    private fun updateViews(count: Int, docType: String) {
        when(count){
            0->updateColor(selected_doc_view_txt_0,docType)
            1->updateColor(selected_doc_view_txt_1,docType)
            2->updateColor(selected_doc_view_txt_2,docType)
            else->{
                updateColor(selected_doc_view_txt_2,"${count-1}+")
            }
        }
    }

    private fun updateColor(view: TextView, docType: String) {
        val mDrawable = ContextCompat.getDrawable(mContext, R.drawable.tb_doc_picker_dr_rect_round_red_doc_background)

        when {
            docType.contains("+") -> {
                mDrawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(mContext,R.color.md_grey_300), PorterDuff.Mode.SRC)
                view.textColor = ContextCompat.getColor(mContext,R.color.md_black_1000)
                view.visibility = View.VISIBLE
                view.text = docType
            }
            docType == DocPicker.DocTypes.AUDIO -> {
                mDrawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(mContext,R.color.tb_doc_light_audio), PorterDuff.Mode.SRC)
                view.textColor = ContextCompat.getColor(mContext,R.color.tb_doc_audio)
                view.visibility = View.VISIBLE
                view.text = DocConstants.docTypeMapLabel()[docType]

            }
            docType == DocPicker.DocTypes.IMAGE -> {
                mDrawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(mContext,R.color.tb_doc_light_image), PorterDuff.Mode.SRC)
                view.textColor = ContextCompat.getColor(mContext,R.color.tb_doc_image)
                view.visibility = View.VISIBLE
                view.text = DocConstants.docTypeMapLabel()[docType]

            }
            docType == DocPicker.DocTypes.VIDEO -> {
                mDrawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(mContext,R.color.tb_doc_light_video), PorterDuff.Mode.SRC)
                view.textColor = ContextCompat.getColor(mContext,R.color.tb_doc_video)
                view.visibility = View.VISIBLE
                view.text = DocConstants.docTypeMapLabel()[docType]

            }
            else -> {
                mDrawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(mContext,mPickerConfig.mDocLabelSet.getLabelForExt(DocConstants.docTypeMapLabel()[docType]!!).colorLightRes), PorterDuff.Mode.SRC)
                view.textColor = ContextCompat.getColor(mContext,mPickerConfig.mDocLabelSet.getLabelForExt(DocConstants.docTypeMapLabel()[docType]!!).colorRes)
                view.visibility = View.VISIBLE
                view.text = DocConstants.docTypeMapLabel()[docType]
            }
        }

        view.background=mDrawable
    }

}