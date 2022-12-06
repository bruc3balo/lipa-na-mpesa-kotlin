package com.mpesa.kotlindemo.models

data class LipaNaMpesaResponse(
    val CheckoutRequestID: String,
    val CustomerMessage: String,
    val MerchantRequestID: String,
    val ResponseCode: String,
    val ResponseDescription: String
)