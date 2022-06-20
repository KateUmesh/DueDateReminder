package com.duedatereminder.view.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.adapter.NotificationTemplatesAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationTemplates

class NotificationTemplatesActivity : AppCompatActivity(), SnackBarCallback {
    lateinit var rvNotificationTemplates: RecyclerView
    private lateinit var mViewModelNotificationTemplates: ViewModelNotificationTemplates
    private lateinit var llLoading : LinearLayoutCompat
    private var idNotificationCategory:Int=0
    private var SEND_SMS_DETAILS:String=""
    private var SEND_EMAIL_DETAILS:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_templates)

        /**Toolbar*/
        toolbar(getString(R.string.notification_templates),true)

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
        mViewModelNotificationTemplates = ViewModelProvider(this)[ViewModelNotificationTemplates::class.java]

        /**Call NotificationCategories GET Api*/
        callGetNotificationTemplatesApi()

        /**Response of SendLoginOtp Api*/
        mViewModelNotificationTemplates.mModelNotificationTemplatesResponse.observe(this, {
            llLoading.visibility = View.GONE
            when (it.status) {
                "1" -> {

                    if (!it.data!!.templates.isNullOrEmpty()) {
                        val mAdapter = NotificationTemplatesAdapter(this,it.data!!.templates!!,idNotificationCategory.toString(),SEND_SMS_DETAILS,SEND_EMAIL_DETAILS)
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

    private fun callGetNotificationTemplatesApi(){
        llLoading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelNotificationTemplates.getNotificationTemplates(idNotificationCategory)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        mViewModelNotificationTemplates.getNotificationTemplates(idNotificationCategory)
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }
}