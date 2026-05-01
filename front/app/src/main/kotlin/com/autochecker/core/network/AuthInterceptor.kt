package com.autochecker.core.network

import com.autochecker.data.local.DataStoreManager
import com.autochecker.data.local.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val dataStoreManager: DataStoreManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenManager.token.first() }
        val language = runBlocking { dataStoreManager.language.first() }

        val requestBuilder = chain.request().newBuilder()
            .addHeader("Accept-Language", language)

        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
