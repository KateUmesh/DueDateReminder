package com.duedatereminder.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.duedatereminder.R
import com.duedatereminder.view.activities.HomeActivity
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

        fun callHomeActivity(activity: Activity){
            val intent = Intent(activity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
            activity.finish()
        }

        fun callOtpVerificationActivity(activity: Activity,mobileNumber:String){
            val intent = Intent(activity, OtpVerificationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Constant.MOBILE_NUMBER,mobileNumber)
            activity.startActivity(intent)

        }

        fun snackBar(message: CharSequence,activity: Activity){
            Snackbar.make(activity.findViewById(android.R.id.content),message,Snackbar.LENGTH_SHORT).show()
        }

    }
}