package com.duedatereminder.model

class ModelNotificationCategoriesResponse(var status:String,var message:String,var data:NotificationCategoriesData?)

class NotificationCategoriesData(var due_date_categories:ArrayList<DueDateCategories>)