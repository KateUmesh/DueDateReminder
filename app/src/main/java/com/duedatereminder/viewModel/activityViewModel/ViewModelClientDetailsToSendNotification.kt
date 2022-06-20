package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelClientDetailsToSendNotification() : ViewModel(){
    private var mRepository = RepositoryApi()

    /**GET Client Details To Send Notifications*/
    var mModelClientDetailsToSendNotificationsResponse = MutableLiveData<ModelClientDetailsToSendNotificationsResponse>()
    fun getClientDetailsToSendNotifications(idNotificationCategory: Int) {
        viewModelScope.launch {
            try {
                val response = mRepository.getClientDetailsToSendNotifications(idNotificationCategory)
                if (response.isSuccessful)
                    mModelClientDetailsToSendNotificationsResponse.value = response.body()
                else
                    mModelClientDetailsToSendNotificationsResponse.value =
                        ModelClientDetailsToSendNotificationsResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelClientDetailsToSendNotificationsResponse.value =
                        ModelClientDetailsToSendNotificationsResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mModelClientDetailsToSendNotificationsResponse.value =
                        ModelClientDetailsToSendNotificationsResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }

    /**POST Send Sms Notification*/
    var mModelSendSmsNotificationResponse = MutableLiveData<ModelSendSmsNotificationResponse>()
    fun sendSmsNotification(modelSendSmsNotificationRequest: ModelSendSmsNotificationRequest) {
        viewModelScope.launch {
            try {
                val response = mRepository.sendSmsNotification(modelSendSmsNotificationRequest)
                if (response.isSuccessful)
                    mModelSendSmsNotificationResponse.value = response.body()
                else
                    mModelSendSmsNotificationResponse.value =
                        ModelSendSmsNotificationResponse(
                            "",
                            Constant.something_went_wrong
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelSendSmsNotificationResponse.value =
                        ModelSendSmsNotificationResponse(
                            "",
                            Constant.slow_internet_connection_detected
                        )
                else
                    mModelSendSmsNotificationResponse.value =
                        ModelSendSmsNotificationResponse(
                            "",
                            Constant.something_went_wrong
                        )
            }
        }
    }

    /**POST Send Email Notification*/
    var mModelSendEmailNotificationResponse = MutableLiveData<ModelSendEmailNotificationResponse>()
    fun sendEmailNotification(modelSendEmailNotificationRequest: ModelSendEmailNotificationRequest) {
        viewModelScope.launch {
            try {
                val response = mRepository.sendEmailNotification(modelSendEmailNotificationRequest)
                if (response.isSuccessful)
                    mModelSendEmailNotificationResponse.value = response.body()
                else
                    mModelSendEmailNotificationResponse.value =
                        ModelSendEmailNotificationResponse(
                            "",
                            Constant.something_went_wrong
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelSendEmailNotificationResponse.value =
                        ModelSendEmailNotificationResponse(
                            "",
                            Constant.slow_internet_connection_detected
                        )
                else
                    mModelSendEmailNotificationResponse.value =
                        ModelSendEmailNotificationResponse(
                            "",
                            Constant.something_went_wrong
                        )
            }
        }
    }

    /**POST Delete Client*/
    var mModelDeleteClientResponse = MutableLiveData<ModelDeleteClientResponse>()
    fun deleteClient(modelDeleteClientRequest: ModelDeleteClientRequest) {
        viewModelScope.launch {
            try {
                val response = mRepository.deleteClient(modelDeleteClientRequest)
                if (response.isSuccessful)
                    mModelDeleteClientResponse.value = response.body()
                else
                    mModelDeleteClientResponse.value =
                        ModelDeleteClientResponse(
                            "",
                            Constant.something_went_wrong
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelDeleteClientResponse.value =
                        ModelDeleteClientResponse(
                            "",
                            Constant.slow_internet_connection_detected
                        )
                else
                    mModelDeleteClientResponse.value =
                        ModelDeleteClientResponse(
                            "",
                            Constant.something_went_wrong
                        )
            }
        }
    }
}