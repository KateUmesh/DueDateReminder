package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.adapter.ClientDetailsToSendNotificationAdapter
import com.duedatereminder.adapter.NotificationTemplatesAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.ClientsList
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelClientDetailsToSendNotification
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationTemplates

class ClientDetailsToSendNotificationsActivity : AppCompatActivity(), SnackBarCallback,ClientDetailsToSendNotificationAdapter.SendSmsClickListener,ClientDetailsToSendNotificationAdapter.SendEmailClickListener {
    lateinit var rvClientDetails: RecyclerView
    private lateinit var mViewModelClientDetailsToSendNotification: ViewModelClientDetailsToSendNotification
    private lateinit var llLoading : LinearLayoutCompat
    private var idNotificationCategory:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_details_to_send_notifications)

        /**Toolbar*/
        toolbar(getString(R.string.client_details),true)

        /**Get idNotification from NotificationTemplatesAdapter*/
        if(intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!=null) {
            idNotificationCategory = intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!!.toInt()
        }

        /**Initialize Variables*/
        rvClientDetails = findViewById(R.id.rvClientDetails)
        llLoading = findViewById(R.id.ll_loading)

        llLoading.visibility = View.VISIBLE

        /**Initialize View Model*/
        mViewModelClientDetailsToSendNotification = ViewModelProvider(this)[ViewModelClientDetailsToSendNotification::class.java]

        /**Call ClientDetailsToSendNotifications GET Api*/
        callGetClientDetailsToSendNotificationsApi()

        /**Response of SendLoginOtp Api*/
            mViewModelClientDetailsToSendNotification.mModelClientDetailsToSendNotificationsResponse.observe(this, {
            llLoading.visibility = View.GONE
            when (it.status) {
                "1" -> {

                    if (!it.data!!.clients.isNullOrEmpty()) {
                        val mAdapter = ClientDetailsToSendNotificationAdapter(this,it.data!!.clients!!,this,this)
                        rvClientDetails.adapter=mAdapter
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

    private fun callGetClientDetailsToSendNotificationsApi(){
        llLoading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelClientDetailsToSendNotification.getClientDetailsToSendNotifications(idNotificationCategory)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        mViewModelClientDetailsToSendNotification.getClientDetailsToSendNotifications(idNotificationCategory)
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }

    override fun sendSmsClick(position: Int, items: ArrayList<ClientsList>) {

    }

    override fun sendEmailClick(position: Int, items: ArrayList<ClientsList>) {

    }
}