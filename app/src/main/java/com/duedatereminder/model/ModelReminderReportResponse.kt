package com.duedatereminder.model

class ModelReminderReportResponse(var status:String,var message:String,var data:ReminderReportData?)

class ReminderReportData(var report:ArrayList<RemiderReport>)
