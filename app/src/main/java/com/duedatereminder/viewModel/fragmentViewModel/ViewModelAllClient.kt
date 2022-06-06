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


    /**Notification Categories*/
    var mModelNotificationCategoriesResponse = MutableLiveData<ModelNotificationCategoriesResponse>()
    fun notificationCategories() {
        viewModelScope.launch {
            try {
                val response = mRepository.notificationCategories()
                if (response.isSuccessful)
                    mModelNotificationCategoriesResponse.value = response.body()
                else
                    mModelNotificationCategoriesResponse.value =
                        ModelNotificationCategoriesResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelNotificationCategoriesResponse.value =
                        ModelNotificationCategoriesResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mModelNotificationCategoriesResponse.value =
                        ModelNotificationCategoriesResponse(
                            "", Constant.something_went_wrong,null)
            }
        }
    }

}