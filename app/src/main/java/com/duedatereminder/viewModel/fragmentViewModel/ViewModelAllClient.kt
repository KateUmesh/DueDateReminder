package com.duedatereminder.viewModel.fragmentViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelAllClient() : ViewModel(){
    private var mRepository = RepositoryApi()

    /**All Clients*/
     var mModelAllClientsResponse = MutableLiveData<ModelAllClientsResponse>()
    fun allClients() {
        viewModelScope.launch {
            try {
                val response = mRepository.allClients()
                if (response.isSuccessful)
                    mModelAllClientsResponse.value = response.body()
                else
                    mModelAllClientsResponse.value =
                        ModelAllClientsResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelAllClientsResponse.value =
                        ModelAllClientsResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mModelAllClientsResponse.value =
                        ModelAllClientsResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }

}