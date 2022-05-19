package com.duedatereminder.view.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.ModelCreateAccountRequest
import com.duedatereminder.model.ModelSendRegistrationOtpRequest
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.LocalSharedPreference
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelCreateAccount
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import java.util.concurrent.TimeUnit

class RegistrationOTPVerificationActivity : AppCompatActivity(), SnackBarCallback {
    var otp=""
    var token=""
    var mobileNumber=""
    var name=""
    var email=""
    var whatsapp=""
    var address=""
    private var androidId:String=""
    lateinit var tvTimer : TextView
    private lateinit var mViewModelCreateAccount: ViewModelCreateAccount
    @SuppressLint("SetTextI18n", "HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_otpverification)

        /*Toolbar*/
        toolbar(getString(R.string.phone_verification),true)

        /**Initialize View Model*/
        mViewModelCreateAccount = ViewModelProvider(this).get(ViewModelCreateAccount::class.java)

        /**Initialize Variable*/
        val tvOtpMessage : TextView = findViewById(R.id.tvOtpMessage)
        val edtOtp : TextInputEditText = findViewById(R.id.edtOtp)
        val btnVerifyAndProceed : Button = findViewById(R.id.btnVerifyAndProceed)
        tvTimer  = findViewById(R.id.tvTimer)

        /**Start Timer*/
        countDownTimer(tvTimer)

        /**Android Id*/
        androidId =  Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        /**Get name from CreateAccountActivity*/
        if(!intent.getStringExtra(Constant.NAME).isNullOrEmpty()){
            name=intent.getStringExtra(Constant.NAME)!!
        }

        /**Get email from CreateAccountActivity*/
        if(!intent.getStringExtra(Constant.EMAIL).isNullOrEmpty()){
            email=intent.getStringExtra(Constant.EMAIL)!!
        }

        /**Get mobile number from CreateAccountActivity*/
        if(!intent.getStringExtra(Constant.MOBILE_NUMBER).isNullOrEmpty()){
            mobileNumber=intent.getStringExtra(Constant.MOBILE_NUMBER)!!
            tvOtpMessage.text = getString(R.string.sentOTPto)+" "+intent.getStringExtra(Constant.MOBILE_NUMBER)
        }

        /**Get whatsapp from CreateAccountActivity*/
        if(!intent.getStringExtra(Constant.WHATSAPP).isNullOrEmpty()){
            whatsapp=intent.getStringExtra(Constant.WHATSAPP)!!
        }

        /**Get address from CreateAccountActivity*/
        if(!intent.getStringExtra(Constant.ADDRESS).isNullOrEmpty()){
            address=intent.getStringExtra(Constant.ADDRESS)!!
        }

        /**Get otp from CreateAccountActivity*/
        if(!intent.getStringExtra(Constant.OTP).isNullOrEmpty()){
            otp=intent.getStringExtra(Constant.OTP)!!
        }

        /**Verify and Proceed Button Click*/
        btnVerifyAndProceed.setOnClickListener {
            if(edtOtp.text.toString() == otp){
                callCreateAccountApi(name, mobileNumber, whatsapp, email, address, androidId)
            }else{
                if(!this.isFinishing){
                    try{
                        ContextExtension.showOkDialog(getString(R.string.invalid_code), this)
                    }catch(e: WindowManager.BadTokenException){
                        e.printStackTrace()
                    }
                }

            }
        }

        /**Response of SendRegistrationOtp Api*/
        mViewModelCreateAccount.mSendRegistrationOtpLiveData.observe(this, androidx.lifecycle.Observer {
            when(it.status){
                "1"->{
                    otp=it.data!!.otp
                }
                "0"->{
                    ContextExtension.snackBar(it.message, this)
                }
                else->{
                    showSnackBar(this,it.message)
                }
            }
        })

        /**Response of CreateAccount Api*/
        mViewModelCreateAccount.mCreateAccountLiveData.observe(this, androidx.lifecycle.Observer {
            when(it.status){
                "1"->{
                    LocalSharedPreference.putStringValue(Constant.token,token)
                    ContextExtension.callHomeActivity(this)
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

    private fun countDownTimer(tvTime:TextView) {
        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTime.text = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    java.lang.Long.valueOf(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                    ), java.lang.Long.valueOf(
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                    )
                )
            }

            override fun onFinish() {
                tvTime.text = getString(R.string.resend_code)
                tvTime.setOnClickListener {
                    callSendRegistrationOtpApi(mobileNumber)
                }
            }
        }.start()
    }


    private fun callSendRegistrationOtpApi(mobileNumber:String){
        val mModelSendRegistrationOtpRequest = ModelSendRegistrationOtpRequest(mobileNumber,otp)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelCreateAccount.sendRegistrationOtp(mModelSendRegistrationOtpRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    private fun callCreateAccountApi(name:String,mobileNumber:String,whatsapp:String,email:String,address:String,androidId:String){
        val modelCreateAccountRequest= ModelCreateAccountRequest(name,mobileNumber,whatsapp,email,address,androidId)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelCreateAccount.createAccount(modelCreateAccountRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        callSendRegistrationOtpApi(mobileNumber)
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }
}