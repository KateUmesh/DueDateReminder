package com.duedatereminder.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.duedatereminder.application.ApplicationContext.Companion.context

object LocalSharedPreference {

    var mSharedPreferences:SharedPreferences =context.getSharedPreferences(Constant.DUE_DATE_REMINDER,Context.MODE_PRIVATE)
    var sharedPreferencesEdit: SharedPreferences.Editor = mSharedPreferences.edit()

    fun putStringValue(key: String,value:String?){
        sharedPreferencesEdit.putString(key,value)
        commitValue()
    }

    fun getStringValue(key:String):String?{

        return mSharedPreferences.getString(key,"")
    }

    fun putIntValue(key: String,value:Int){
        sharedPreferencesEdit.putInt(key,value)
        commitValue()
    }

    fun getIntValue(key:String):Int{
        return mSharedPreferences.getInt(key,0)
    }

    fun commitValue(){
        sharedPreferencesEdit.apply()
        sharedPreferencesEdit.commit()
    }

    fun deleteValue(key: String){
        sharedPreferencesEdit.remove(key)
        commitValue()
    }

}