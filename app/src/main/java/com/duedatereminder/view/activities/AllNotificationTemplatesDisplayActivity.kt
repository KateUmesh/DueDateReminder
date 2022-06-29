package com.duedatereminder.view.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.adapter.AllNotificationTemplatesDisplayAdapter
import com.duedatereminder.adapter.NotificationTemplatesAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelAllNotificationTemplatesDisplay

class AllNotificationTemplatesDisplayActivity : AppCompatActivity(), SnackBarCallback {
    lateinit var rvNotificationTemplates: RecyclerView
    private lateinit var mViewModelAllNotificationTemplatesDisplay: ViewModelAllNotificationTemplatesDisplay
    private lateinit var llLoading : LinearLayoutCompat
    private var idNotificationCategory:Int=0
    private var SEND_SMS_DETAILS:String=""
    private var SEND_EMAIL_DETAILS:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_all_notification_templates_display)

        /**Toolbar*/
        toolbar(getString(R.string.select_message),true)

        /**Get idNotification from NotificationCategoriesFragmentAdapter*/
        if(intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!=null) {
            idNotificationCategory = intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!!.toInt()
        }
        if(intent.getStringExtra(Constant.SEND_SMS_DETAILS)!=null) {
            SEND_SMS_DETAILS = intent.getStringExtra(Constant.SEND_SMS_DETAILS)!!
        }
        if(intent.getStringExtra(Constant.SEND_EMAIL_DETAILS)!=null) {
            SEND_EMAIL_DETAILS = intent.getStringExtra(Constant.SEND_EMAIL_DETAILS)!!
        }

        /**Initialize Variables*/
        rvNotificationTemplates = findViewById(R.id.rvNotificationTemplates)
        llLoading = findViewById(R.id.ll_loading)

        llLoading.visibility = View.VISIBLE

        /**Initialize View Model*/
        mViewModelAllNotificationTemplatesDisplay = ViewModelProvider(this)[ViewModelAllNotificationTemplatesDisplay::class.java]

        /**Call AllNotificationTemplates GET Api*/
        callGetAllNotificationTemplatesDisplayApi()

        /**Response of SendLoginOtp Api*/
        mViewModelAllNotificationTemplatesDisplay.mModelAllNotificationTemplatesDisplayResponse.observe(this, {
            llLoading.visibility = View.GONE
            when (it.status) {
                "1" -> {

                    if (!it.data!!.templates.isNullOrEmpty()) {
                        val mAdapter = AllNotificationTemplatesDisplayAdapter(this,it.data!!.templates!!)
                        rvNotificationTemplates.adapter=mAdapter
                    }
                }
                "0" -> {
                    ContextExtension.snackBar(it.message, this)
                }
                else -> {
                    showSnackBar(this, it.message)
                }
            }
        })
    }

    private fun callGetAllNotificationTemplatesDisplayApi(){
        llLoading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelAllNotificationTemplatesDisplay.getAllNotificationTemplatesDisplay()
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        mViewModelAllNotificationTemplatesDisplay.getAllNotificationTemplatesDisplay()
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }
}