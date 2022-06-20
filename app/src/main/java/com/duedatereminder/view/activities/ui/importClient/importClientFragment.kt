package com.duedatereminder.view.activities.ui.importClient

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.adapter.NotificationCategoriesAdapter
import com.duedatereminder.adapter.NotificationCategoriesFragmentAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.databinding.FragmentImportClientBinding
import com.duedatereminder.databinding.FragmentNotificationBinding
import com.duedatereminder.docpicker.DocPicker
import com.duedatereminder.docpicker.core.DocPickerConfig
import com.duedatereminder.model.DueDateCategories
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.LocalSharedPreference
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.view.activities.ClientDetailsToSendNotificationsActivity
import com.duedatereminder.view.activities.MyWb
import com.duedatereminder.viewModel.activityViewModel.ViewModelImportClientCsvFile
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationCategories
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

class importClientFragment : Fragment(), SnackBarCallback {

    private var _binding: FragmentImportClientBinding? = null
    private val binding get() = _binding!!

    lateinit var mViewModelNotificationCategories: ViewModelNotificationCategories
    lateinit var mViewModelImportClientCsvFile: ViewModelImportClientCsvFile
    private lateinit var ll_loading : LinearLayoutCompat
    private lateinit var btnSelectFile : Button
    private lateinit var btnUploadCsvFile : Button
    lateinit var edt_categories: TextInputEditText
    lateinit var llImportClient: LinearLayoutCompat
    private var categoryList = ArrayList<DueDateCategories>()
    private var mSelectedCategory = -1
    var maritalStatus = ""
    lateinit var fileUri: Uri
    private var idDueDateCategory:String = ""
    private lateinit var tvNoData : TextView
    private lateinit var wvImportClient : WebView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentImportClientBinding.inflate(inflater, container, false)
        val root: View = binding.root



        /**Initialize Variables*/
        wvImportClient = root.findViewById(R.id.wvImportClient)

        val intent = Intent(context, MyWb::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        context!!.startActivity(intent)

        //wvImportClient.loadUrl(" https://kuberaduedate.com/app/welcome/csv_file_upload/"+ LocalSharedPreference.getStringValue(Constant.token))
        //wvImportClient.settings.javaScriptEnabled = true

        /**Initialize Variables*/
        ll_loading = root.findViewById(R.id.ll_loading)
        btnSelectFile = root.findViewById(R.id.btnSelectFile)
        btnUploadCsvFile = root.findViewById(R.id.btnUploadCsvFile)
        edt_categories = root.findViewById(R.id.edt_categories)
        llImportClient = root.findViewById(R.id.llImportClient)
        tvNoData = root.findViewById(R.id.tvNoData)
        //  ll_loading.visibility = View.VISIBLE

        /**Initialize View Model*/
        mViewModelNotificationCategories = ViewModelProvider(this)[ViewModelNotificationCategories::class.java]
        /**Initialize View Model*/
        mViewModelImportClientCsvFile = ViewModelProvider(this)[ViewModelImportClientCsvFile::class.java]


        /**Call NotificationCategories GET Api*/
        //callNotificationCategoriesApi()

        /**Response of NotificationCategories Api*/
        mViewModelNotificationCategories.mModelNotificationCategoriesResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    tvNoData.visibility= View.GONE
                    llImportClient.visibility = View.VISIBLE
                    if(!it.data!!.due_date_categories.isNullOrEmpty()){
                        categoryList = it.data?.due_date_categories as ArrayList<DueDateCategories>
                    }else{
                        tvNoData.visibility= View.VISIBLE
                    }
                }
                "0"->{
                    tvNoData.visibility= View.VISIBLE
                    ContextExtension.snackBar(it.message, this.requireActivity())
                }
                else->{
                    activity?.showSnackBar(this,it.message)
                }
            }
        })

        //Categories
        edt_categories.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val values = ArrayList<String>()
                val key = ArrayList<String>()
                for (i in this.categoryList.indices) {
                    values.add(this.categoryList[i].category_name)
                    key.add(this.categoryList[i].id_due_date_category)
                }
                val mBuilder = AlertDialog.Builder(requireContext())
                mBuilder.setTitle(getString(R.string.select_categories))

                mBuilder.setSingleChoiceItems(values.toTypedArray(), mSelectedCategory) { dialogInterface, i ->
                    mSelectedCategory = i
                    edt_categories.setText(values[i])
                    idDueDateCategory = key[i]
                    dialogInterface.dismiss()
                }
                val mDialog = mBuilder.create()
                mDialog.show()
            }
            true
        }

        /**TV CSV File Click*/
        btnSelectFile.setOnClickListener {
            startAllPicker(false)

        }

        /**Button Upload CSV File Click*/
        btnUploadCsvFile.setOnClickListener {
            uploadCSVFile(idDueDateCategory,fileUri)
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun callNotificationCategoriesApi(){
        ll_loading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelNotificationCategories.notificationCategories()
        }else{
            activity?.showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        mViewModelNotificationCategories.notificationCategories()
    }

    override fun snackBarFailedInterConnection() {
        activity?.showSnackBar(this,getString(R.string.no_internet_connection))
    }


    @SuppressLint("CheckResult")
    private fun startAllPicker(isMultiple: Boolean) {

        val docs = arrayListOf<String>(
            DocPicker.DocTypes.PDF,
            DocPicker.DocTypes.MS_WORD,
            DocPicker.DocTypes.MS_POWERPOINT,
            DocPicker.DocTypes.MS_EXCEL,
            DocPicker.DocTypes.TEXT,
            DocPicker.DocTypes.AUDIO,
            DocPicker.DocTypes.IMAGE,
            DocPicker.DocTypes.VIDEO)

        val config = DocPickerConfig()
            .setShowConfirmationDialog(true)
            .setAllowMultiSelection(isMultiple)
            .setExtArgs(docs)

        DocPicker.with(this.requireActivity())
            .setConfig(config)
            .onResult()
            .subscribe({
                fileUri = it[0]
                val csvFile = File(Environment.getExternalStorageDirectory().toString()+ File.separatorChar+"Download" + File.separatorChar+getFileName(fileUri))
                btnSelectFile.text=csvFile.name
            },{
                Log.e("error", "error: ${it.printStackTrace()}")
                //info { "error: ${it.printStackTrace()}" }
            })
    }

    private fun uploadCSVFile(idDueDateCategory:String,uri: Uri) {
        val mimeType = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension("csv");
        Log.e("mime:", mimeType!!)
        //creating a file
        //val file1 = File(uri.path)
        val csvFile = File(Environment.getExternalStorageDirectory().toString()+ File.separatorChar+"Download" + File.separatorChar+getFileName(uri))
        val idDueDateCategoryRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"),idDueDateCategory)
        val csv_file: RequestBody = RequestBody.create(MediaType.parse(mimeType), csvFile)
        //val requestBody = createRequestBodyFromFile(csvFile, mimeType)


        ll_loading.visibility = View.VISIBLE
        if (NetworkConnection.isNetworkConnected()) {
            mViewModelImportClientCsvFile.importClientCsvFile(idDueDateCategoryRequestBody,csv_file)
        } else {
            activity?.showSnackBar(this,getString(R.string.no_internet_connection))
        }

        mViewModelImportClientCsvFile.mImportClientCsvFileLiveData.observe(this, androidx.lifecycle.Observer {
            ll_loading.visibility = View.GONE
            if (it.status == "1") {
                ContextExtension.showOkDialog(it.message, requireContext())
            }else{
                Log.e("exp",it.message)
                ContextExtension.showOkDialog(it.message, requireContext())
            }
        })

    }

    fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }
}