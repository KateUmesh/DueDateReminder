package com.duedatereminder.model

class ModelAddClientGetResponse(var status:String,var message:String,var data:AddClientData?)

class AddClientData(var due_date_categories:ArrayList<DueDateCategories>?)