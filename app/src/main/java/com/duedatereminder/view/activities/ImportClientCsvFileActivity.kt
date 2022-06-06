package com.duedatereminder.view.activities

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.UploadFileModel
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.toast
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.FilePath
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelImportClientCsvFile
import com.google.android.gms.common.util.IOUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*


class ImportClientCsvFileActivity : AppCompatActivity(), SnackBarCallback {
    lateinit var mViewModelImportClientCsvFile: ViewModelImportClientCsvFile
    private lateinit var ll_loading : LinearLayoutCompat
    private lateinit var tvCsvFile : TextView
    private lateinit var btnUploadCsvFile : Button
    private lateinit var finalUri : Uri
    private val PICK_DOCUMENT_REQUEST = 23
    var arrayUri: java.util.ArrayList<UploadFileModel>? = null
    private var fileName: String? = null
    private var idDueDateCategory:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_client_csv_file)

        /**Toolbar*/
        toolbar(getString(R.string.selectCsvFile),true)

        /**Get idDueDateCategory from NotificationCategoriesAdapter*/
        idDueDateCategory = intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!!

        /**Initialize Variables*/
        ll_loading = findViewById(R.id.ll_loading)
        tvCsvFile = findViewById(R.id.tvCsvFile)
        btnUploadCsvFile = findViewById(R.id.btnUploadCsvFile)

        /**Initialize View Model*/
        mViewModelImportClientCsvFile = ViewModelProvider(this)[ViewModelImportClientCsvFile::class.java]


        /**TV CSV File Click*/
        tvCsvFile.setOnClickListener {

            openDocumentPicker()
        }

        /**Button Upload CSV File Click*/
        btnUploadCsvFile.setOnClickListener {
            uploadCSVFile(idDueDateCategory,finalUri)
        }
    }


    private fun uploadCSVFile(idDueDateCategory:String,uri: Uri) {

        //creating a file
        //creating a file
        val file1 = File(getFileName1(uri)!!)

        Log.e("fileUri11",file1.toString())
            val originalFile = File(uri.path!!)
            //tvCsvFile.text = getFileName(uri)
            val filePart: RequestBody = RequestBody.create(MediaType.parse("*/*"), file1)
            val file: MultipartBody.Part = MultipartBody.Part.createFormData("csv_file", getFileName(uri), filePart)
        val idDueDateCategoryRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"),idDueDateCategory)
        ll_loading.visibility = View.VISIBLE
        if (NetworkConnection.isNetworkConnected()) {
            mViewModelImportClientCsvFile.importClientCsvFile(idDueDateCategoryRequestBody,file)
        } else {
            showSnackBar(this,getString(R.string.no_internet_connection))
        }

        mViewModelImportClientCsvFile.mImportClientCsvFileLiveData.observe(this, androidx.lifecycle.Observer {
            ll_loading.visibility = View.GONE
            if (it.status == "1") {
               toast(it.message)
            }
        })

    }

    private fun openDocumentPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            /**
             * It's possible to limit the types of files by mime-type. Since this
             * app displays pages from a PDF file, we'll specify `application/pdf`
             * in `type`.
             * See [Intent.setType] for more details.
             */
            //type = "application/*"
            //type = "text/*"
            type = "application/pdf"

            /**
             * Because we'll want to use [ContentResolver.openFileDescriptor] to read
             * the data of whatever file is picked, we set [Intent.CATEGORY_OPENABLE]
             * to ensure this will succeed.
             */
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, PICK_DOCUMENT_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == PICK_DOCUMENT_REQUEST) {
                    if (null != data) { // checking empty selection
                        if (null != data.clipData) { // checking multiple selection or not
                            arrayUri = java.util.ArrayList()
                            for (i in 0 until data.clipData!!.itemCount) {
                                val uri = data.clipData!!.getItemAt(i).uri
                                var filePath: String = FilePath.getPath(this, uri)
                                filePath = filePath.substring(filePath.lastIndexOf("/") + 1)
                               // filePath = FilePath.PATH_MEDIA_IMAGES.toString() + "/" + filePath
                                val file = File(filePath)
                                val photo = BitmapFactory.decodeFile(uri.path)
                                var out: FileOutputStream? = null
                                try {
                                    out = FileOutputStream(file)
                                    photo.compress(Bitmap.CompressFormat.JPEG, 20, out)
                                    out.flush()
                                    out.close()
                                } catch (e: FileNotFoundException) {
                                    e.printStackTrace()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }
                        } else {
                            //isFilesSend = true
                            val uri = data.data
                            Log.e("base64",getStringPdf(data.data!!)!!)
                            Log.e("fileName",getFileName(uri!!)!!)
                            arrayUri = java.util.ArrayList()
                            val filePath = uri.toString()
                            val file = File(uri.toString())
                            var out: FileOutputStream? = null
                            try {
                                out = FileOutputStream(file)
                                out.flush()
                                out.close()
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            fileName = file.name
                            tvCsvFile.text = getFileName1(uri!!)
                            val file1 = File(getFileName1(uri)!!)
                            Log.e("fileUri",file1.toString())
                            finalUri = uri!!
                            //Log.e("fileName",file.name)
                            //Log.e("fileName",FileUtils.getPath(data.data))
                        }
                    } else {
                        Toast.makeText(this, "You haven't picked File", Toast.LENGTH_LONG).show()
                    }
                }
            } else {

            }
        } else {

        }

    }

    /*private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }*/



    override fun snackBarSuccessInternetConnection() {

    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }

    /*public override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            resultData?.let { intent ->
                finalUri = resultData.data!!
                val file = File(finalUri.toString())

                //tvCsvFile.text = readCSV(resultData.data!!).joinToString(separator = "\n")
            }
        }
    }*/

    @Throws(IOException::class)
    fun readCSV(uri: Uri): List<String> {
        val csvFile = contentResolver.openInputStream(uri)
        val isr = InputStreamReader(csvFile)
        return BufferedReader(isr).readLines()
    }
    @SuppressLint("Range")
    fun getFileName1(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            var cursor: Cursor? =contentResolver.query(uri,null,null,null,null)
            try{
                if(cursor!=null&&cursor.moveToFirst()){
                    result=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))

                }
            }finally {
                if(cursor!=null){
                    cursor.close()
                }
            }

        }
        if(result==null){
            result=uri.lastPathSegment!!
        }

        return result
    }
    companion object {
        const val READ_REQUEST_CODE = 123
        const val FILE_PICKER_REQUEST_CODE = 1
    }

    fun getStringPdf(filepath: Uri?): String? {
        var inputStream: InputStream? = null
        var byteArrayOutputStream = ByteArrayOutputStream()
        try {
            inputStream = contentResolver.openInputStream(filepath!!)
            val buffer = ByteArray(1024)
            byteArrayOutputStream = ByteArrayOutputStream()
            var bytesRead: Int
            while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        val pdfByteArray = byteArrayOutputStream.toByteArray()
        return android.util.Base64.encodeToString(pdfByteArray, android.util.Base64.DEFAULT)
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        //val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, contentUri, null, null, null, null)
        val cursor: Cursor? = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME).toString())
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }

    fun getFilePathFromURI(context: Context, contentUri: Uri?): String? {
        val folder: File
        folder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString() + "/" + "duedatereminder"
            )
        } else {
            File(Environment.getExternalStorageDirectory().toString() + "/" + "duedatereminder")
        }
        // if you want to save the copied image temporarily for further process use .TEMP folder otherwise use your app folder where you want to save
        // if you want to save the copied image temporarily for further process use .TEMP folder otherwise use your app folder where you want to save
        val TEMP_DIR_PATH = folder.absolutePath.toString() + "/.TEMP_CAMERA.xxx"
        //copy file and send new file path
        val fileName = getFileName(contentUri)
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile = File(TEMP_DIR_PATH + File.separator.toString() + fileName)
            copy(context, contentUri, copyFile)
            return copyFile.absolutePath
        }
        return null
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

    fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
        try {
            val inputStream: InputStream = context.getContentResolver().openInputStream(srcUri!!)
                ?: return
            val outputStream: OutputStream = FileOutputStream(dstFile)
            IOUtils.copyStream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}