package com.duedatereminder.firebase

import android.content.Context
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.LocalSharedPreference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        LocalSharedPreference.putStringValue(Constant.firebase_token,token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }

    public fun getToken():String{
        return LocalSharedPreference.getStringValue(Constant.firebase_token)!!
    }
}