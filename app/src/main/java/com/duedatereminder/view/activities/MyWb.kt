package com.duedatereminder.view.activities

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.duedatereminder.R
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.LocalSharedPreference


class MyWb : AppCompatActivity() {
    var web: WebView? = null
    var progressBar: ProgressBar? = null

    private var mUploadMessage: ValueCallback<Uri?>? = null
    private val FILECHOOSER_RESULTCODE = 1

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        intent: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return
            val result: Uri? =
                if (intent == null || resultCode != RESULT_OK) null else intent.getData()
            mUploadMessage!!.onReceiveValue(result)
            mUploadMessage = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wb)

        web = findViewById<View>(R.id.webview01) as WebView
        progressBar = findViewById<View>(R.id.progressBar1) as ProgressBar

        web = WebView(this)
        web!!.settings.javaScriptEnabled = true
        //web!!.loadUrl("http://www.script-tutorials.com/demos/199/index.html")
        web!!.loadUrl(" https://kuberaduedate.com/app/welcome/csv_file_upload/"+ LocalSharedPreference.getStringValue(Constant.token))
        web!!.webViewClient = myWebClient()
        web!!.webChromeClient = object : WebChromeClient() {
            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            fun openFileChooser(uploadMsg: ValueCallback<Uri?>) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.setType("image/*")
                this@MyWb.startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE
                )
            }

            // For Android 3.0+
            fun openFileChooser(uploadMsg: ValueCallback<Uri?>, acceptType: String?) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.setType("*/*")
                this@MyWb.startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE
                )
            }

            //For Android 4.1
            fun openFileChooser(
                uploadMsg: ValueCallback<Uri?>,
                acceptType: String?,
                capture: String?
            ) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.setType("image/*")
                this@MyWb.startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE
                )
            }
        }


        setContentView(web)
    }

    class myWebClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // TODO Auto-generated method stub
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)

        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

}