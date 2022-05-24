package com.duedatereminder.repository

import com.duedatereminder.model.*
import com.duedatereminder.network.ApiClient

class RepositoryApi {
    /**Splash*/
    suspend fun userAppStatus(modelSplashRequest: ModelSplashRequest)=ApiClient.build().userAppStatus(modelSplashRequest)
    /**Create Account*/
    suspend fun sendRegistrationOtp(modelSendRegistrationOtpRequest: ModelSendRegistrationOtpRequest)=ApiClient.build().sendRegistrationOtp(modelSendRegistrationOtpRequest)
    suspend fun createAccount(modelCreateAccountRequest: ModelCreateAccountRequest)=ApiClient.build().createAccount(modelCreateAccountRequest)
    /**Login*/
    suspend fun sendLoginOtp(modelSendLoginOtpRequest: ModelSendLoginOtpRequest)=ApiClient.build().sendLoginOtp(modelSendLoginOtpRequest)
    /**Add Client*/
    suspend fun addClient(modelAddClientRequest: ModelAddClientRequest)=ApiClient.build().addClient(modelAddClientRequest)
    /**Notification Categories*/
    suspend fun notificationCategories()=ApiClient.build().notificationCategories()
    /**All Client*/
    suspend fun allClients()=ApiClient.build().allClients()
    /**Edit Client*/
    suspend fun editClient(idClient: Int)=ApiClient.build().editClient(idClient)
    suspend fun editClient(idClient: Int,modelEditClientRequest: ModelEditClientRequest)=ApiClient.build().editClient(idClient,modelEditClientRequest)
}