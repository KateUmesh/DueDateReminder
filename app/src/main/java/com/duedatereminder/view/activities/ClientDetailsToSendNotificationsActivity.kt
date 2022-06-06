package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationTemplates

class ClientDetailsToSendNotificationsActivity : AppCompatActivity(), SnackBarCallback {
    lateinit var rvClientDetails: RecyclerView
    private lateinit var mViewModelNotificationTemplates: ViewModelNotificationTemplates
    private lateinit var llLoading : LinearLayoutCompat
    private var idNotificationCategory:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_details_to_send_notifications)

        /**Toolbar*/
        toolbar(getString(R.string.client_details),true)
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