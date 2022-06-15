package com.duedatereminder.docpicker.ui.components

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.duedatereminder.R
import com.duedatereminder.docpicker.core.DocConstants
import com.duedatereminder.docpicker.core.DocPickerConfig
import com.duedatereminder.docpicker.ui.base.ActivityBase
import com.duedatereminder.docpicker.ui.common.bottom_sheet_filter.DocFilterFragment
import com.duedatereminder.docpicker.ui.components.file.DocFragment
import com.duedatereminder.docpicker.ui.components.folder.DocFolderFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.jetbrains.anko.info
import java.io.Serializable


class ActivityLibMain : ActivityBase(){

    companion object{
        const val B_ARG_URI_LIST = "activity.lib.main.uri.list"
    }


    private lateinit var mPickerConfig: DocPickerConfig
    private var mDocFilterFragment: BottomSheetDialogFragment? = null
    lateinit var activity_lib_main_toolbar:Toolbar
    lateinit var activity_lib_main_toolbar_txt_count:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tb_doc_picker_activity_lib_main)
        activity_lib_main_toolbar=findViewById(R.id.activity_lib_main_toolbar)
        activity_lib_main_toolbar_txt_count=findViewById(R.id.activity_lib_main_toolbar_txt_count)
        initToolbar(R.drawable.tb_doc_picker_ic_arrow_back_black_24dp,activity_lib_main_toolbar)
        toolbarTitle = "Select Folder"

        if (intent.extras != null) {
            mPickerConfig = intent.getSerializableExtra(DocPickerConfig.ARG_BUNDLE) as DocPickerConfig
        }

        startDocFolderFragment()
    }




    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_content)
        when (fragment) {
            is DocFolderFragment -> finish()
            is DocFragment -> {
                super.onBackPressed()
                toolbarTitle = "Select Folder"
                activity_lib_main_toolbar_txt_count.visibility = View.GONE
                updateCounter(0)

                val fragment2 = supportFragmentManager.findFragmentById(R.id.frame_content)
                if(fragment2 is DocFolderFragment){
                    fragment2.onRefresh()
                }

            }
            else -> super.onBackPressed()
        }
    }


    /**
     * @param counter counter for selecting multiple media files
     */
    fun updateCounter(counter: Int) {
        activity_lib_main_toolbar_txt_count.text = "$counter"
    }

    fun sendBackData(list: ArrayList<Uri>) {
        if (list.isNotEmpty()) {
            val intent = Intent()
            intent.putExtra(B_ARG_URI_LIST, list as Serializable)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }


     fun startDocFolderFragment() {
        toolbarTitle = "Select Folder"
        activity_lib_main_toolbar_txt_count.visibility = View.GONE

        val bundle = Bundle()
        bundle.putSerializable(DocPickerConfig.ARG_BUNDLE, mPickerConfig)

        val fragment = DocFolderFragment.newInstance(DocConstants.Fragment.DOC_FOLDER, bundle)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_content, fragment, DocFolderFragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()
    }


    fun startDocFragment(folderPath: String,pickerConfig: DocPickerConfig) {
        toolbarTitle = "Choose Doc"
        activity_lib_main_toolbar_txt_count.visibility = View.VISIBLE

        val bundle = Bundle()
        bundle.putSerializable(DocPickerConfig.ARG_BUNDLE, pickerConfig)
        bundle.putString(DocFragment.B_ARG_FOLDER_PATH, folderPath)

        val fragment = DocFragment.newInstance(DocConstants.Fragment.DOC_LIST, bundle)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_content, fragment, DocFragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()
    }


    fun startFragmentFilterFragment() {
        val bundle = Bundle()
        bundle.putSerializable(DocPickerConfig.ARG_BUNDLE, mPickerConfig)

        info { "fragment: create" }
        mDocFilterFragment = DocFilterFragment.newInstance(DocConstants.Fragment.DOC_FILTER, bundle)
        (mDocFilterFragment as DocFilterFragment).setListener(object : DocFilterFragment.OnFilterDoneListener{
            override fun onFilterDone() {
                val fragment = supportFragmentManager.findFragmentById(R.id.frame_content)
                when (fragment) {
                    is DocFolderFragment -> {
                        info { "folder fragment" }
                        fragment.onFilterDone()
                    }
                    is DocFragment -> {
                        info { "doc fragment" }
                        fragment.onFilterDone()
                    }
                }
                mDocFilterFragment?.dismiss()
            }
        })
        mDocFilterFragment?.show(supportFragmentManager, DocFilterFragment::class.java.simpleName)
    }

}
