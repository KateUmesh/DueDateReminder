package com.duedatereminder.model

class ModelAllClientsResponse(var status:String,var message:String,var data:AllClientsData?)

class AllClientsData(var clients:ArrayList<AllClients>?)