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

    fun importClientCsvFile(idDueDateCategory: RequestBody, csv_file: RequestBody) {
        viewModelScope.launch {
            try {
                val response = mRepository.importClientCsvFile(idDueDateCategory,csv_file)
                if (response.isSuccessful)
                    mImportClientCsvFileLiveData.value = response.body()
                else
                    mImportClientCsvFileLiveData.value =
                        ModelImportClientCsvFileResponse(
                            "",
                            response.message()
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mImportClientCsvFileLiveData.value =
                        ModelImportClientCsvFileResponse(
                            "",
                            e.message!!
                        )
                else
                    mImportClientCsvFileLiveData.value =
                        ModelImportClientCsvFileResponse(
                            "",e.message!!
                        )
            }
        }
    }
}