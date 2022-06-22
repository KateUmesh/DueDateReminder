package com.duedatereminder.view.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.model.ModelSendLoginOtpRequest
import com.duedatereminder.model.ModelSendRegistrationOtpRequest
import com.duedatereminder.model.ModelSplashRequest
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.callOtpVerificationActivity
import com.duedatereminder.utils.ContextExtension.Companion.getPhone
import com.duedatereminder.utils.ContextExtension.Companion.hideKeyboard
import com.duedatereminder.utils.ContextExtension.Companion.showKeyBoard
import com.duedatereminder.utils.ContextExtension.Companion.showOkDialog
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.snackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.LocalSharedPreference
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelLogin
import com.duedatereminder.viewModel.activityViewModel.ViewModelSplashScreen
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity(),SnackBarCallback ,GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener {
    private lateinit var mViewModelLogin: ViewModelLogin
    var otp:String = ""
    lateinit var tietMobileNumber :TextInputEditText
    private lateinit var ll_loading : LinearLayoutCompat
    private var nonMob = 0
    private val RC_HINT = 1000
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_card_overlap)

        /*Toolbar*/
       // toolbar(getString(R.string.login),false)

        /*Initialize variables*/
         tietMobileNumber  = findViewById(R.id.tietMobileNumber)
        val btnLogin :Button = findViewById(R.id.btnLogin)
        //val btnCreateAccount :Button = findViewById(R.id.btnCreateAccount)
        val tvSignUp :TextView = findViewById(R.id.tvSignUp)
        ll_loading = findViewById(R.id.ll_loading)

        /**Initialize View Model*/
        mViewModelLogin = ViewModelProvider(this).get(ViewModelLogin::class.java)


        /*Mobile Number*/
        tietMobileNumber.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                showKeyBoard(tietMobileNumber)
                tietMobileNumber.isEnabled = true
                /*if(Build.VERSION.SDK_INT>Build.VERSION_CODES.R) {
                    showKeyBoard(tietMobileNumber)
                    tietMobileNumber.isEnabled = true
                }else {
                    if (nonMob != 0) {
                    tietMobileNumber.isEnabled = true
                    if (tietMobileNumber.text!!.length >= 10) tietMobileNumber.setSelection(10)
                        showKeyBoard(tietMobileNumber)
                    } else {
                        //getPhone()
                        val pendingIntent = getPhone(this, this)
                        startIntentResult(pendingIntent)
                    }
                }*/
            }
            false
        }


        /*Button Login Click*/
        btnLogin.setOnClickListener {
            hideKeyboard(tietMobileNumber)
            if(tietMobileNumber.text.toString().length==10) {
                /**Call SendLoginOtp  Api*/
                callSendLoginOtpApi(tietMobileNumber.text.toString())
                //callOtpVerificationActivity(this,tietMobileNumber.text.toString())
            }else{
                showOkDialog(getString(R.string.invalid_number),this)
            }
        }

        /**Response of SendLoginOtp Api*/
        mViewModelLogin.mSendLoginOtpLiveData.observe(this, Observer {
            ll_loading.visibility = View.GONE
            when(it.status){
                "1"->{
                    callOtpVerificationActivity(this,tietMobileNumber.text.toString(),it.data?.otp!!,it.data?.token!!,it.data?.name!!,it.data?.email!!)
                }
                "0"->{
                    snackBar(it.message, this)
                }
                else->{
                    showSnackBar(this,it.message)
                }
            }
        })

        /*Button Create Account click*/
        tvSignUp.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun callSendLoginOtpApi(mobileNumber:String){
        ll_loading.visibility = View.VISIBLE
        val mModelSendLoginOtpRequest = ModelSendLoginOtpRequest(mobileNumber,otp)
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelLogin.sendLoginOtp(mModelSendLoginOtpRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        callSendLoginOtpApi(tietMobileNumber.text.toString())
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private fun startIntentResult(pendingIntent: PendingIntent?) {
        try {
            startIntentSenderForResult(
                pendingIntent?.intentSender,
                RC_HINT, null, 0, 0, 0
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        nonMob = requestCode
        if (requestCode == RC_HINT) {
            showKeyBoard(tietMobileNumber)
            if (resultCode == Activity.RESULT_OK) {
                val credential: Credential =
                    data!!.getParcelableExtra(Credential.EXTRA_KEY)!!
                if (credential != null) {
                    var mobile = credential.id
                    val newString: String = mobile.replace("+91", "")
                    tietMobileNumber.setText(newString)
                    tietMobileNumber.setSelection(tietMobileNumber.text!!.length)

                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    showKeyBoard(tietMobileNumber)
                }
            }
        }
    }
}