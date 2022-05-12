package com.duedatereminder.model

class ModelCreateAccountResponse(var status:String,var message:String,var data:CreateAccountData)

class CreateAccountData(var token:String)