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
        const val ID_NOTIFICATION="id_notification"

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
    }
}