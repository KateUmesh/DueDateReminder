package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.duedatereminder.R
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
    }
}