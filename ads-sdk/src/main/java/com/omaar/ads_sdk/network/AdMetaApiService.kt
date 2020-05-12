package com.omaar.ads_sdk.network

import com.google.gson.GsonBuilder
import com.omaar.ads_sdk.model.AdMeta
import kotlinx.coroutines.Deferred
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


private const val BASE_URL = "https://subway-ticketing-system.herokuapp.com"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
    .build()

internal interface AdMetaApiService {

    @POST("ads/increment")
    suspend fun incrementClickAsync(
        @Header("Authorization") authorization: String,
        @Query("category") category: String
        , @Query("brandName") brandName: String
    )

    @GET("ads/request")
    suspend fun requestAdAsync(@Header("Authorization") authorization: String): AdMeta

}

internal object AdMetaApi {
    val retrofitService: AdMetaApiService by lazy {
        retrofit.create(AdMetaApiService::class.java)
    }
}
