package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelAddClient() : ViewModel(){
    private var mRepository = RepositoryApi()

    /**Add Client*/
     var mModelAddClientResponse = MutableLiveData<ModelAddClientResponse>()
    fun addClient(modelAddClientRequest: ModelAddClientRequest) {
        viewModelScope.launch {
            try {
                val response = mRepository.addClient(modelAddClientRequest)
                if (response.isSuccessful)
                    mModelAddClientResponse.value = response.body()
                else
                    mModelAddClientResponse.value =
                        ModelAddClientResponse(
                            "",
                            Constant.something_went_wrong
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelAddClientResponse.value =
                        ModelAddClientResponse(
                            "",
                            Constant.slow_internet_connection_detected
                        )
                else
                    mModelAddClientResponse.value =
                        ModelAddClientResponse(
                            "",
                            Constant.something_went_wrong
                        )
            }
        }
    }

}