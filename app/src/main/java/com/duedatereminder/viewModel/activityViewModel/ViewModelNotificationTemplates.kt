package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelNotificationTemplates() : ViewModel(){
    private var mRepository = RepositoryApi()

    /**Notification Templates*/


     var mModelNotificationTemplatesResponse = MutableLiveData<ModelNotificationTemplatesResponse>()
    fun getNotificationTemplates(idNotification: Int) {
        viewModelScope.launch {
            try {
                val response = mRepository.getNotificationTemplates(idNotification)
                if (response.isSuccessful)
                    mModelNotificationTemplatesResponse.value = response.body()
                else
                    mModelNotificationTemplatesResponse.value =
                        ModelNotificationTemplatesResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelNotificationTemplatesResponse.value =
                        ModelNotificationTemplatesResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mModelNotificationTemplatesResponse.value =
                        ModelNotificationTemplatesResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }

}