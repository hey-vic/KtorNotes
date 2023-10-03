package com.myprojects.ktornotes.data.remote

import com.myprojects.ktornotes.data.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = authManager.accessToken
        token?.let {
            request.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}