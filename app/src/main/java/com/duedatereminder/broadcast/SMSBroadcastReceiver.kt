package com.duedatereminder.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SMSBroadcastReceiver : BroadcastReceiver() {
    private var otpReceiveInterface: OtpReceivedInterface? = null

    fun setOnOtpListener(otpReceiveInterface: OtpReceivedInterface?) {
        this.otpReceiveInterface = otpReceiveInterface
    }

    override fun onReceive(context: Context?, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status =
                extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (status!!.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // Get SMS message contents
                    val message =
                        extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                    if (otpReceiveInterface != null) {
                        otpReceiveInterface!!.onOtpReceived(message)
                    }
                    Log.e("SMSBroad",message!!)
                }
                CommonStatusCodes.TIMEOUT -> {
                }
            }
        }
    }
}