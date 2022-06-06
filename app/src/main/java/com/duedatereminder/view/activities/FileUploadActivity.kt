package com.duedatereminder.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.duedatereminder.R
import com.duedatereminder.utils.FilePath


class FileUploadActivity : AppCompatActivity() {
    private val PICK_FILE_REQUEST = 1
    private val TAG: String = FileUploadActivity::class.java.getSimpleName()
    private var selectedFilePath: String? = null
    private val SERVER_URL = "http://Ip Address/a.php"
    lateinit var ivAttachment: ImageView
    var bUpload: Button? = null
    var tvFileName: TextView? = null
    var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_upload)
        ivAttachment =  findViewById(R.id.ivAttachment);
        bUpload = findViewById(R.id.b_upload);
        tvFileName =  findViewById(R.id.tv_file_name);
        //ivAttachment.setOnClickListener(this);
       // bUpload.setOnClickListener(this);
        ivAttachment.setOnClickListener {
            showFileChooser()
        }
    }


    /*override fun onClick(v: View) {
        if (v === ivAttachment) {

            //on attachment icon click
            showFileChooser()
        }
        if (v === bUpload) {

            //on upload button Click
            if (selectedFilePath != null) {
                dialog = ProgressDialog.show(this@FileUploadActivity, "", "Uploading File...", true)
                Thread { //creating new thread to handle Http Operations
                    //uploadFile(selectedFilePath)
                }.start()
            } else {
                Toast.makeText(this@FileUploadActivity, "Please choose a File First", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }*/

    private fun showFileChooser() {
        val intent = Intent()
        //sets the select file to all types of files
        intent.type = "*/*"
        //allows to select data and return it
        intent.action = Intent.ACTION_GET_CONTENT
        //starts new activity to select file and return data
        startActivityForResult(
            Intent.createChooser(intent, "Choose File to Upload.."),
            PICK_FILE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return
                }
                val selectedFileUri: Uri? = data.data
                selectedFilePath = FilePath.getPath(this, selectedFileUri)
                Log.i(TAG, "Selected File Path:$selectedFilePath")
                if (selectedFilePath != null && selectedFilePath != "") {
                    tvFileName!!.text = selectedFilePath
                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}