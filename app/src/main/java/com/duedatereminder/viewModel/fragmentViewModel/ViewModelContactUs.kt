package com.duedatereminder.viewModel.fragmentViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelContactUs() : ViewModel(){
    private var mRepository = RepositoryApi()

    /**get Contact Us*/
     var mModelContactUsResponse = MutableLiveData<ModelContactUsResponse>()
    fun getContactUs() {
        viewModelScope.launch {
            try {
                val response = mRepository.getContactUs()
                if (response.isSuccessful)
                    mModelContactUsResponse.value = response.body()
                else
                    mModelContactUsResponse.value =
                        ModelContactUsResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelContactUsResponse.value =
                        ModelContactUsResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mModelContactUsResponse.value =
                        ModelContactUsResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }


    /**Notification Categories*/
    var mModelSubmitFeedbackResponse = MutableLiveData<ModelSubmitFeedbackResponse>()
    fun submitFeedback(mModelSubmitFeedbackRequest: ModelSubmitFeedbackRequest) {
        viewModelScope.launch {
            try {
                val response = mRepository.submitFeedback(mModelSubmitFeedbackRequest)
                if (response.isSuccessful)
                    mModelSubmitFeedbackResponse.value = response.body()
                else
                    mModelSubmitFeedbackResponse.value =
                        ModelSubmitFeedbackResponse(
                            "",
                            Constant.something_went_wrong
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelSubmitFeedbackResponse.value =
                        ModelSubmitFeedbackResponse(
                            "",
                            Constant.slow_internet_connection_detected
                        )
                else
                    mModelSubmitFeedbackResponse.value =
                        ModelSubmitFeedbackResponse(
                            "", Constant.something_went_wrong)
            }
        }
    }

}