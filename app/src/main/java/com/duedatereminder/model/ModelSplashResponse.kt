package com.duedatereminder.model

class ModelSplashResponse(var status:String,var message:String,var data:SplashData?) {
    class SplashData(var name:String,var email:String)
}