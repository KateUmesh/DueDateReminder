package com.duedatereminder.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.ModelSendLoginOtpRequest
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.callHomeActivity
import com.duedatereminder.utils.ContextExtension.Companion.hideKeyboard
import com.duedatereminder.utils.ContextExtension.Companion.showOkDialog
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.LocalSharedPreference
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelLogin
import com.duedatereminder.viewModel.activityViewModel.ViewModelOtpVerification
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import java.util.concurrent.TimeUnit

class OtpVerificationActivity : AppCompatActivity(),SnackBarCallback {
    var otp=""
    var etOtp=""
    var token=""
    var mobileNumber=""
    lateinit var tvTimer : TextView
    lateinit var btnResendCode : Button
    private lateinit var mViewModelOtpVerification: ViewModelOtpVerification
    private lateinit var ll_loading : LinearLayoutCompat
    var otpEt = arrayOfNulls<TextInputEditText>(4)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_header)

        /*Toolbar*/
        toolbar("",true)

        /**Initialize View Model*/
        mViewModelOtpVerification = ViewModelProvider(this).get(ViewModelOtpVerification::class.java)

        /**Initialize Variable*/
        //val tvOtpMessage : TextView = findViewById(R.id.tvOtpMessage)
        val etMobileNumber : TextInputEditText = findViewById(R.id.etMobileNumber)
        //val edtOtp : TextInputEditText = findViewById(R.id.edtOtp)
        otpEt[0] = findViewById<View>(R.id.etOtp1) as TextInputEditText
        otpEt[1] = findViewById<View>(R.id.etOtp2) as TextInputEditText
        otpEt[2] = findViewById<View>(R.id.etOtp3) as TextInputEditText
        otpEt[3] = findViewById<View>(R.id.etOtp4) as TextInputEditText
        val btnVerifyAndProceed : Button = findViewById(R.id.btnVerifyAndProceed)
         tvTimer  = findViewById(R.id.tvTimer)
        btnResendCode  = findViewById(R.id.btnResendCode)
        ll_loading = findViewById(R.id.ll_loading)

        /**Start Timer*/
        countDownTimer(tvTimer)

        /**Get mobile number from LoginActivity*/
        if(!intent.getStringExtra(Constant.MOBILE_NUMBER).isNullOrEmpty()){
            mobileNumber=intent.getStringExtra(Constant.MOBILE_NUMBER)!!
            //tvOtpMessage.text = getString(R.string.sentOTPto)+" "+intent.getStringExtra(Constant.MOBILE_NUMBER)
            etMobileNumber.setText(intent.getStringExtra(Constant.MOBILE_NUMBER))
        }

        /**Get otp from LoginActivity*/
        if(!intent.getStringExtra(Constant.OTP).isNullOrEmpty()){
            otp=intent.getStringExtra(Constant.OTP)!!
        }

        /**Get token from LoginActivity*/
        if(!intent.getStringExtra(Constant.token).isNullOrEmpty()){
            token=intent.getStringExtra(Constant.token)!!
        }

        /**Verify and Proceed Button Click*/
        btnVerifyAndProceed.setOnClickListener {
            otpEt[0]?.let { it1 -> hideKeyboard(it1) }

            etOtp = otpEt[0]!!.text.toString()+otpEt[1]!!.text.toString()+otpEt[2]!!.text.toString()+otpEt[3]!!.text.toString()

            if(etOtp.length==4 && etOtp == otp) {
                LocalSharedPreference.putStringValue(Constant.token,token)
                callHomeActivity(this)
            }else{
                if(!this.isFinishing){
                    try{
                        showOkDialog(getString(R.string.invalid_code),this)
                    }catch(e: WindowManager.BadTokenException){
                        e.printStackTrace()
                    }
                }
            }
            /*if(edtOtp.text.toString() == otp){
                LocalSharedPreference.putStringValue(Constant.token,token)
                callHomeActivity(this)
            }else{
                if(!this.isFinishing){
                    try{
                        showOkDialog(getString(R.string.invalid_code),this)
                    }catch(e: WindowManager.BadTokenException){
                        e.printStackTrace()
                    }
                }

            }*/
        }

        /**Response of ResendLoginOtp*/
        mViewModelOtpVerification.mResendLoginOtpLiveData.observe(this, androidx.lifecycle.Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    otp=it.data!!.otp
                    token=it.data!!.token
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
        tvTime.visibility = View.VISIBLE
        btnResendCode.visibility = View.GONE
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
                /*tvTime.text = getString(R.string.resend_code)
                tvTime.setOnClickListener {
                    callResendLoginOtp(mobileNumber)
                }*/
                tvTime.visibility = View.GONE
                btnResendCode.visibility = View.VISIBLE
                btnResendCode.setOnClickListener {
                    callResendLoginOtp(mobileNumber)
                }

            }
        }.start()
    }

    private fun callResendLoginOtp(mobileNumber:String){
        ll_loading.visibility = View.VISIBLE
        val mModelSendLoginOtpRequest = ModelSendLoginOtpRequest(mobileNumber,otp)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelOtpVerification.resendLoginOtp(mModelSendLoginOtpRequest)
            countDownTimer(tvTimer)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        callResendLoginOtp(mobileNumber)
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }
}