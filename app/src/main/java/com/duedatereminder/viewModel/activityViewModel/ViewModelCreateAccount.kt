package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.ModelCreateAccountRequest
import com.duedatereminder.model.ModelCreateAccountResponse
import com.duedatereminder.model.ModelSplashRequest
import com.duedatereminder.model.ModelSplashResponse
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelCreateAccount() : ViewModel(){
    private var mRepository = RepositoryApi()
    private var mCreateAccountLiveData = MutableLiveData<ModelCreateAccountResponse>()

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
}