package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.duedatereminder.R
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.google.android.material.textfield.TextInputEditText

class EditAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)

        /*Toolbar*/
        toolbar(getString(R.string.editAccount),true)

        /*Initialize variables*/
        val tietName : TextInputEditText = findViewById(R.id.tietName)
        val tietEmail : TextInputEditText = findViewById(R.id.tietEmail)
        val tietMobileNumber : TextInputEditText = findViewById(R.id.tietMobileNumber)
        val tietWhatsappNumber : TextInputEditText = findViewById(R.id.tietWhatsappNumber)
        val tietAddress : TextInputEditText = findViewById(R.id.tietAddress)
        val btnSaveChanges : Button = findViewById(R.id.btnSaveChanges)

        /*Save Changes Click*/
        btnSaveChanges.setOnClickListener {

        }
    }
}