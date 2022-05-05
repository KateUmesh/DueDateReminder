package com.duedatereminder.application

import android.app.Application
import android.content.Context

class ApplicationContext:Application() {

    companion object{
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context=this
    }
}