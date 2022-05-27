package com.duedatereminder.broadcast

interface OtpReceivedInterface
{
    fun onOtpReceived(otp: String?)
    fun onOtpTimeout()
}