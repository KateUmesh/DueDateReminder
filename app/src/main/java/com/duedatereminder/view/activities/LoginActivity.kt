package com.duedatereminder.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
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
import com.duedatereminder.utils.ContextExtension.Companion.hideKeyboard
import com.duedatereminder.utils.ContextExtension.Companion.showOkDialog
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.snackBar
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.duedatereminder.utils.LocalSharedPreference
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelLogin
import com.duedatereminder.viewModel.activityViewModel.ViewModelSplashScreen
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity(),SnackBarCallback {
    private lateinit var mViewModelLogin: ViewModelLogin
    var otp:String = ""
    lateinit var tietMobileNumber :TextInputEditText
    private lateinit var ll_loading : LinearLayoutCompat
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
                    callOtpVerificationActivity(this,tietMobileNumber.text.toString(),it.data?.otp!!,it.data?.token!!)
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
}