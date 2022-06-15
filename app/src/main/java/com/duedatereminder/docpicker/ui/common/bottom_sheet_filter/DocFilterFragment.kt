package com.duedatereminder.docpicker.ui.common.bottom_sheet_filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.duedatereminder.R
import com.duedatereminder.docpicker.core.DocPickerConfig
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info



class DocFilterFragment : BottomSheetDialogFragment(), AnkoLogger, DocFilterAdapter.OnDocFilterClickListener {

    companion object {
        @JvmStatic
        fun newInstance(key: Int, b: Bundle?): BottomSheetDialogFragment {
            val bf: Bundle = b ?: Bundle()
            bf.putInt("fragment.key", key)
            val fragment = DocFilterFragment()
            fragment.arguments = bf
            return fragment
        }
    }

    private var mDocFilterList: ArrayList<DocFilterModel> = arrayListOf()
    private var mAdapter: DocFilterAdapter = DocFilterAdapter()
    private var mPickerConfig: DocPickerConfig = DocPickerConfig()
    private var mOnFilterDoneListener: OnFilterDoneListener? = null
    private lateinit var doc_filter_fragment_btn_update: TextView
    private lateinit var doc_filter_fragment_btn_cancel: ImageView
    private lateinit var doc_filter_fragment_ll_for_selected_docs: LinearLayout
    private lateinit var tb_doc_picker_doc_filter_fragment_recycler_view: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.tb_doc_picker_fragment_doc_filter, container, false)
        doc_filter_fragment_btn_update= view.findViewById(R.id.doc_filter_fragment_btn_update)
        doc_filter_fragment_btn_cancel= view.findViewById(R.id.doc_filter_fragment_btn_cancel)
        doc_filter_fragment_ll_for_selected_docs= view.findViewById(R.id.doc_filter_fragment_ll_for_selected_docs)
        tb_doc_picker_doc_filter_fragment_recycler_view= view.findViewById(R.id.tb_doc_picker_doc_filter_fragment_recycler_view)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(arguments!=null){
            mPickerConfig = arguments?.getSerializable(DocPickerConfig.ARG_BUNDLE) as DocPickerConfig
        }

        for(i in mPickerConfig.mDocTypes){
            mDocFilterList.add((DocFilterModel(i)))
        }
        initAdapter()
        initButton()
    }

    private fun initButton() {
        doc_filter_fragment_btn_update.setOnClickListener {
            info { "clicked" }

            val docTypes = arrayListOf<String>()
            for(i in mDocFilterList){
                if(i.isSelected){
                    docTypes.add(i.docType)
                }
            }
            if(docTypes.isNotEmpty()){
                mPickerConfig.setUserSelectedDocTypes(docTypes)
                mOnFilterDoneListener?.onFilterDone()
            }
            else
                Toast.makeText(context,"Please select at least one doc type.",Toast.LENGTH_LONG).show()
        }

        doc_filter_fragment_btn_cancel.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onDocClick(pData: DocFilterModel) {
        val selectedIndex = mDocFilterList.indexOf(pData)

        if(selectedIndex >= 0){
            //toggle
            mDocFilterList[selectedIndex].isSelected = !(mDocFilterList[selectedIndex].isSelected)

            //selected filter should not be null
            val selectedList = arrayListOf<String>()
            for(i in mDocFilterList){
                if(i.isSelected){
                    selectedList.add(i.docType)
                }
            }
            //if null then toggle the item again to get previous state
            if(selectedList.isEmpty()){
                mDocFilterList[selectedIndex].isSelected = !(mDocFilterList[selectedIndex].isSelected)
                Toast.makeText(context,"Filter can't be empty.",Toast.LENGTH_LONG).show()
                return
            }

            //update ui
            mAdapter.updateIsSelected(mDocFilterList[selectedIndex])
            SelectedDocsLayout(doc_filter_fragment_ll_for_selected_docs,selectedList,mPickerConfig)
                .updateSelectedViews()
        }
    }

    override fun getTheme(): Int {
        return R.style.App_Dialog_BottomSheet
    }

    private fun initAdapter() {
        mAdapter.setListener(this)
        mAdapter.setPickerConfig(mPickerConfig)
        tb_doc_picker_doc_filter_fragment_recycler_view.layoutManager = LinearLayoutManager(context)
        tb_doc_picker_doc_filter_fragment_recycler_view.adapter = mAdapter

        for(i in mDocFilterList){
            i.isSelected = mPickerConfig.mUserSelectedDocTypes.contains(i.docType)
        }

        mAdapter.setData(mDocFilterList)
        val selectedList = arrayListOf<String>()
        for(i in mDocFilterList){
            if(i.isSelected){
                selectedList.add(i.docType)
            }
        }
        if(selectedList.isNotEmpty())
            SelectedDocsLayout(doc_filter_fragment_ll_for_selected_docs,selectedList,mPickerConfig)
                .updateSelectedViews()
    }

    fun setListener(pListener: OnFilterDoneListener?) {
        mOnFilterDoneListener = pListener
    }


    interface OnFilterDoneListener {
        fun onFilterDone()
    }
}