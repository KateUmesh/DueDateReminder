package com.duedatereminder.view.activities

import android.annotation.SuppressLint
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.broadcast.OtpReceivedInterface
import com.duedatereminder.broadcast.SMSBroadcastReceiver
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
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import java.util.concurrent.TimeUnit

class RegistrationOTPVerificationActivity : AppCompatActivity(), SnackBarCallback,
    OtpReceivedInterface {
    var otp=""
    var etOtp=""
    var token=""
    var mobileNumber=""
    var name=""
    var email=""
    var address=""
    var whatsapp=""
    var firmName=""
    var accountType=""
    private var androidId:String=""
    lateinit var tvTimer : TextView
    lateinit var btnResendCode : Button
    private lateinit var mViewModelCreateAccount: ViewModelCreateAccount
    private lateinit var ll_loading : LinearLayoutCompat
    var otpEt = arrayOfNulls<TextInputEditText>(4)
    lateinit var edtOtp : TextInputEditText
    private var smsBroadcastReceiver: SMSBroadcastReceiver? = null
    @SuppressLint("SetTextI18n", "HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_header)

        /*Toolbar*/
        toolbar("",true)

        /**Initialize View Model*/
        mViewModelCreateAccount = ViewModelProvider(this).get(ViewModelCreateAccount::class.java)

        /**Initialize Variable*/
        val tvOtpMessage : TextView = findViewById(R.id.tvOtpMessage)
        //val edtOtp : TextInputEditText = findViewById(R.id.edtOtp)
        edtOtp  = findViewById(R.id.edtOtp)
        val etMobileNumber : TextInputEditText = findViewById(R.id.etMobileNumber)
        otpEt[0] = findViewById<View>(R.id.etOtp1) as TextInputEditText
        otpEt[1] = findViewById<View>(R.id.etOtp2) as TextInputEditText
        otpEt[2] = findViewById<View>(R.id.etOtp3) as TextInputEditText
        otpEt[3] = findViewById<View>(R.id.etOtp4) as TextInputEditText
        val btnVerifyAndProceed : Button = findViewById(R.id.btnVerifyAndProceed)
        ll_loading = findViewById(R.id.ll_loading)
        tvTimer  = findViewById(R.id.tvTimer)
        btnResendCode  = findViewById(R.id.btnResendCode)

        /**Start Timer*/
        countDownTimer(tvTimer)

        /**Sms listener*/
        smsListener()

        /**Text watcher*/
        editTextOperation()

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
            //tvOtpMessage.text = getString(R.string.sentOTPto)+" "+intent.getStringExtra(Constant.MOBILE_NUMBER)
            tvOtpMessage.text = "You will get SMS with a confirmation code to this "+intent.getStringExtra(Constant.MOBILE_NUMBER)+" number."
            etMobileNumber.setText(intent.getStringExtra(Constant.MOBILE_NUMBER))
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

        /**Get firmName from CreateAccountActivity*/
        if(!intent.getStringExtra(Constant.FIRM_NAME).isNullOrEmpty()){
            firmName=intent.getStringExtra(Constant.FIRM_NAME)!!
        }

        /**Get accountType from CreateAccountActivity*/
        if(!intent.getStringExtra(Constant.ACCOUNT_TYPE).isNullOrEmpty()){
            accountType=intent.getStringExtra(Constant.ACCOUNT_TYPE)!!
        }

        /**Verify and Proceed Button Click*/
        btnVerifyAndProceed.setOnClickListener {
            /*otpEt[0]?.let { it1 -> ContextExtension.hideKeyboard(it1) }

            etOtp = otpEt[0]!!.text.toString()+otpEt[1]!!.text.toString()+otpEt[2]!!.text.toString()+otpEt[3]!!.text.toString()

            if(etOtp.length==4 && etOtp == otp) {
                callCreateAccountApi(name, mobileNumber, whatsapp, email, address, androidId)
            }else{
                if(!this.isFinishing){
                    try{
                        ContextExtension.showOkDialog(getString(R.string.invalid_code), this)
                    }catch(e: WindowManager.BadTokenException){
                        e.printStackTrace()
                    }
                }
            }*/

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
            ll_loading.visibility = View.GONE
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
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    LocalSharedPreference.putStringValue(Constant.token,it.data!!.token)
                    LocalSharedPreference.putStringValue(Constant.USER_NAME,name)
                    LocalSharedPreference.putStringValue(Constant.USER_EMAIL,email)
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
               /* tvTime.text = getString(R.string.resend_code)
                tvTime.setOnClickListener {
                    callSendRegistrationOtpApi(mobileNumber)
                }*/

                tvTime.visibility = View.GONE
                btnResendCode.visibility = View.VISIBLE
                btnResendCode.setOnClickListener {
                    callSendRegistrationOtpApi(mobileNumber)
                }
            }
        }.start()
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

    private fun callCreateAccountApi(name:String,mobileNumber:String,whatsapp:String,email:String,address:String,androidId:String){
        ll_loading.visibility = View.VISIBLE
        val modelCreateAccountRequest= ModelCreateAccountRequest(name,mobileNumber,whatsapp,email,address,firmName,accountType,androidId)
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

    private fun editTextOperation() {
        edtOtp.requestFocus()

        edtOtp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                Attempt: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                Attempt: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {

                if(edtOtp.text.toString().length == 4){
                    checkingOtp()
                }
            }
        })

    }

    fun checkingOtp() {
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

    private fun smsListener() {
        smsBroadcastReceiver = SMSBroadcastReceiver()
        smsBroadcastReceiver!!.setOnOtpListener(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        applicationContext.registerReceiver(smsBroadcastReceiver, intentFilter)
        val client = SmsRetriever.getClient(this)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener {}
        task.addOnFailureListener {}
    }

    override fun onOtpReceived(otp: String?) {
        val s = otp!!.replace("\\D+".toRegex(), "")
        edtOtp.setText(s)
        edtOtp.setSelection(4)
    }

    override fun onOtpTimeout() {
    }
}