package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.adapter.NotificationCategoriesAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.ModelSendLoginOtpRequest
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationCategories

class NotificationCategoriesActivity : AppCompatActivity(),SnackBarCallback {
    lateinit var rvNotificationCategories:RecyclerView
    lateinit var mViewModelNotificationCategories: ViewModelNotificationCategories
    private lateinit var ll_loading : LinearLayoutCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_categories)

        /**Toolbar*/
        toolbar(getString(R.string.notification_categories),true)

        /**Initialize Variables*/
        rvNotificationCategories = findViewById(R.id.rvNotificationCategories)
        ll_loading = findViewById(R.id.ll_loading)

        ll_loading.visibility = View.VISIBLE

        /**Initialize View Model*/
        mViewModelNotificationCategories = ViewModelProvider(this)[ViewModelNotificationCategories::class.java]


        /**Call NotificationCategories GET Api*/
        callNotificationCategoriesApi()

        /**Response of SendLoginOtp Api*/
        mViewModelNotificationCategories.mModelNotificationCategoriesResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{

                    if(!it.data!!.due_date_categories.isNullOrEmpty()){
                        val mAdapter = NotificationCategoriesAdapter(this,it.data!!.due_date_categories!!)
                        rvNotificationCategories.adapter=mAdapter
                    }
                }
                "0"->{
                    ContextExtension.snackBar(it.message, this)
                }
                else->{
                    showSnackBar(this,it.message)
                }
            }
        })

    }

    private fun callNotificationCategoriesApi(){
        ll_loading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelNotificationCategories.notificationCategories()
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        mViewModelNotificationCategories.notificationCategories()
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }
}