package com.duedatereminder.repository

import com.duedatereminder.model.ModelCreateAccountRequest
import com.duedatereminder.model.ModelSendLoginOtpRequest
import com.duedatereminder.model.ModelSendRegistrationOtpRequest
import com.duedatereminder.model.ModelSplashRequest
import com.duedatereminder.network.ApiClient

class RepositoryApi {
    /**Splash*/
    suspend fun userAppStatus(modelSplashRequest: ModelSplashRequest)=ApiClient.build().userAppStatus(modelSplashRequest)
    /**Create Account*/
    suspend fun sendRegistrationOtp(modelSendRegistrationOtpRequest: ModelSendRegistrationOtpRequest)=ApiClient.build().sendRegistrationOtp(modelSendRegistrationOtpRequest)
    suspend fun createAccount(modelCreateAccountRequest: ModelCreateAccountRequest)=ApiClient.build().createAccount(modelCreateAccountRequest)
    /**Login*/
    suspend fun sendLoginOtp(modelSendLoginOtpRequest: ModelSendLoginOtpRequest)=ApiClient.build().sendLoginOtp(modelSendLoginOtpRequest)
}