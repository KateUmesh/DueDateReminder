package com.duedatereminder.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.ModelAddClientRequest
import com.duedatereminder.model.ModelCreateAccountRequest
import com.duedatereminder.model.ModelSendLoginOtpRequest
import com.duedatereminder.model.ModelSendRegistrationOtpRequest
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.callOtpVerificationActivity
import com.duedatereminder.utils.ContextExtension.Companion.hideKeyboard
import com.duedatereminder.utils.ContextExtension.Companion.showOkDialog
import com.duedatereminder.utils.ContextExtension.Companion.showOkFinishActivityDialog
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.snackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelAddClient
import com.duedatereminder.viewModel.activityViewModel.ViewModelCreateAccount
import com.duedatereminder.viewModel.activityViewModel.ViewModelLogin
import com.google.android.material.textfield.TextInputEditText

class AddClientActivity : AppCompatActivity(), SnackBarCallback {
    private lateinit var tietName : TextInputEditText
    private lateinit var tietEmail : TextInputEditText
    private lateinit var tietMobileNumber : TextInputEditText
    private lateinit var tietWhatsappNumber : TextInputEditText
    private lateinit var tietAddress : TextInputEditText
    private lateinit var edtNotificationCategories : TextInputEditText
    private lateinit var ll_loading : LinearLayoutCompat
    var otp:String = ""
    private lateinit var mViewModelAddClient: ViewModelAddClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)

        /*Toolbar*/
        toolbar(getString(R.string.add_client),true)

        /*Initialize variables*/
        tietName  = findViewById(R.id.tietName)
        tietEmail = findViewById(R.id.tietEmail)
        tietMobileNumber = findViewById(R.id.tietMobileNumber)
        tietWhatsappNumber = findViewById(R.id.tietWhatsappNumber)
        tietAddress = findViewById(R.id.tietAddress)
        edtNotificationCategories = findViewById(R.id.edtNotificationCategories)
        ll_loading = findViewById(R.id.ll_loading)
        val btnSubmit : Button = findViewById(R.id.btnSubmit)

        /**Initialize View Model*/
        mViewModelAddClient = ViewModelProvider(this).get(ViewModelAddClient::class.java)



        /*Button Submit Click*/
        btnSubmit.setOnClickListener {
            hideKeyboard(tietAddress)
            validateInput()
        }

        /**Response of SendRegistrationOtp Api*/
        mViewModelAddClient.mModelAddClientResponse.observe(this, Observer {
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
            callAddClientApi(tietName.text.toString(),tietMobileNumber.text.toString(),tietWhatsappNumber.text.toString(),
                tietEmail.text.toString(),tietAddress.text.toString())

        }
    }

    private fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    private fun callAddClientApi(name:String,mobileNumber:String,whatsapp:String,email:String,address:String){
        ll_loading.visibility = View.VISIBLE
        val modelAddClientRequest= ModelAddClientRequest(name,mobileNumber,whatsapp,email,address)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelAddClient.addClient(modelAddClientRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        callAddClientApi(tietName.text.toString(),tietMobileNumber.text.toString(),tietWhatsappNumber.text.toString(),
            tietEmail.text.toString(),tietAddress.text.toString())
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }
}