package com.duedatereminder.model


class ModelEditClientGetResponse(var status:String,var message:String,var data:EditClientGetData?)

class EditClientGetData(var due_date_categories:ArrayList<DueDateCategories>?,var client: Client)

class Client(var id_client:String,var name:String,var mobile:String,
             var whatsapp:String,var email:String,var address:String,var due_date_categories:ArrayList<String>)


