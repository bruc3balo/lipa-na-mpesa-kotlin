package com.mpesa.kotlindemo.network

import com.mpesa.kotlindemo.models.LipaNaMpesaRequest
import com.mpesa.kotlindemo.models.LipaNaMpesaResponse
import com.mpesa.kotlindemo.models.MpesaToken
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MpesaCalls {
    @GET("oauth/v1/generate?grant_type=client_credentials")
    fun sendGetAuthenticationTokenRequest(@Header("Authorization") basicHeader: String) : Call<MpesaToken>

    @POST("mpesa/stkpush/v1/processrequest")
    fun sendLipaNaMpesaRequest(@Header("Authorization") token: String, @Body request: LipaNaMpesaRequest) : Call<LipaNaMpesaResponse>
}