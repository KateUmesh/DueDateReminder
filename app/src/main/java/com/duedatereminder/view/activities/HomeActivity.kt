package com.duedatereminder.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.duedatereminder.R
import com.duedatereminder.utils.ContextExtension.Companion.toolbar

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar(getString(R.string.home),false)
    }
}