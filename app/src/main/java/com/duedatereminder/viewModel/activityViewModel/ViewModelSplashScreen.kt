package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.ModelSplashRequest
import com.duedatereminder.model.ModelSplashResponse
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelSplashScreen() : ViewModel(){
    private var mRepository = RepositoryApi()
    private var mSplashScreenLiveData = MutableLiveData<ModelSplashResponse>()

    fun userAppStatus(modelSplashRequest: ModelSplashRequest) {
        viewModelScope.launch {
            try {
                val response = mRepository.userAppStatus(modelSplashRequest)
                if (response.isSuccessful)
                    mSplashScreenLiveData.value = response.body()
                else
                    mSplashScreenLiveData.value =
                        ModelSplashResponse(
                            "",
                            Constant.something_went_wrong
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mSplashScreenLiveData.value =
                        ModelSplashResponse(
                            "",
                            Constant.slow_internet_connection_detected
                        )
                else
                    mSplashScreenLiveData.value =
                        ModelSplashResponse(
                            "",
                            Constant.something_went_wrong

                        )
            }
        }
    }
}