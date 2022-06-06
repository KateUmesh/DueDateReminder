package com.duedatereminder.network

import com.duedatereminder.model.*
import com.duedatereminder.utils.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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
    @GET(Constant.addClient)
    suspend fun addClient():Response<ModelAddClientGetResponse>
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

    /*POST Import Client Csv File*/
    @Multipart
    @POST(Constant.importClientCsvFile)
    suspend fun importClientCsvFile(@Part(Constant.ID_DUE_DATE_CATEGORY) idDueDateCategory: RequestBody, @Part csv_file: MultipartBody.Part):Response<ModelImportClientCsvFileResponse>

    /**GET Notification Templates*/
    @GET(Constant.notificationTemplates)
    suspend fun getNotificationTemplates(@Path("idNotification") idNotification: Int,):Response<ModelNotificationTemplatesResponse>
}