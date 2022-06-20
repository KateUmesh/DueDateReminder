package com.duedatereminder.model

class ModelMyBalanceResponse(var status:String,var message:String,var data:MyBalanceData?)

class MyBalanceData(var sms_balance:String)