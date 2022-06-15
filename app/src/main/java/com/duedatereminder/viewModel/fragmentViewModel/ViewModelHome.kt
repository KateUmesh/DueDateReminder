package com.duedatereminder.viewModel.fragmentViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duedatereminder.model.*
import com.duedatereminder.repository.RepositoryApi
import com.duedatereminder.utils.Constant
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ViewModelHome() : ViewModel(){
    private var mRepository = RepositoryApi()

    /**Blogs*/
     var mModelBlogsResponse = MutableLiveData<ModelBlogsResponse>()
    fun getBlogs() {
        viewModelScope.launch {
            try {
                val response = mRepository.getBlogs()
                if (response.isSuccessful)
                    mModelBlogsResponse.value = response.body()
                else
                    mModelBlogsResponse.value =
                        ModelBlogsResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    mModelBlogsResponse.value =
                        ModelBlogsResponse(
                            "",
                            Constant.slow_internet_connection_detected,null
                        )
                else
                    mModelBlogsResponse.value =
                        ModelBlogsResponse(
                            "",
                            Constant.something_went_wrong,null
                        )
            }
        }
    }


}