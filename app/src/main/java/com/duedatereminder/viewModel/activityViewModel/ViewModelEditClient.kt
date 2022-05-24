package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelEditClient() : ViewModel(){
    private var mRepository = RepositoryApi()

    /**GET Edit Client*/
     var mModelEditClientGetResponse = MutableLiveData<ModelEditClientGetResponse>()
    fun editClient(idClient: Int) {
        viewModelScope.launch {
            try {
                val response = mRepository.editClient(idClient)
                if (response.isSuccessful)
                    mModelEditClientGetResponse.value = response.body()
                else
                    mModelEditClientGetResponse.value =
                        ModelEditClientGetResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelEditClientGetResponse.value =
                        ModelEditClientGetResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mModelEditClientGetResponse.value =
                        ModelEditClientGetResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }

    /**POST Edit Client*/
    var mModelEditClientResponse = MutableLiveData<ModelEditClientResponse>()
    fun editClient(idClient: Int,mModelEditClientRequest: ModelEditClientRequest) {
        viewModelScope.launch {
            try {
                val response = mRepository.editClient(idClient,mModelEditClientRequest)
                if (response.isSuccessful)
                    mModelEditClientResponse.value = response.body()
                else
                    mModelEditClientResponse.value =
                        ModelEditClientResponse(
                            "",
                            Constant.something_went_wrong
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelEditClientResponse.value =
                        ModelEditClientResponse(
                            "",
                            Constant.slow_internet_connection_detected
                        )
                else
                    mModelEditClientResponse.value =
                        ModelEditClientResponse(
                            "",
                            Constant.something_went_wrong
                        )
            }
        }
    }

}