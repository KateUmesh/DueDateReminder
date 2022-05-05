package com.duedatereminder.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.duedatereminder.R
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.ContextExtension.Companion.callHomeActivity
import com.duedatereminder.utils.ContextExtension.Companion.showOkDialog
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import java.util.concurrent.TimeUnit

class OtpVerificationActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        /*Toolbar*/
        toolbar(getString(R.string.phone_verification),true)

        /*Initialize Variable*/
        val tvOtpMessage : TextView = findViewById(R.id.tvOtpMessage)
        val edtOtp : TextInputEditText = findViewById(R.id.edtOtp)
        val btnVerifyAndProceed : Button = findViewById(R.id.btnVerifyAndProceed)
        val tvTimer : TextView = findViewById(R.id.tvTimer)

        /*Start Timer*/
        countDownTimer(tvTimer)

        /*Get mobile number from LoginActivity*/
        if(!intent.getStringExtra(Constant.MOBILE_NUMBER).isNullOrEmpty()){
            tvOtpMessage.text = getString(R.string.sentOTPto)+intent.getStringExtra(Constant.MOBILE_NUMBER)
        }

        /*Verify and Proceed Button Click*/
        btnVerifyAndProceed.setOnClickListener {
            if(edtOtp.text.toString() == "1234"){
                callHomeActivity(this)
            }else{
                showOkDialog(getString(R.string.invalid_code),this)
            }
        }
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
                    countDownTimer(tvTime)
                }
            }
        }.start()
    }
}