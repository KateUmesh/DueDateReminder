package com.duedatereminder.utils

class Constant {

    companion object{
        const val MOBILE_NUMBER="mobileNumber"
        const val OTP="otp"
        const val DUE_DATE_REMINDER="DueDateReminder"
        const val Bearer: String="Bearer "
        const val token="token"
        const val firebase_token="firebase_token"
        const val something_went_wrong: String = "Something went wrong"
        const val slow_internet_connection_detected: String="Slow internet connection detected"
        const val NAME="name"
        const val WHATSAPP="whatsapp"
        const val EMAIL="email"
        const val ADDRESS="ADDRESS"
        const val ID_CLIENT="idClient"
        const val ID_DUE_DATE_CATEGORY="id_due_date_category"
        const val CATEGORY_NAME="category_name"
        const val CSV_FILE="csv_file"
        const val ID_NOTIFICATION="id_notification"
        const val appUrl:String ="market://details?id="
        const val TEMPLATE:String ="template"
        const val USER_NAME:String ="userName"
        const val USER_EMAIL:String ="userEmail"
        const val SEND_SMS_DETAILS:String ="send_sms_details"
        const val SEND_EMAIL_DETAILS:String ="send_email_details"
        const val FIRM_NAME:String ="firm_name"
        const val ACCOUNT_TYPE:String ="account_type"
        const val MENU_STATE:String ="menu_state"

        const val userAppStatus="user/userAppStatus"
        const val sendRegistrationOtp="user/sendRegistrationOtp"
        const val createAccount="user/createAccount"
        const val sendLoginOtp="user/sendLoginOtp"
        const val addClient="user/addClient"
        const val notificationCategories="user/dueDateCategories"
        const val allClients="user/allClients"
        const val editClient="user/editClient/{idClient}"
        const val importClientCsvFile="user/importClientCsvFile"
        const val notificationTemplates="user/notificationTemplates/{idNotificationCategory}"
        const val clientDetailsToSendNotifications="user/clientDetailsToSendNotifications/{idNotificationCategory}"
        const val sendEmailNotification="user/sendEmailNotification"
        const val sendSmsNotification="user/sendSmsNotification"
        const val blogs="user/blogs"
        const val deleteClient="user/deleteClient"
        const val myBalance="user/myBalance"
        const val sendSmsCost="user/sendSmsCost"
    }
}