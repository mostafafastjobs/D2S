package com.example.nytarticlestask.useCases.impl

import com.example.nytarticlestask.network.api.ArticlesApiService
import com.example.nytarticlestask.network.model.ArticlesInfo
import com.example.nytarticlestask.useCases.NetworkUseCase
import io.reactivex.Single

class NetworkUseCaseImpl(private val newsApi: ArticlesApiService) : NetworkUseCase {

    override fun callBackArticlesData(
        period: String,apiKey: String
    ): Single<ArticlesInfo> {
        return newsApi.callBackArticlesData(period,apiKey)
    }
}