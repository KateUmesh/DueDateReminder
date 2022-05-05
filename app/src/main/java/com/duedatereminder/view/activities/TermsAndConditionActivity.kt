package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.duedatereminder.R
import com.duedatereminder.utils.ContextExtension.Companion.toolbar

class TermsAndConditionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_condition)

        /*Toolbar*/
        toolbar(getString(R.string.terms_and_condition),true)
    }
}