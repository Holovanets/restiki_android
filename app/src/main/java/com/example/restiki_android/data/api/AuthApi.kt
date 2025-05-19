package com.example.restiki_android.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/by-phone/request-auth")
    suspend fun requestCode(@Body request: PhoneRequest): Response<RequestCodeResponse>

    @POST("auth/by-phone/auth-code")
    suspend fun verifyCode(@Body request: CodeRequest): Response<VerifyResponse>
}

data class PhoneRequest(val phone: String)
data class CodeRequest(val phone: String, val code: String)

data class RequestCodeResponse(
    val message: String? = null,
    val attempts: Int? = null,
    val error: AuthError? = null
)

data class AuthError(
    val blocked: Boolean = false,
    val expired: Long? = null
)

data class VerifyResponse(
    val confirmed: Boolean = false,
    val isNew: Boolean = false,
    val nameNeeded: Boolean = false,
    val authMe: AuthMe? = null,
    val tokens: Tokens? = null
)

data class AuthMe(
    val userId: Int,
    val email: String?,
    val phone: String,
    val confirmations: Confirmations
)

data class Confirmations(
    val email: Boolean,
    val phone: Boolean
)

data class Tokens(
    val auth_token: String,
    val access_token: String,
    val refresh_token: String,
    val __comment: String
)