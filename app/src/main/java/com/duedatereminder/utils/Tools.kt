package com.duedatereminder.utils

import android.app.Activity
import com.duedatereminder.R

class Tools {

    companion object{
        fun setSystemBarColor(activity: Activity){
            val window = activity.window
            window.addFlags(Int.MIN_VALUE)
            window.statusBarColor=activity.getColor(R.color.colorPrimaryDark)
        }
    }
}