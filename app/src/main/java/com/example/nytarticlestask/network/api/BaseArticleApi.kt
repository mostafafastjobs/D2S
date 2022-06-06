package com.example.nytarticlestask.network.api

import com.example.nytarticlestask.network.model.ArticlesInfo
import com.example.nytarticlestask.network.util.NetworkUtil
import com.example.nytarticlestask.useCases.NetworkUseCase
import io.reactivex.Single
import okhttp3.OkHttpClient

open class BaseArticleApi (baseUrl: String,okHttpClient: OkHttpClient)
    : ArticlesApiService, NetworkUseCase {

    private var apiService: ArticlesApiService = NetworkUtil.getRetrofit(
        baseUrl,okHttpClient
    ).create(ArticlesApiService::class.java)

    override fun callBackArticlesData(
         period: String ,apiKey: String
    ): Single<ArticlesInfo> {
        return apiService.callBackArticlesData(period,apiKey)
    }
}