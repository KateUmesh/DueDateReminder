package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelSendMessage() : ViewModel(){
    private var mRepository = RepositoryApi()


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

    /**POST Send Sms Cost*/
    var mModelSendSmsCostResponse = MutableLiveData<ModelSendSmsCostResponse>()
    fun sendSmsCost(modelSendSmsCostRequest: ModelSendSmsCostRequest) {
        viewModelScope.launch {
            try {
                val response = mRepository.sendSmsCost(modelSendSmsCostRequest)
                if (response.isSuccessful)
                    mModelSendSmsCostResponse.value = response.body()
                else
                    mModelSendSmsCostResponse.value =
                        ModelSendSmsCostResponse(
                            "",
                            Constant.something_went_wrong
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelSendSmsCostResponse.value =
                        ModelSendSmsCostResponse(
                            "",
                            Constant.slow_internet_connection_detected
                        )
                else
                    mModelSendSmsCostResponse.value =
                        ModelSendSmsCostResponse(
                            "",
                            Constant.something_went_wrong
                        )
            }
        }
    }

}