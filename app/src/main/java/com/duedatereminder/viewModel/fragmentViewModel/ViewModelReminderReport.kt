package com.duedatereminder.viewModel.fragmentViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelReminderReport() : ViewModel(){
    private var mRepository = RepositoryApi()

    /**get Due Date Reminder Report*/
     var mModelReminderReportResponse = MutableLiveData<ModelReminderReportResponse>()
    fun getDueDateReminderReport() {
        viewModelScope.launch {
            try {
                val response = mRepository.getDueDateReminderReport()
                if (response.isSuccessful)
                    mModelReminderReportResponse.value = response.body()
                else
                    mModelReminderReportResponse.value =
                        ModelReminderReportResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelReminderReportResponse.value =
                        ModelReminderReportResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mModelReminderReportResponse.value =
                        ModelReminderReportResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }


}