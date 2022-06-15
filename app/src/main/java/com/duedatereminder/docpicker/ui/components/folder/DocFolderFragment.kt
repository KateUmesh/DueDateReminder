package com.duedatereminder.docpicker.ui.components.folder


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.duedatereminder.R
import com.duedatereminder.docpicker.core.DocPickerConfig
import com.duedatereminder.docpicker.core.FileManager
import com.duedatereminder.docpicker.ui.base.FragmentBase
import com.duedatereminder.docpicker.ui.common.bottom_sheet_filter.SelectedDocsLayout
import com.duedatereminder.docpicker.ui.components.ActivityLibMain
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.info

class DocFolderFragment : FragmentBase() {


    companion object {
        @JvmStatic
        fun newInstance(key: Int, b: Bundle?): Fragment {
            val bf: Bundle = b ?: Bundle()
            bf.putInt("fragment.key", key);
            val fragment = DocFolderFragment()
            fragment.arguments = bf
            return fragment
        }

        var sSelectedDocTypes: ArrayList<String> = arrayListOf()
    }

    private var mDocFolderAdapter: DocFolderAdapter = DocFolderAdapter()
    private var mDocFolderList: MutableLiveData<ArrayList<DocFolder>> = MutableLiveData()

    private var mPickerConfig: DocPickerConfig = DocPickerConfig()

    lateinit var  doc_folder_fragment_ll_for_selected_docs:LinearLayout
    lateinit var  doc_folder_fragment_ll_filter:LinearLayout
    lateinit var  frame_progress:FrameLayout
    lateinit var  doc_folder_fragment_recycler_view:RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.tb_doc_picker_fragment_doc_folder, container, false)
        doc_folder_fragment_ll_for_selected_docs = view.findViewById(R.id.doc_folder_fragment_ll_for_selected_docs)
        frame_progress = view.findViewById(R.id.frame_progress)
        doc_folder_fragment_ll_filter = view.findViewById(R.id.doc_folder_fragment_ll_filter)
        doc_folder_fragment_recycler_view = view.findViewById(R.id.doc_folder_fragment_recycler_view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments != null) {
            mPickerConfig = arguments?.getSerializable(DocPickerConfig.ARG_BUNDLE) as DocPickerConfig
        }

        //live data : While loading if user back-press then the app will not crash
        mDocFolderList.observe(this, Observer {
            mDocFolderAdapter.setData(it)
            sSelectedDocTypes = mPickerConfig.mUserSelectedDocTypes
            SelectedDocsLayout(doc_folder_fragment_ll_for_selected_docs, sSelectedDocTypes,mPickerConfig).updateSelectedViews()
            frame_progress.visibility = View.GONE
        })

        initButton()
        initAdapter()
    }

    private fun initButton() {
        doc_folder_fragment_ll_filter.setOnClickListener {
            (activity as ActivityLibMain).startFragmentFilterFragment()
        }
    }

    fun onRefresh() {
        info { "content: $sSelectedDocTypes" }
        info { "content2: ${mPickerConfig.mUserSelectedDocTypes}" }
        if (!sSelectedDocTypes.containsAll(mPickerConfig.mUserSelectedDocTypes) || !mPickerConfig.mUserSelectedDocTypes.containsAll(
                sSelectedDocTypes
            )
        ) {
            fetchDocFolders()
        }
    }

    fun onFilterDone() {
        fetchDocFolders()
    }

    private fun initAdapter() {
        mDocFolderAdapter.setListener(object : DocFolderAdapter.OnDocFolderClickListener {
            override fun onFolderClick(pData: DocFolder) {
                info { "folderPath: ${pData.path}" }
                (activity as ActivityLibMain).startDocFragment(pData.path, mPickerConfig)
            }
        })

        doc_folder_fragment_recycler_view.layoutManager = LinearLayoutManager(context)
        doc_folder_fragment_recycler_view.adapter = mDocFolderAdapter
        fetchDocFolders()
    }


    private fun fetchDocFolders() {
        //info { "list2: ${args.toList()}" }
        val bucketFetch = Single.fromCallable<ArrayList<DocFolder>> {
            FileManager.fetchAudioFolderList(
                context!!,
                mPickerConfig.getUserSelectedExtArgs(mPickerConfig.mUserSelectedDocTypes)
            )
        }
        bucketFetch
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<ArrayList<DocFolder>> {
                override fun onSubscribe(@NonNull d: Disposable) {
                    frame_progress.visibility = View.VISIBLE
                }

                override fun onSuccess(@NonNull docFolders: ArrayList<DocFolder>) {
                    mDocFolderList.value = docFolders
                }

                override fun onError(@NonNull e: Throwable) {
                    frame_progress.visibility = View.GONE
                    e.printStackTrace()
                    info { "error: ${e.message}" }
                }
            })
    }


}
