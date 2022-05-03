package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.duedatereminder.R
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.google.android.material.textfield.TextInputEditText

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        /*Toolbar*/
        toolbar(getString(R.string.createAccount),true)

        /*Initialize variables*/
        val tietName : TextInputEditText = findViewById(R.id.tietName)
        val tietEmail : TextInputEditText = findViewById(R.id.tietEmail)
        val tietMobileNumber : TextInputEditText = findViewById(R.id.tietMobileNumber)
        val tietWhatsappNumber : TextInputEditText = findViewById(R.id.tietWhatsappNumber)
        val tietAddress : TextInputEditText = findViewById(R.id.tietAddress)
        val btnCreateAccount : Button = findViewById(R.id.btnCreateAccount)
    }
}