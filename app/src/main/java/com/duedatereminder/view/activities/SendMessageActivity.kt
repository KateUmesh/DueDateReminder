package com.duedatereminder.view.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.ModelSendEmailNotificationRequest
import com.duedatereminder.model.ModelSendSmsCostRequest
import com.duedatereminder.model.ModelSendSmsNotificationRequest
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension.Companion.showOkDialog
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.snackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelSendMessage

class SendMessageActivity : AppCompatActivity(),SnackBarCallback {
    private lateinit var btnSendSms: Button
    private lateinit var btnSendEmail: Button
    private lateinit var tvTemplate: TextView
    private var idNotification:String=""
    private var template:String=""
    private var SEND_SMS_DETAILS:String=""
    private var SEND_EMAIL_DETAILS:String=""
    private var idNotificationCategory:String=""
    private  var  mClientIdList=ArrayList<String>()
    private lateinit var llLoading : LinearLayoutCompat
    private lateinit var mViewModelSendMessage: ViewModelSendMessage
    private var flag:Int=1
    lateinit var progressDialog:ProgressDialog
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
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...")

        /**Initialize View Model*/
        mViewModelSendMessage = ViewModelProvider(this)[ViewModelSendMessage::class.java]

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
        if(intent.getStringExtra(Constant.SEND_SMS_DETAILS)!=null) {
            SEND_SMS_DETAILS = intent.getStringExtra(Constant.SEND_SMS_DETAILS)!!

        }
        if(intent.getStringExtra(Constant.SEND_EMAIL_DETAILS)!=null) {
            SEND_EMAIL_DETAILS = intent.getStringExtra(Constant.SEND_EMAIL_DETAILS)!!

        }
        if(intent.getStringArrayListExtra(Constant.ID_CLIENT_LIST)!=null) {
            mClientIdList = intent.getStringArrayListExtra(Constant.ID_CLIENT_LIST)!!
        }

        /**Set Template*/
        tvTemplate.text = template

        /**Send Sms Click*/
        btnSendSms.setOnClickListener {
            //smsAlertDialog(SEND_SMS_DETAILS)
            callSendSmsCostPostApi(idNotificationCategory,idNotification)
        }

        /**Send Email Click*/
        btnSendEmail.setOnClickListener {
            emailAlertDialog(SEND_EMAIL_DETAILS)
        }

        /** Response of SendSms Api*/
        mViewModelSendMessage.mModelSendSmsNotificationResponse.observe(this, Observer {
            //llLoading.visibility = View.GONE
            progressDialog.dismiss()
            when(it.status){
                "1"->{
                    showOkDialog(it.message,this)
                }
                "0"->{
                   snackBar(it.message, this)
                }
                else->{
                    showSnackBar(this,getString(R.string.no_internet_connection))
                }
            }

        })

        /** Response of SendEmail Api*/
        mViewModelSendMessage.mModelSendSmsNotificationResponse.observe(this, Observer {
            progressDialog.dismiss()
           // llLoading.visibility = View.GONE
            when(it.status){
                "1"->{
                    showOkDialog(it.message,this)
                }
                "0"->{
                    snackBar(it.message, this)
                }
                else->{
                    showSnackBar(this,getString(R.string.no_internet_connection))
                }
            }

        })


        /** Response of SendSmsCost Api*/
        mViewModelSendMessage.mModelSendSmsCostResponse.observe(this, Observer {
            llLoading.visibility = View.GONE
            when(it.status){
                "1"->{
                    smsAlertDialog(it.message)
                }
                "0"->{
                    snackBar(it.message, this)
                }
                else->{
                    showSnackBar(this,getString(R.string.no_internet_connection))
                }
            }

        })
    }

    private fun callSendSmsNotificationPostApi(idDueDateCategory:String,idNotification:String){
        flag=2
        //llLoading.visibility = View.VISIBLE
        progressDialog.show()
        val mModelSendSmsNotificationRequest = ModelSendSmsNotificationRequest(idDueDateCategory,idNotification,mClientIdList)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelSendMessage.sendSmsNotification(mModelSendSmsNotificationRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    private fun callSendEmailNotificationPostApi(idDueDateCategory:String,idNotification:String){
        flag=3
        //llLoading.visibility = View.VISIBLE
        progressDialog.show()
        val mModelSendEmailNotificationRequest = ModelSendEmailNotificationRequest(idDueDateCategory,idNotification,mClientIdList)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelSendMessage.sendEmailNotification(mModelSendEmailNotificationRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }


    private fun callSendSmsCostPostApi(idDueDateCategory:String,idNotification:String){
        flag=4
        llLoading.visibility = View.VISIBLE
        val mModelSendSmsCostRequest = ModelSendSmsCostRequest(idDueDateCategory,idNotification,mClientIdList)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelSendMessage.sendSmsCost(mModelSendSmsCostRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        when(flag){
            2->{
                callSendSmsNotificationPostApi(idNotificationCategory,idNotification)
            }
            3->{
                callSendEmailNotificationPostApi(idNotificationCategory,idNotification)
            }
            4->{
                callSendSmsCostPostApi(idNotificationCategory,idNotification)
        }
        }
    }

    private fun smsAlertDialog(message:String){
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle(getString(R.string.send_sms))
        mBuilder.setMessage(message)

        mBuilder.setPositiveButton(getString(R.string.send), DialogInterface.OnClickListener { dialogInterface, i ->
            callSendSmsNotificationPostApi(idNotificationCategory,idNotification)
            dialogInterface.dismiss()
        })

        mBuilder.setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialogInterface, i ->

            dialogInterface.dismiss()
        })


        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun emailAlertDialog(message:String){
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle(getString(R.string.send_email))
        mBuilder.setMessage(message)

        mBuilder.setPositiveButton(getString(R.string.send), DialogInterface.OnClickListener { dialogInterface, i ->
            callSendEmailNotificationPostApi(idNotificationCategory,idNotification)
            dialogInterface.dismiss()
        })

        mBuilder.setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialogInterface, i ->

            dialogInterface.dismiss()
        })


        val mDialog = mBuilder.create()
        mDialog.show()
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }

    fun showOKDialog(message: String,context: Context) {
        val builder =
            AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.app_name) as CharSequence)
        builder.setMessage(message)
        builder.setPositiveButton(
            R.string.Ok
        ) { _, _ -> }
        builder.show()
    }
}