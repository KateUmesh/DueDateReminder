package com.duedatereminder.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.duedatereminder.R
import com.duedatereminder.utils.ContextExtension.Companion.callOtpVerificationActivity
import com.duedatereminder.utils.ContextExtension.Companion.showOkDialog
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*Toolbar*/
        toolbar(getString(R.string.login),false)

        /*Initialize variables*/
        val tietMobileNumber :TextInputEditText = findViewById(R.id.tietMobileNumber)
        val btnLogin :Button = findViewById(R.id.btnLogin)
        val btnCreateAccount :Button = findViewById(R.id.btnCreateAccount)


        /*Button Login Click*/
        btnLogin.setOnClickListener {
            if(tietMobileNumber.text.toString().length==10) {
                callOtpVerificationActivity(this,tietMobileNumber.text.toString())
            }else{
                showOkDialog(getString(R.string.invalid_number),this)
            }
        }

        /*Button Create Account click*/
        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}