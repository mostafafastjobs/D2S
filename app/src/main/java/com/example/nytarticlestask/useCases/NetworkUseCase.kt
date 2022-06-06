package com.example.nytarticlestask.useCases

import com.example.nytarticlestask.network.model.ArticlesInfo
import io.reactivex.Single

interface NetworkUseCase {

    fun callBackArticlesData(period: String,apiKey: String): Single<ArticlesInfo>

}