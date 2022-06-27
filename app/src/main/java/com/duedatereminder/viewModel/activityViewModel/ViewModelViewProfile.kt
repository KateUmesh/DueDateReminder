package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelViewProfile() : ViewModel(){
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