package com.duedatereminder.model

class ModelClientDetailsToSendNotificationsResponse (var status:String,var message:String,var data:ClientsData?)

class ClientsData(var clients:ArrayList<ClientsList>?,var totalClients  :String,var send_sms_details:String,var send_email_details:String)
