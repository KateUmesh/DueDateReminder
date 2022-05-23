package com.duedatereminder.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.duedatereminder.R
import com.duedatereminder.callback.SnackBarCallback
import com.duedatereminder.view.activities.HomeActivity
import com.duedatereminder.view.activities.LoginActivity
import com.duedatereminder.view.activities.NavigationDrawerActivity
import com.duedatereminder.view.activities.OtpVerificationActivity
import com.google.android.material.snackbar.Snackbar

class ContextExtension {

    companion object{

        fun AppCompatActivity.toolbar(title: String,backArrow:Boolean){

            val toolbar = this.findViewById<View>(R.id.toolbar) as Toolbar
            this.setSupportActionBar(toolbar)
            this.supportActionBar!!.title = title
            this.supportActionBar!!.setDisplayHomeAsUpEnabled(backArrow)
            Tools.setSystemBarColor(this)
            toolbar.setNavigationOnClickListener { this.onBackPressed() }

        }

        fun showOkDialog(message: String,context: Context) {
            val builder =
                AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.app_name) as CharSequence)
            builder.setMessage(message)
            builder.setPositiveButton(
                R.string.Ok
            ) { _, _ -> }
            builder.show()
        }

        fun showOkFinishActivityDialog(message: String,activity: Activity) {
            val builder =
                AlertDialog.Builder(activity)
            builder.setTitle(activity.getString(R.string.app_name) as CharSequence)
            builder.setMessage(message)
            builder.setPositiveButton(
                R.string.Ok
            ) { _, _ ->
                activity.finish()
            }
            builder.show()
        }

        fun callLoginActivity(activity: Activity){
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            activity.startActivity(intent)
            activity.finish()
        }

        fun callHomeActivity(activity: Activity){
            val intent = Intent(activity, NavigationDrawerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
            activity.finish()
        }

        fun callOtpVerificationActivity(activity: Activity,mobileNumber:String,otp:String,token:String){
            val intent = Intent(activity, OtpVerificationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.MOBILE_NUMBER,mobileNumber)
            intent.putExtra(Constant.OTP,otp)
            intent.putExtra(Constant.token,token)
            activity.startActivity(intent)

        }

        fun snackBar(message: CharSequence,activity: Activity){
            Snackbar.make(activity.findViewById(android.R.id.content),message,Snackbar.LENGTH_SHORT).show()
        }

        fun hideKeyboard(view: View) {
            view.apply {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun Activity.showSnackBar(snackBarCallback: SnackBarCallback, message: CharSequence){
            val snackBar = Snackbar.make(this.findViewById(android.R.id.content),message,Snackbar.LENGTH_INDEFINITE)

            snackBar.setActionTextColor(ContextCompat.getColor(this,R.color.Yellow))
            snackBar.setAction(this.getString(R.string.retry)){
                if(NetworkConnection.isNetworkConnected()){
                    snackBarCallback.snackBarSuccessInternetConnection()
                }else{
                    this.showSnackBar(snackBarCallback,this.getString(R.string.no_internet_connection))
                }
                snackBar.dismiss()
            }
            snackBar.show()
        }

    }
}