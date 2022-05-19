package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelCreateAccount() : ViewModel(){
    private var mRepository = RepositoryApi()

    /**Create Account*/
     var mCreateAccountLiveData = MutableLiveData<ModelCreateAccountResponse>()
    fun createAccount(modelCreateAccountRequest: ModelCreateAccountRequest) {
        viewModelScope.launch {
            try {
                val response = mRepository.createAccount(modelCreateAccountRequest)
                if (response.isSuccessful)
                    mCreateAccountLiveData.value = response.body()
                else
                    mCreateAccountLiveData.value =
                        ModelCreateAccountResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mCreateAccountLiveData.value =
                        ModelCreateAccountResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mCreateAccountLiveData.value =
                        ModelCreateAccountResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }

    /**sendRegistrationOtp*/
     var mSendRegistrationOtpLiveData = MutableLiveData<ModelSendRegistrationOtpResponse>()
    fun sendRegistrationOtp(modelSendRegistrationOtpRequest: ModelSendRegistrationOtpRequest) {
        viewModelScope.launch {
            try {
                val response = mRepository.sendRegistrationOtp(modelSendRegistrationOtpRequest)
                if (response.isSuccessful)
                    mSendRegistrationOtpLiveData.value = response.body()
                else
                    mSendRegistrationOtpLiveData.value =
                        ModelSendRegistrationOtpResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mSendRegistrationOtpLiveData.value =
                        ModelSendRegistrationOtpResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mSendRegistrationOtpLiveData.value =
                        ModelSendRegistrationOtpResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }
}