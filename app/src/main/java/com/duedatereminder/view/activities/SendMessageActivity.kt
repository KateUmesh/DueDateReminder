package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Observer
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.ModelSendEmailNotificationRequest
import com.duedatereminder.model.ModelSendSmsNotificationRequest
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelClientDetailsToSendNotification

class SendMessageActivity : AppCompatActivity(),SnackBarCallback {
    private lateinit var btnSendSms: Button
    private lateinit var btnSendEmail: Button
    private lateinit var tvTemplate: TextView
    private var idNotification:String=""
    private var template:String=""
    private var idNotificationCategory:String=""
    private lateinit var llLoading : LinearLayoutCompat
    private lateinit var mViewModelClientDetailsToSendNotification: ViewModelClientDetailsToSendNotification
    private var flag:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)

        /**Toolbar*/
        toolbar(getString(R.string.send_message),true)

        /**Initialize Variable*/
        btnSendSms = findViewById(R.id.btnSendSms)
        btnSendEmail = findViewById(R.id.btnSendEmail)
        llLoading = findViewById(R.id.ll_loading)
        tvTemplate = findViewById(R.id.tvTemplate)

        /**Get idNotification from NotificationTemplatesAdapter*/
        if(intent.getStringExtra(Constant.ID_NOTIFICATION)!=null) {
            idNotification = intent.getStringExtra(Constant.ID_NOTIFICATION)!!
        }
        if(intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!=null) {
            idNotificationCategory = intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!!
        }
        if(intent.getStringExtra(Constant.TEMPLATE)!=null) {
            template = intent.getStringExtra(Constant.TEMPLATE)!!

        }

        /**Set Template*/
        tvTemplate.text = template

        /**Send Sms Click*/
        btnSendSms.setOnClickListener {
            callSendSmsNotificationPostApi(idNotificationCategory,idNotification)
        }

        /**Send Email Click*/
        btnSendEmail.setOnClickListener {
            callSendEmailNotificationPostApi(idNotificationCategory,idNotification)
        }

        /** Response of SendSms Api*/
        mViewModelClientDetailsToSendNotification.mModelSendSmsNotificationResponse.observe(this, Observer {
            llLoading.visibility = View.GONE
            when(it.status){
                "1"->{

                }
                "0"->{

                }
                else->{
                    showSnackBar(this,getString(R.string.no_internet_connection))
                }
            }

        })

        /** Response of SendEmail Api*/
        mViewModelClientDetailsToSendNotification.mModelSendSmsNotificationResponse.observe(this, Observer {
            llLoading.visibility = View.GONE
            when(it.status){
                "1"->{

                }
                "0"->{

                }
                else->{
                    showSnackBar(this,getString(R.string.no_internet_connection))
                }
            }

        })
    }

    private fun callSendSmsNotificationPostApi(idDueDateCategory:String,idNotification:String){
        flag=2
        llLoading.visibility = View.VISIBLE
        val mModelSendSmsNotificationRequest = ModelSendSmsNotificationRequest(idDueDateCategory,idNotification)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelClientDetailsToSendNotification.sendSmsNotification(mModelSendSmsNotificationRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    private fun callSendEmailNotificationPostApi(idDueDateCategory:String,idNotification:String){
        flag=3
        llLoading.visibility = View.VISIBLE
        val mModelSendEmailNotificationRequest = ModelSendEmailNotificationRequest(idDueDateCategory,idNotification)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelClientDetailsToSendNotification.sendEmailNotification(mModelSendEmailNotificationRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        when(flag){
            2->{
                callSendEmailNotificationPostApi(idNotificationCategory,idNotification)
            }
            3->{
                callSendEmailNotificationPostApi(idNotificationCategory,idNotification)
            }
        }
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }
}