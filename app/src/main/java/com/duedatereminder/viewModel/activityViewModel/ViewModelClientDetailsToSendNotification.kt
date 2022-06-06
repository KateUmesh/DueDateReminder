package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelClientDetailsToSendNotification() : ViewModel(){
    private var mRepository = RepositoryApi()

    /**GET Client Details To Send Notifications*/

    var mModelClientDetailsToSendNotificationsResponse = MutableLiveData<ModelClientDetailsToSendNotificationsResponse>()
    fun getClientDetailsToSendNotifications(idNotificationCategory: Int) {
        viewModelScope.launch {
            try {
                val response = mRepository.getClientDetailsToSendNotifications(idNotificationCategory)
                if (response.isSuccessful)
                    mModelClientDetailsToSendNotificationsResponse.value = response.body()
                else
                    mModelClientDetailsToSendNotificationsResponse.value =
                        ModelClientDetailsToSendNotificationsResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelClientDetailsToSendNotificationsResponse.value =
                        ModelClientDetailsToSendNotificationsResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mModelClientDetailsToSendNotificationsResponse.value =
                        ModelClientDetailsToSendNotificationsResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }

}