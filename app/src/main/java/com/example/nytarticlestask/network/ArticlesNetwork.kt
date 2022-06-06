package com.example.nytarticlestask.network

import com.example.nytarticlestask.BuildConfig
import com.example.nytarticlestask.network.api.BaseArticleApi
import com.example.nytarticlestask.network.api.ArticlesApiService
import com.example.nytarticlestask.network.util.NetworkUtil
import com.example.nytarticlestask.useCases.NetworkUseCase
import okhttp3.Authenticator
import okhttp3.Interceptor

object ArticlesNetwork {
    fun getArticleApi(
        baseUrl: String = BuildConfig.BASE_URL,
        interceptors: List<Interceptor> = listOf(),
        networkInterceptors: List<Interceptor> = listOf(),
    ): NetworkUseCase {

        val interceptorList = mutableListOf<Interceptor>()
        interceptorList.addAll(interceptors)

        return BaseArticleApi(
            baseUrl = baseUrl,
            okHttpClient = NetworkUtil.getOkHttpClient(
                interceptors = interceptorList,
                networkInterceptors = networkInterceptors
            )
        )

    }
}