package com.duedatereminder.viewModel.activityViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelMyBalance() : ViewModel(){
    private var mRepository = RepositoryApi()

    /**My Balance*/

     var mModelMyBalanceResponse = MutableLiveData<ModelMyBalanceResponse>()
    fun getMyBalance() {
        viewModelScope.launch {
            try {
                val response = mRepository.getMyBalance()
                if (response.isSuccessful)
                    mModelMyBalanceResponse.value = response.body()
                else
                    mModelMyBalanceResponse.value =
                        ModelMyBalanceResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelMyBalanceResponse.value =
                        ModelMyBalanceResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mModelMyBalanceResponse.value =
                        ModelMyBalanceResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }

}