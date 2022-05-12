package com.duedatereminder.network

import com.duedatereminder.model.*
import com.duedatereminder.utils.Constant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

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
}