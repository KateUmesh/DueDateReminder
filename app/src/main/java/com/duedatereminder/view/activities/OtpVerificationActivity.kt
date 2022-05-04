package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.duedatereminder.R
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.google.android.material.textfield.TextInputEditText

class OtpVerificationActivity : AppCompatActivity() {
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

        /*Verify and Proceed Button Click*/
        btnVerifyAndProceed.setOnClickListener {

        }
    }
}