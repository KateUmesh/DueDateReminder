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
import com.duedatereminder.model.ModelSendLoginOtpRequest
import com.duedatereminder.model.ModelSendRegistrationOtpRequest
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.callOtpVerificationActivity
import com.duedatereminder.utils.ContextExtension.Companion.hideKeyboard
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.snackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelCreateAccount
import com.duedatereminder.viewModel.activityViewModel.ViewModelLogin
import com.google.android.material.textfield.TextInputEditText

class CreateAccountActivity : AppCompatActivity(), SnackBarCallback {
    private lateinit var tietName : TextInputEditText
    private lateinit var tietEmail : TextInputEditText
    private lateinit var tietMobileNumber : TextInputEditText
    private lateinit var tietWhatsappNumber : TextInputEditText
    private lateinit var tietAddress : TextInputEditText
    private lateinit var ll_loading : LinearLayoutCompat
    var otp:String = ""
    private lateinit var mViewModelCreateAccount: ViewModelCreateAccount
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        /*Toolbar*/
        toolbar(getString(R.string.createAccount),true)

        /*Initialize variables*/
        tietName  = findViewById(R.id.tietName)
        tietEmail = findViewById(R.id.tietEmail)
        tietMobileNumber = findViewById(R.id.tietMobileNumber)
        tietWhatsappNumber = findViewById(R.id.tietWhatsappNumber)
        tietAddress = findViewById(R.id.tietAddress)
        ll_loading = findViewById(R.id.ll_loading)
        val btnCreateAccount : Button = findViewById(R.id.btnCreateAccount)
        val tvTermsAndConditions: AppCompatTextView = findViewById(R.id.tvTermsAndConditions)

        /**Initialize View Model*/
        mViewModelCreateAccount = ViewModelProvider(this).get(ViewModelCreateAccount::class.java)


        /*Terms And Conditions Click*/
        tvTermsAndConditions.setOnClickListener {  val intent = Intent(this,TermsAndConditionActivity::class.java)
            startActivity(intent) }

        /*Button Create Account Click*/
        btnCreateAccount.setOnClickListener {
            hideKeyboard(tietAddress)
            validateInput()
        }

        /**Response of SendRegistrationOtp Api*/
        mViewModelCreateAccount.mSendRegistrationOtpLiveData.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    val intent = Intent(this, RegistrationOTPVerificationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.putExtra(Constant.NAME,tietName.text.toString())
                    intent.putExtra(Constant.EMAIL,tietEmail.text.toString())
                    intent.putExtra(Constant.MOBILE_NUMBER,tietMobileNumber.text.toString())
                    intent.putExtra(Constant.WHATSAPP,tietWhatsappNumber.text.toString())
                    intent.putExtra(Constant.ADDRESS,tietAddress.text.toString())
                    intent.putExtra(Constant.OTP,it.data?.otp!!)
                    startActivity(intent)
                }
                "0"->{
                    snackBar(it.message, this)
                }
                else->{
                    showSnackBar(this,it.message)
                }
            }
        })
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
            //callOtpVerificationActivity(this,tietMobileNumber.text.toString(),otp,"")

            callSendRegistrationOtpApi(tietMobileNumber.text.toString())

        }
    }

    private fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    private fun callSendRegistrationOtpApi(mobileNumber:String){
        ll_loading.visibility = View.VISIBLE
        val mModelSendRegistrationOtpRequest = ModelSendRegistrationOtpRequest(mobileNumber,otp)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelCreateAccount.sendRegistrationOtp(mModelSendRegistrationOtpRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        callSendRegistrationOtpApi(tietMobileNumber.text.toString())
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }
}