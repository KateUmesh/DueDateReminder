package com.duedatereminder.network

import com.duedatereminder.model.*
import com.duedatereminder.utils.Constant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    /**Splash Screen**/
    @POST(Constant.userAppStatus)
    suspend fun userAppStatus(@Body modelSplashRequest: ModelSplashRequest):Response<ModelSplashResponse>

    /**Create Account*/
    /*sendRegistrationOtp*/
    @POST(Constant.sendRegistrationOtp)
    suspend fun sendRegistrationOtp(@Body modelSendRegistrationOtpRequest: ModelSendRegistrationOtpRequest):Response<ModelSendRegistrationOtpResponse>

    /*createAccount*/
    @POST(Constant.createAccount)
    suspend fun createAccount(@Body modelCreateAccountRequest: ModelCreateAccountRequest):Response<ModelCreateAccountResponse>

    /**Login*/
    @POST(Constant.sendLoginOtp)
    suspend fun sendLoginOtp(@Body modelSendLoginOtpRequest: ModelSendLoginOtpRequest):Response<ModelSendLoginOtpResponse>

    /**Add Client*/
    @POST(Constant.addClient)
    suspend fun addClient(@Body modelAddClientRequest: ModelAddClientRequest):Response<ModelAddClientResponse>

    /**Notification Categories*/
    @GET(Constant.notificationCategories)
    suspend fun notificationCategories():Response<ModelNotificationCategoriesResponse>

    /**All Clients*/
    @GET(Constant.allClients)
    suspend fun allClients():Response<ModelAllClientsResponse>

    /**Edit Client*/
    /*GET Edit Client*/
    @GET(Constant.editClient)
    suspend fun editClient(@Path("idClient") idClient: Int,):Response<ModelEditClientGetResponse>

    /*POST Edit Client*/
    @POST(Constant.editClient)
    suspend fun editClient(@Path("idClient") idClient: Int,@Body modelEditClientRequest: ModelEditClientRequest):Response<ModelEditClientResponse>
}