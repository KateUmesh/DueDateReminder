package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.LinearLayoutCompat
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.ModelSendEmailNotificationRequest
import com.duedatereminder.model.ModelSendSmsNotificationRequest
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelClientDetailsToSendNotification

class SendMessageActivity : AppCompatActivity(),SnackBarCallback {
    private lateinit var btnSendSms: Button
    private lateinit var btnSendEmail: Button
    private var idNotification:String=""
    private var idNotificationCategory:String=""
    private lateinit var llLoading : LinearLayoutCompat
    private lateinit var mViewModelClientDetailsToSendNotification: ViewModelClientDetailsToSendNotification
    private var flag:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)

        /**Get idNotification from NotificationTemplatesAdapter*/
        if(intent.getStringExtra(Constant.ID_NOTIFICATION)!=null) {
            idNotification = intent.getStringExtra(Constant.ID_NOTIFICATION)!!
        }
        if(intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!=null) {
            idNotificationCategory = intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!!
        }

        btnSendSms = findViewById(R.id.btnSendSms)
        btnSendEmail = findViewById(R.id.btnSendEmail)
        llLoading = findViewById(R.id.ll_loading)

        /**Send Sms Click*/
        btnSendSms.setOnClickListener {
            callSendSmsNotificationPostApi(idNotificationCategory,idNotification)
        }

        /**Send Email Click*/
        btnSendEmail.setOnClickListener {
            callSendEmailNotificationPostApi(idNotificationCategory,idNotification)
        }
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