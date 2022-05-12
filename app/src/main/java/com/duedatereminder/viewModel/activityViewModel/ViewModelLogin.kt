package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelLogin() : ViewModel(){
    private var mRepository = RepositoryApi()
    private var mSendLoginOtpLiveData = MutableLiveData<ModelSendLoginOtpResponse>()

    fun sendLoginOtp(modelSendLoginOtpRequest: ModelSendLoginOtpRequest) {
        viewModelScope.launch {
            try {
                val response = mRepository.sendLoginOtp(modelSendLoginOtpRequest)
                if (response.isSuccessful)
                    mSendLoginOtpLiveData.value = response.body()
                else
                    mSendLoginOtpLiveData.value =
                        ModelSendLoginOtpResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mSendLoginOtpLiveData.value =
                        ModelSendLoginOtpResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mSendLoginOtpLiveData.value =
                        ModelSendLoginOtpResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }
}