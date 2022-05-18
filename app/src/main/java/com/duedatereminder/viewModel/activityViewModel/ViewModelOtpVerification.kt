package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelOtpVerification() : ViewModel(){
    private var mRepository = RepositoryApi()
     var mResendLoginOtpLiveData = MutableLiveData<ModelSendLoginOtpResponse>()

    fun resendLoginOtp(modelSendLoginOtpRequest: ModelSendLoginOtpRequest) {
        viewModelScope.launch {
            try {
                val response = mRepository.sendLoginOtp(modelSendLoginOtpRequest)
                if (response.isSuccessful)
                    mResendLoginOtpLiveData.value = response.body()
                else
                    mResendLoginOtpLiveData.value =
                        ModelSendLoginOtpResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mResendLoginOtpLiveData.value =
                        ModelSendLoginOtpResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mResendLoginOtpLiveData.value =
                        ModelSendLoginOtpResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }
}