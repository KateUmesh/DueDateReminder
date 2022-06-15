package com.duedatereminder.view.activities

import android.R.attr.mimeType
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.adapter.NotificationCategoriesAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.docpicker.DocPicker
import com.duedatereminder.docpicker.core.DocPickerConfig
import com.duedatereminder.model.DueDateCategories
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showOkDialog
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelImportClientCsvFile
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationCategories
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File


class ImportClientCsvFileActivity1 : AppCompatActivity(),SnackBarCallback {
    lateinit var mViewModelImportClientCsvFile: ViewModelImportClientCsvFile
    private lateinit var ll_loading : LinearLayoutCompat
    private lateinit var tvCsvFile : TextView
    private lateinit var btnUploadCsvFile : Button
    private var idDueDateCategory:String = ""
    lateinit var fileUri: Uri
    lateinit var csvFile: File
    lateinit var edt_categories:TextInputEditText
    private var maritalStatusList = ArrayList<DueDateCategories>()
    private var mSelectedMaritalStatus = -1
    var maritalStatus = ""
    lateinit var mViewModelNotificationCategories: ViewModelNotificationCategories
    lateinit var llImportClient: LinearLayoutCompat
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_client_csv_file1)

        /**Toolbar*/
        toolbar(getString(R.string.selectCsvFile),true)

        /**Get idDueDateCategory from NotificationCategoriesAdapter*/
        idDueDateCategory = intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!!

        /**Initialize Variables*/
        ll_loading = findViewById(R.id.ll_loading)
        tvCsvFile = findViewById(R.id.tvCsvFile)
        btnUploadCsvFile = findViewById(R.id.btnUploadCsvFile)
        edt_categories = findViewById(R.id.edt_categories)
        llImportClient = findViewById(R.id.llImportClient)

        /**Initialize View Model*/
        mViewModelImportClientCsvFile = ViewModelProvider(this)[ViewModelImportClientCsvFile::class.java]


        /**Initialize View Model*/
        mViewModelNotificationCategories = ViewModelProvider(this)[ViewModelNotificationCategories::class.java]


        /**Call NotificationCategories GET Api*/
        callNotificationCategoriesApi()

        /**Response of SendLoginOtp Api*/
        mViewModelNotificationCategories.mModelNotificationCategoriesResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    llImportClient.visibility = View.VISIBLE
                    if(!it.data!!.due_date_categories.isNullOrEmpty()){
                        maritalStatusList = it.data?.due_date_categories as ArrayList<DueDateCategories>
                    }
                }
                "0"->{
                    ContextExtension.snackBar(it.message, this)
                }
                else->{
                    showSnackBar(this,it.message)
                }
            }
        })


        //Marital status
        edt_categories.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val values = ArrayList<String>()
                val key = ArrayList<String>()
                for (i in this.maritalStatusList.indices) {
                    values.add(this.maritalStatusList[i].category_name)
                    key.add(this.maritalStatusList[i].id_due_date_category)
                }
                val mBuilder = AlertDialog.Builder(this)
                mBuilder.setTitle(getString(R.string.select_categories))

                mBuilder.setSingleChoiceItems(values.toTypedArray(), mSelectedMaritalStatus) { dialogInterface, i ->
                    mSelectedMaritalStatus = i
                    edt_categories.setText(values[i])
                    maritalStatus = key[i]
                    dialogInterface.dismiss()
                }
                val mDialog = mBuilder.create()
                mDialog.show()
            }
            true
        }

        /**TV CSV File Click*/
        tvCsvFile.setOnClickListener {
            startAllPicker(false)

        }

        /**Button Upload CSV File Click*/
        btnUploadCsvFile.setOnClickListener {
            uploadCSVFile(idDueDateCategory,fileUri)
        }
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

        DocPicker.with(this)
            .setConfig(config)
            .onResult()
            .subscribe({
                fileUri = it[0]
                val csvFile = File(Environment.getExternalStorageDirectory().toString()+File.separatorChar+"Download" + File.separatorChar+getFileName(fileUri))
                tvCsvFile.text=csvFile.name
               /* Log.e("list","here is the list: $it")
                Log.e("path","here is the path: ${it[0].path}")
                  csvFile = File(Environment.getExternalStorageDirectory().toString()+File.separatorChar+"Download" + "/"+getFileName(fileUri))
                Log.e("csvfile","csvfile: $csvFile")
                 val csvfileString:String = this.applicationInfo.dataDir + File.separatorChar + getFileName(fileUri)
                 val csvFile11 = File(csvfileString)
                Log.e("csvfile11","csvfile11: $csvFile11")
                Log.e("realPath","realPath: ${createCopyAndReturnRealPath(this,it[0])}")*/
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
        val csvFile = File(Environment.getExternalStorageDirectory().toString()+File.separatorChar+"Download" + File.separatorChar+getFileName(uri))
        val idDueDateCategoryRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"),idDueDateCategory)
        val csv_file: RequestBody = RequestBody.create(MediaType.parse(mimeType), csvFile)
        //val requestBody = createRequestBodyFromFile(csvFile, mimeType)


        ll_loading.visibility = View.VISIBLE
        if (NetworkConnection.isNetworkConnected()) {
            mViewModelImportClientCsvFile.importClientCsvFile(idDueDateCategoryRequestBody,csv_file)
        } else {
            showSnackBar(this,getString(R.string.no_internet_connection))
        }

        mViewModelImportClientCsvFile.mImportClientCsvFileLiveData.observe(this, androidx.lifecycle.Observer {
            ll_loading.visibility = View.GONE
            if (it.status == "1") {
                showOkDialog(it.message,this)
            }else{
                Log.e("exp",it.message)
                showOkDialog(it.message,this)
            }
        })

    }

    override fun snackBarSuccessInternetConnection() {
        uploadCSVFile(idDueDateCategory,fileUri)
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
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


    private fun createRequestBodyFromFile(file: File, mimeType: String): RequestBody? {
        return RequestBody.create(MediaType.parse(mimeType), file)
    }

    private fun callNotificationCategoriesApi(){
        ll_loading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelNotificationCategories.notificationCategories()
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }


}