package com.duedatereminder.view.activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.duedatereminder.R
import com.duedatereminder.adapter.ClientDetailsToSendNotificationAdapter
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.*
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showOkDialog
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelClientDetailsToSendNotification
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationTemplates
import org.jetbrains.anko.toast


class ClientDetailsToSendNotificationsActivity : AppCompatActivity(), SnackBarCallback,ClientDetailsToSendNotificationAdapter.SendSmsClickListener,ClientDetailsToSendNotificationAdapter.SendEmailClickListener {
    lateinit var rvClientDetails: RecyclerView
    private lateinit var mViewModelClientDetailsToSendNotification: ViewModelClientDetailsToSendNotification
    private lateinit var mViewModelNotificationTemplates: ViewModelNotificationTemplates
    private lateinit var llLoading : LinearLayoutCompat
    private var idNotification:String=""
    private var idNotificationCategory:String=""
    private lateinit var tvTotalClients:TextView
    private lateinit var btnSendSms:Button
    private lateinit var btnSendEmail:Button
    private lateinit var nsClientDetails:NestedScrollView
    private var flag:Int=1
    private var maritalStatusList = ArrayList<NotificationTemplates>()
    private var mSelectedMaritalStatus = -1
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_details_to_send_notifications)

        /**Toolbar*/
        toolbar(getString(R.string.client_details),true)

        /**Get idNotification from NotificationTemplatesAdapter*/
        /*if(intent.getStringExtra(Constant.ID_NOTIFICATION)!=null) {
            idNotification = intent.getStringExtra(Constant.ID_NOTIFICATION)!!
        }*/
        if(intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!=null) {
            idNotificationCategory = intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!!
        }

        /**Initialize Variables*/
        rvClientDetails = findViewById(R.id.rvClientDetails)
        llLoading = findViewById(R.id.ll_loading)
        tvTotalClients = findViewById(R.id.tvTotalClients)
        btnSendSms = findViewById(R.id.btnSendSms)
        //btnSendEmail = findViewById(R.id.btnSendEmail)
        nsClientDetails = findViewById(R.id.nsClientDetails)

        llLoading.visibility = View.VISIBLE

        /**Initialize View Model*/
        mViewModelClientDetailsToSendNotification = ViewModelProvider(this)[ViewModelClientDetailsToSendNotification::class.java]
        mViewModelNotificationTemplates = ViewModelProvider(this)[ViewModelNotificationTemplates::class.java]

        /**Call ClientDetailsToSendNotifications GET Api*/
        callGetClientDetailsToSendNotificationsApi()

        /**Response of ClientDetailsToSendNotifications Api*/
        mViewModelClientDetailsToSendNotification.mModelClientDetailsToSendNotificationsResponse.observe(this, {
            llLoading.visibility = View.GONE
            when (it.status) {
                "1" -> {

                    /*Set Total clients*/
                    if(it.data!!.totalClients.isNotEmpty()){
                        tvTotalClients.text = getString(R.string.total_clients)+it.data!!.totalClients
                    }

                    if (!it.data!!.clients.isNullOrEmpty()) {
                        val mAdapter = ClientDetailsToSendNotificationAdapter(this,it.data!!.clients!!,this,this)
                        rvClientDetails.adapter=mAdapter
                        nsClientDetails.visibility = View.VISIBLE
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

        /**Send Sms Click*/
        btnSendSms.setOnClickListener {
            //callSendSmsNotificationPostApi(idNotificationCategory,idNotification)
            /**Call NotificationCategories GET Api*/
            callGetNotificationTemplatesApi()
        }


        /**Response of SendSmsNotification Api*/
        mViewModelClientDetailsToSendNotification.mModelSendSmsNotificationResponse.observe(this, {
            llLoading.visibility = View.GONE
            when (it.status) {
                "1" -> {

                    if (it.message.isNotEmpty()) {
                        showOkDialog(it.message,this)
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

        /**Send Email Click*//*
        btnSendEmail.setOnClickListener {
            callSendEmailNotificationPostApi(idNotificationCategory,idNotification)
        }*/

        /**Response of SendEmailNotification Api*/
        mViewModelClientDetailsToSendNotification.mModelSendEmailNotificationResponse.observe(this, {
            llLoading.visibility = View.GONE
            when (it.status) {
                "1" -> {

                    if (it.message.isNotEmpty()) {
                        showOkDialog(it.message,this)
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

        /**Initialize View Model*/
        mViewModelNotificationTemplates = ViewModelProvider(this)[ViewModelNotificationTemplates::class.java]



        /**Response of Notification Templates Api*/
        mViewModelNotificationTemplates.mModelNotificationTemplatesResponse.observe(this, {
            llLoading.visibility = View.GONE
            when (it.status) {
                "1" -> {

                    if (!it.data!!.templates.isNullOrEmpty()) {
                        maritalStatusList = it.data?.templates as ArrayList<NotificationTemplates>

                        val values = ArrayList<String>()
                        val key = ArrayList<String>()
                        for (i in this.maritalStatusList.indices) {
                            values.add(this.maritalStatusList[i].message)
                            key.add(this.maritalStatusList[i].id_notification)
                        }
                        val mBuilder = AlertDialog.Builder(this)
                        mBuilder.setTitle(getString(R.string.select_template))

                        mBuilder.setSingleChoiceItems(values.toTypedArray(), mSelectedMaritalStatus) { dialogInterface, i ->
                            mSelectedMaritalStatus = i
                            idNotification = key[i]
                            dialogInterface.dismiss()
                        }
                        mBuilder.setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->
                                if(idNotification.isNotEmpty()) {
                                    val intent = Intent(this, SendMessageActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    intent.putExtra(Constant.ID_NOTIFICATION, idNotification)
                                    intent.putExtra(Constant.ID_DUE_DATE_CATEGORY, idNotificationCategory)
                                    startActivity(intent)
                                }else{
                                    toast("Please Select Template")
                                }
                            })

                        val mDialog = mBuilder.create()
                        mDialog.show()
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
            mViewModelNotificationTemplates.getNotificationTemplates(idNotificationCategory.toInt())
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    private fun callGetClientDetailsToSendNotificationsApi(){
        flag=1
        llLoading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelClientDetailsToSendNotification.getClientDetailsToSendNotifications(idNotificationCategory.toInt())
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
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
            1->{
                mViewModelClientDetailsToSendNotification.getClientDetailsToSendNotifications(idNotificationCategory.toInt())
            }
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

    override fun sendSmsClick(position: Int, items: ArrayList<ClientsList>) {
        callSendSmsNotificationPostApi(items[position].id_client,items[position].due_date_categories)
    }

    override fun sendEmailClick(position: Int, items: ArrayList<ClientsList>) {
        callSendEmailNotificationPostApi(items[position].id_client,items[position].due_date_categories)
    }
}