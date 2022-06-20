package com.duedatereminder.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.firebase.MyFirebaseMessagingService
import com.duedatereminder.model.ModelSplashRequest
import com.duedatereminder.utils.AppSignatureHelper
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension.Companion.callHomeActivity
import com.duedatereminder.utils.ContextExtension.Companion.callLoginActivity
import com.duedatereminder.utils.ContextExtension.Companion.showSnackBar
import com.duedatereminder.utils.ContextExtension.Companion.snackBar
import com.duedatereminder.utils.LocalSharedPreference
import com.duedatereminder.utils.NetworkConnection
import com.duedatereminder.viewModel.activityViewModel.ViewModelSplashScreen

class SplashActivity : AppCompatActivity(),SnackBarCallback {
    private lateinit var mModelSplashRequest: ModelSplashRequest
    private lateinit var mViewModelSplashScreen: ViewModelSplashScreen

     var token:String=""
     var firebaseToken:String=""
     var androidId:String=""

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /**Initialize View Model*/
        mViewModelSplashScreen = ViewModelProvider(this).get(ViewModelSplashScreen::class.java)


        /*Token*/
        if(!LocalSharedPreference.getStringValue(Constant.token).isNullOrEmpty()){
            token=LocalSharedPreference.getStringValue(Constant.token)!!
        }

        if(!MyFirebaseMessagingService().getToken().isNullOrEmpty()){
            firebaseToken=MyFirebaseMessagingService().getToken()
        }

        androidId =  Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        mModelSplashRequest = ModelSplashRequest(token,androidId,firebaseToken)

        AppSignatureHelper(this).appSignatures

        /**Call UserAppStatus Api*/
        callUserAppStatus(mModelSplashRequest)

        /**Response of UserAppStatus Api*/
        mViewModelSplashScreen.mSplashScreenLiveData.observe(this, Observer {
            when(it.status){
                "1"->{
                    if(!it.data?.name.isNullOrEmpty()){
                        LocalSharedPreference.putStringValue(Constant.USER_NAME,it.data?.name)
                    }

                    if(!it.data?.email.isNullOrEmpty()){
                        LocalSharedPreference.putStringValue(Constant.USER_EMAIL,it.data?.email)
                    }

                    if(LocalSharedPreference.getStringValue(Constant.token).isNullOrEmpty()){
                        callLoginActivity(this)
                    }else{
                        callHomeActivity(this)
                    }
                }
                "0"->{
                    snackBar(it.message,this)
                }
                else->{
                    showSnackBar(this,it.message)
                }
            }
        })

        /*Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }, 3000)*/
    }


    private fun callUserAppStatus(mModelSplashRequest: ModelSplashRequest){
        if(NetworkConnection.isNetworkConnected()) {
            mViewModelSplashScreen.userAppStatus(mModelSplashRequest)
        }else{
            showSnackBar(this,getString(R.string.no_internet_connection))
        }
    }

    override fun snackBarSuccessInternetConnection() {
        callUserAppStatus(mModelSplashRequest)
    }

    override fun snackBarFailedInterConnection() {
        showSnackBar(this,getString(R.string.no_internet_connection))
    }
}

