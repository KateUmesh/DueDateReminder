package com.duedatereminder.model

class ModelEditClientRequest(var id_client:String,
                             var name:String,
                             var mobile:String,
                             var whatsapp:String,
                             var email:String,
                             var address:String,
                             var due_date_categories:ArrayList<String>)
