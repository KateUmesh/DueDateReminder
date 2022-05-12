package com.duedatereminder.model

class ModelSendLoginOtpResponse(var status:String,var message:String,var data:SendLoginOtpData?)

class SendLoginOtpData(var otp:String,var token:String)
