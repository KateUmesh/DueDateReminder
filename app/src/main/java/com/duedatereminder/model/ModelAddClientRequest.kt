package com.duedatereminder.model

class ModelAddClientRequest(var name:String,
                            var mobile:String,
                            var whatsapp:String,
                            var email:String,
                            var address:String,
                            var due_date_categories:ArrayList<String>)
