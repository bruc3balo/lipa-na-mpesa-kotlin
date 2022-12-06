package com.mpesa.kotlindemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mpesa.kotlindemo.models.LipaNaMpesaRequest
import com.mpesa.kotlindemo.models.LipaNaMpesaResponse
import com.mpesa.kotlindemo.models.MpesaToken
import com.mpesa.kotlindemo.network.MpesaCalls
import com.mpesa.kotlindemo.retrofit.RetrofitClient
import retrofit2.Response
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class MpesaViewModel(application: Application) : AndroidViewModel(application) {

    private var mpesaCalls: MpesaCalls = RetrofitClient.getInstance().create(MpesaCalls::class.java)
    private val CONSUMER_SECRET=""
    private val PASS_KEY =""
    private val CONSUMER_KEY=""
    private val SHORT_CODE ="174379"
    private val CALLBACK_URL ="https://httpdump.app/inspect/c3f405d5-ae68-4815-87da-1fb1750524af"
    private val TRANSACTION_TYPE ="CustomerPayBillOnline"


    private fun createAuthTokenRequest() : MpesaToken {
        val mpesaTokenResponse: Response<MpesaToken> = mpesaCalls.sendGetAuthenticationTokenRequest(createBasicHeader(createBasicToken())).execute()
         if(mpesaTokenResponse.isSuccessful) return mpesaTokenResponse.body()!!
         else throw java.lang.RuntimeException(mpesaTokenResponse.message())
    }

    private fun createStkPushRequest(amount: String, description: String, phoneNumber: String, account: String): String {
         val token: MpesaToken = createAuthTokenRequest()
         val request = LipaNaMpesaRequest(account, amount, SHORT_CODE, CALLBACK_URL, phoneNumber, SHORT_CODE, createEncodedPassword(),phoneNumber,getCurrentTimestamp(),description,TRANSACTION_TYPE)
         val stkResponse: Response<LipaNaMpesaResponse> = mpesaCalls.sendLipaNaMpesaRequest(createBearerHeader(token.access_token), request).execute()
         return stkResponse.message()
     }

    fun sendLipaNaMpesaRequest(amount: String, description: String, phoneNumber: String, account: String) : Boolean {
        return try {
            val message:String = createStkPushRequest(amount, description, phoneNumber, account)
            print(message)
            true
        } catch (e: java.lang.Exception) {
            false
        }
    }

    ///functions///
    //auth
    private fun createBasicHeader(token: String) : String {
        return "Basic $token"
    }

    private fun createBearerHeader(token: String) : String {
        return "Bearer $token"
    }

    private fun createBasicToken() : String {
        val basicToken = "$CONSUMER_KEY:$CONSUMER_SECRET";
        val bytesAuth = basicToken.toByteArray(charset("ISO-8859-1"))
        return Base64.getEncoder().encodeToString(bytesAuth)
    }

    //timestamp
    private fun getCurrentTimestamp(): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val timestamp = Timestamp(System.currentTimeMillis())
        return simpleDateFormat.format(timestamp)
    }

    //password
    private fun createEncodedPassword(): String {
        val password = "$SHORT_CODE$PASS_KEY${getCurrentTimestamp()}"
        val bytesPassword: ByteArray = password.toByteArray(StandardCharsets.ISO_8859_1)
        return Base64.getEncoder().encodeToString(bytesPassword)
    }

}