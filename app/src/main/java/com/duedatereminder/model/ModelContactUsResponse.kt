package com.duedatereminder.model

class ModelContactUsResponse(var status:String,var message:String,var data:ContactUsData?)

class ContactUsData(var business_queries_email:String,var app_related_queries_email:String,var customer_care_mobile:String)
