package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelAllNotificationTemplatesDisplay() : ViewModel(){
    private var mRepository = RepositoryApi()

    /**All Notification Templates Display*/
    var mModelAllNotificationTemplatesDisplayResponse = MutableLiveData<ModelAllNotificationTemplatesDisplayResponse>()
    fun getAllNotificationTemplatesDisplay() {
        viewModelScope.launch {
            try {
                val response = mRepository.getAllNotificationTemplatesDisplay()
                if (response.isSuccessful)
                    mModelAllNotificationTemplatesDisplayResponse.value = response.body()
                else
                    mModelAllNotificationTemplatesDisplayResponse.value =
                        ModelAllNotificationTemplatesDisplayResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelAllNotificationTemplatesDisplayResponse.value =
                        ModelAllNotificationTemplatesDisplayResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mModelAllNotificationTemplatesDisplayResponse.value =
                        ModelAllNotificationTemplatesDisplayResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }

}