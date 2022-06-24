package com.duedatereminder.view.activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SearchView
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
import com.duedatereminder.utils.ContextExtension.Companion.toast
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelClientDetailsToSendNotification
import com.duedatereminder.viewModel.activityViewModel.ViewModelNotificationTemplates


class ClientDetailsToSendNotificationsActivity : AppCompatActivity(), SnackBarCallback {
    lateinit var rvClientDetails: RecyclerView
    private lateinit var mViewModelClientDetailsToSendNotification: ViewModelClientDetailsToSendNotification
    private lateinit var mViewModelNotificationTemplates: ViewModelNotificationTemplates
    private lateinit var llLoading : LinearLayoutCompat
    private lateinit var llClientDetails : LinearLayout
    private var idNotification:String=""
    private var notificationMessage:String=""
    private var idNotificationCategory:String=""
    private var SEND_SMS_DETAILS:String=""
    private var SEND_EMAIL_DETAILS:String=""
    private lateinit var tvTotalClients:TextView
    private lateinit var btnSendMessage:Button
    private lateinit var nsClientDetails:NestedScrollView
    private var flag:Int=1
    private var templateList = ArrayList<NotificationTemplates>()
    private var mSelectedTemplate = -1
    private lateinit var tvNoData:TextView
    lateinit var mAdapter:ClientDetailsToSendNotificationAdapter
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_details_to_send_notifications)

        /**Get idNotification from NotificationTemplatesAdapter*/
        if(intent.getStringExtra(Constant.CATEGORY_NAME)!=null) {
            /**Toolbar*/
            toolbar(intent.getStringExtra(Constant.CATEGORY_NAME)!!,true)
        }
        if(intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!=null) {
            idNotificationCategory = intent.getStringExtra(Constant.ID_DUE_DATE_CATEGORY)!!
        }

        /**Initialize Variables*/
        rvClientDetails = findViewById(R.id.rvClientDetails)
        llLoading = findViewById(R.id.ll_loading)
        tvTotalClients = findViewById(R.id.tvTotalClients)
        btnSendMessage = findViewById(R.id.btnSendMessage)
        nsClientDetails = findViewById(R.id.nsClientDetails)
        llClientDetails = findViewById(R.id.llClientDetails)
        tvNoData = findViewById(R.id.tvNoData)

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
                    if (!it.data!!.clients.isNullOrEmpty()) {
                        tvNoData.visibility = View.GONE
                         mAdapter = ClientDetailsToSendNotificationAdapter(this,it.data!!.clients!!)
                        rvClientDetails.adapter=mAdapter
                        llClientDetails.visibility = View.VISIBLE
                        if(!it.data?.send_sms_details.isNullOrEmpty()) {
                            SEND_SMS_DETAILS = it.data?.send_sms_details!!
                        }
                        if(!it.data?.send_email_details.isNullOrEmpty()) {
                            SEND_EMAIL_DETAILS = it.data?.send_email_details!!
                        }
                    }else{
                        tvNoData.visibility = View.VISIBLE
                    }
                }
                "0" -> {
                    tvNoData.visibility = View.VISIBLE
                    ContextExtension.snackBar(it.message, this)
                }
                else -> {
                    showSnackBar(this, it.message)
                }
            }
        })

        /**Send Sms Button Click*/
        btnSendMessage.setOnClickListener {
            /**Call NotificationCategories GET Api*/
            //callGetNotificationTemplatesApi()
            val intent = Intent(this, NotificationTemplatesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.ID_DUE_DATE_CATEGORY,idNotificationCategory)
            intent.putExtra(Constant.SEND_SMS_DETAILS,SEND_SMS_DETAILS)
            intent.putExtra(Constant.SEND_EMAIL_DETAILS,SEND_EMAIL_DETAILS)
            startActivity(intent)
        }




        /**Response of Notification Templates Api*/
        mViewModelNotificationTemplates.mModelNotificationTemplatesResponse.observe(this, {
            llLoading.visibility = View.GONE
            when (it.status) {
                "1" -> {

                    if (!it.data!!.templates.isNullOrEmpty()) {
                        templateList = it.data?.templates as ArrayList<NotificationTemplates>

                        val values = ArrayList<String>()
                        val key = ArrayList<String>()
                        for (i in this.templateList.indices) {
                            values.add(this.templateList[i].message)
                            key.add(this.templateList[i].id_notification)
                        }
                        val mBuilder = AlertDialog.Builder(this)
                        mBuilder.setTitle(getString(R.string.select_template))

                        mBuilder.setSingleChoiceItems(values.toTypedArray(), mSelectedTemplate) { dialogInterface, i ->
                            mSelectedTemplate = i
                            idNotification = key[i]
                            notificationMessage=values[i]
                        }

                        mBuilder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                                if(idNotification.isNotEmpty()) {
                                    val intent = Intent(this, SendMessageActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    intent.putExtra(Constant.ID_NOTIFICATION, idNotification)
                                    intent.putExtra(Constant.ID_DUE_DATE_CATEGORY, idNotificationCategory)
                                    intent.putExtra(Constant.TEMPLATE, notificationMessage)
                                    startActivity(intent)
                                    dialogInterface.dismiss()
                                }else{
                                    toast("Please Select Template")
                                }

                            })


                        val mDialog = mBuilder.create()
                        mDialog.show()
                    }else{

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
        flag=2
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


    override fun snackBarSuccessInternetConnection() {
        when(flag){
            1->{
                mViewModelClientDetailsToSendNotification.getClientDetailsToSendNotifications(idNotificationCategory.toInt())
            }
            2->{
                callGetNotificationTemplatesApi()
            }

        }

    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val findItem = menu?.findItem(R.id.action_Search)
        val  searchView: SearchView = findItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                mAdapter.filter.filter(newText)
                return true
            }
        })
        return true
    }

}