package com.kta.app.data.respone

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("errors")
    val errors: ErrorDetail? = null
)

data class ErrorDetail(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)
