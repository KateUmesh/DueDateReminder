package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.SocketTimeoutException

class ViewModelImportClientCsvFile() : ViewModel(){
    private var mRepository = RepositoryApi()
     var mImportClientCsvFileLiveData = MutableLiveData<ModelImportClientCsvFileResponse>()

    fun importClientCsvFile(idDueDateCategory: RequestBody, csv_file: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = mRepository.importClientCsvFile(idDueDateCategory,csv_file)
                if (response.isSuccessful)
                    mImportClientCsvFileLiveData.value = response.body()
                else
                    mImportClientCsvFileLiveData.value =
                        ModelImportClientCsvFileResponse(
                            "",
                            Constant.something_went_wrong
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mImportClientCsvFileLiveData.value =
                        ModelImportClientCsvFileResponse(
                            "",
                            Constant.slow_internet_connection_detected
                        )
                else
                    mImportClientCsvFileLiveData.value =
                        ModelImportClientCsvFileResponse(
                            "",
                            Constant.something_went_wrong
                        )
            }
        }
    }
}