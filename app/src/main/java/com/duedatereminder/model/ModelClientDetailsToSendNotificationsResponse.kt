package com.duedatereminder.model

class ModelClientDetailsToSendNotificationsResponse (var status:String,var message:String,var data:ClientsData?)

class ClientsData(var clients:ArrayList<ClientsList>?)
