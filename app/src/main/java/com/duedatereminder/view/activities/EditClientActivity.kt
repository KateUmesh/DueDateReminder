package com.duedatereminder.view.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.*
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension.Companion.hideKeyboard
import com.duedatereminder.utils.ContextExtension.Companion.showOkFinishActivityDialog
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.snackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelEditClient
import com.google.android.material.textfield.TextInputEditText


class EditClientActivity : AppCompatActivity(), SnackBarCallback {
    private lateinit var tietName : TextInputEditText
    private lateinit var tietEmail : TextInputEditText
    private lateinit var tietMobileNumber : TextInputEditText
    private lateinit var tietWhatsappNumber : TextInputEditText
    private lateinit var tietAddress : TextInputEditText
    private lateinit var edtNotificationCategories : TextInputEditText
    private lateinit var ll_loading : LinearLayoutCompat
    var otp:String = ""
    var idClient:String = ""
    private lateinit var mViewModelEditClient: ViewModelEditClient
    private lateinit var dueDateCategories:ArrayList<String>
    private lateinit var dueDateCategoriesNames:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_client)

        /**Toolbar*/
        toolbar(getString(R.string.edit_client),true)

        /**Initialize variables*/
        tietName  = findViewById(R.id.tietName)
        tietEmail = findViewById(R.id.tietEmail)
        tietMobileNumber = findViewById(R.id.tietMobileNumber)
        tietWhatsappNumber = findViewById(R.id.tietWhatsappNumber)
        tietAddress = findViewById(R.id.tietAddress)
        edtNotificationCategories = findViewById(R.id.edtNotificationCategories)
        ll_loading = findViewById(R.id.ll_loading)
        val btnSubmit : Button = findViewById(R.id.btnSubmit)
        dueDateCategories = ArrayList()
        dueDateCategoriesNames = ArrayList()

        /**Get Client id From AllClientAdapter*/
        if(!intent.getStringExtra(Constant.ID_CLIENT).isNullOrEmpty()){
           idClient =  intent.getStringExtra(Constant.ID_CLIENT)!!
        }

        /**Initialize View Model*/
        mViewModelEditClient = ViewModelProvider(this).get(ViewModelEditClient::class.java)


        /**Call EditClient GET Api*/
        callEditClientGetApi()


        /**Response of EditClient GET Api*/
        mViewModelEditClient.mModelEditClientGetResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    /*Set name*/
                    if(it.data?.client!!.name.isNotEmpty()){
                       tietName.setText(it.data?.client!!.name)
                    }

                    /*Set mobile*/
                    if(it.data?.client!!.mobile.isNotEmpty()){
                        tietMobileNumber.setText(it.data?.client!!.mobile)
                    }

                    /*Set whatsapp*/
                    if(it.data?.client!!.whatsapp.isNotEmpty()){
                        tietWhatsappNumber.setText(it.data?.client!!.whatsapp)
                    }

                    /*Set email*/
                    if(it.data?.client!!.email.isNotEmpty()){
                        tietEmail.setText(it.data?.client!!.email)
                    }

                    /*Set address*/
                    if(it.data?.client!!.address.isNotEmpty()){
                        tietAddress.setText(it.data?.client!!.address)
                    }

                    /*Set dueDateCategories*/
                    if(!it.data?.client!!.due_date_categories.isNullOrEmpty()){
                       dueDateCategories = it.data?.client!!.due_date_categories
                        for(i in 0 until dueDateCategories.size){
                            for(j in 0 until it.data?.due_date_categories!!.size){
                                if(dueDateCategories[i].equals(it.data?.due_date_categories!![j].id_due_date_category)){
                                    dueDateCategoriesNames.add(it.data?.due_date_categories!![j].category_name)
                                }
                            }

                        }
                        Log.e("cats", dueDateCategoriesNames.toString())
                        val categories: String = TextUtils.join(", ", dueDateCategoriesNames)
                        edtNotificationCategories.setText(categories)
                        Log.e("joined", categories)
                    }
                }
                "0"->{
                    snackBar(it.message, this)
                }
                else->{
                    showSnackBar(this,it.message)
                }
            }
        })

        /*Button Submit Click*/
        btnSubmit.setOnClickListener {
            hideKeyboard(tietAddress)
            validateInput()
        }

        /**Response of EditClient POST Api*/
        mViewModelEditClient.mModelEditClientResponse.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    showOkFinishActivityDialog(it.message,this)
                }
                "0"->{
                    snackBar(it.message, this)
                }
                else->{
                    showSnackBar(this,it.message)
                }
            }
        })

        /**Notification Categories Click*/
        edtNotificationCategories.setOnClickListener {
            val intent = Intent(this, NotificationCategoriesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun validateInput(){
        if(tietName.text.toString().isEmpty()){
           snackBar(getString(R.string.name_required),this)
        }else if(tietEmail.text.toString().isEmpty()or!isEmailValid(tietEmail.text.toString())){
            snackBar(getString(R.string.invalid_email_address),this)
        }else if(tietMobileNumber.text.toString().isEmpty()|| tietMobileNumber.text.toString().length!=10|| !tietMobileNumber.text.toString().isDigitsOnly()){
            snackBar(getString(R.string.invalid_number),this)
        }else if(tietWhatsappNumber.text.toString().isEmpty()||tietWhatsappNumber.text.toString().length!=10|| !tietWhatsappNumber.text.toString().isDigitsOnly()){
            snackBar(getString(R.string.invalid_whatsapp_number),this)
        }else if(tietAddress.text.toString().isEmpty()|| tietAddress.text.toString().length<20){
            snackBar(getString(R.string.enter_full_address),this)
        }else{
            callEditClientPostApi(tietName.text.toString(),tietMobileNumber.text.toString(),tietWhatsappNumber.text.toString(),
                tietEmail.text.toString(),tietAddress.text.toString(),dueDateCategories)

        }
    }

    private fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    private fun callEditClientGetApi(){
        ll_loading.visibility = View.VISIBLE
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelEditClient.editClient(idClient.toInt())
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    private fun callEditClientPostApi(name:String,mobileNumber:String,whatsapp:String,email:String,address:String,dueDateCategories:ArrayList<String>){
        ll_loading.visibility = View.VISIBLE
        val modelEditClientRequest= ModelEditClientRequest(idClient,name,mobileNumber,whatsapp,email,address,dueDateCategories)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelEditClient.editClient(idClient.toInt(),modelEditClientRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        callEditClientPostApi(tietName.text.toString(),tietMobileNumber.text.toString(),tietWhatsappNumber.text.toString(),
            tietEmail.text.toString(),tietAddress.text.toString(),dueDateCategories)
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }
}