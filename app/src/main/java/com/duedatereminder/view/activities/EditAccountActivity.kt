package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import androidx.core.text.isDigitsOnly
import com.duedatereminder.R
import com.duedatereminder.utils.ContextExtension
import com.duedatereminder.utils.ContextExtension.Companion.hideKeyboard
import com.duedatereminder.utils.ContextExtension.Companion.showOkFinishActivityDialog
import com.duedatereminder.utils.ContextExtension.Companion.toolbar
import com.google.android.material.textfield.TextInputEditText

class EditAccountActivity : AppCompatActivity() {
    private lateinit var tietName : TextInputEditText
    private lateinit var tietEmail : TextInputEditText
    private lateinit var tietMobileNumber : TextInputEditText
    private lateinit var tietWhatsappNumber : TextInputEditText
    private lateinit var tietAddress : TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)

        /*Toolbar*/
        toolbar(getString(R.string.editAccount),true)

        /*Initialize variables*/
        tietName  = findViewById(R.id.tietName)
        tietEmail = findViewById(R.id.tietEmail)
        tietMobileNumber = findViewById(R.id.tietMobileNumber)
        tietWhatsappNumber = findViewById(R.id.tietWhatsappNumber)
        tietAddress = findViewById(R.id.tietAddress)
        val btnSaveChanges : Button = findViewById(R.id.btnSaveChanges)

        /*Save Changes Click*/
        btnSaveChanges.setOnClickListener {
            hideKeyboard(tietAddress)
            validateInput()
        }
    }

    private fun validateInput(){
        if(tietName.text.toString().isEmpty()){
            ContextExtension.snackBar(getString(R.string.name_required), this)
        }else if(tietEmail.text.toString().isEmpty()or!isEmailValid(tietEmail.text.toString())){
            ContextExtension.snackBar(getString(R.string.invalid_email_address), this)
        }else if(tietMobileNumber.text.toString().isEmpty()|| tietMobileNumber.text.toString().length!=10|| !tietMobileNumber.text.toString().isDigitsOnly()){
            ContextExtension.snackBar(getString(R.string.invalid_number), this)
        }else if(tietWhatsappNumber.text.toString().isEmpty()||tietWhatsappNumber.text.toString().length!=10|| !tietWhatsappNumber.text.toString().isDigitsOnly()){
            ContextExtension.snackBar(getString(R.string.invalid_whatsapp_number), this)
        }else if(tietAddress.text.toString().isEmpty()|| tietAddress.text.toString().length<20){
            ContextExtension.snackBar(getString(R.string.enter_full_address), this)
        }else{
            showOkFinishActivityDialog("Saved Changes Successfully",this)
        }
    }

    private fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }
}