package com.duedatereminder.network

import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.LocalSharedPreference
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private var serviceApiInterface: ApiInterface? = null

    var baseUrl1="https://ameykajarekar.com/due_date/"
    var baseUrl="https://kuberaduedate.com/app/"

    private fun getUnsafeOkHttpClient():OkHttpClient.Builder{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder=OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
            .connectTimeout(10,TimeUnit.SECONDS)
            .readTimeout(10,TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor{chain->
                val newRequest = chain.request().newBuilder()
                    .addHeader("x-api-key","7a719faf-cd21-460b-adf0-e07e9a400d43")
                    .addHeader("Authorization",Constant.Bearer+LocalSharedPreference.getStringValue(Constant.token))
                    .build()
                chain.proceed(newRequest)
            }

        return builder
    }

    fun build():ApiInterface{
        val builder:Retrofit.Builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())

        val httpClient:OkHttpClient.Builder= getUnsafeOkHttpClient()

        val retrofit:Retrofit = builder.client(httpClient.build()).build()

        serviceApiInterface = retrofit.create(ApiInterface::class.java)

        return  serviceApiInterface as ApiInterface
    }
}