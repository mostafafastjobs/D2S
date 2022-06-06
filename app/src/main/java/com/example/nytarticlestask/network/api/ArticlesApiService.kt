package com.example.nytarticlestask.network.api

import com.example.nytarticlestask.network.model.ArticlesInfo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticlesApiService {

    @GET("emailed/{Period}.json?")
    fun callBackArticlesData(
        @Path("Period") period: String,
        @Query("api-key") apiKey: String
    ): Single<ArticlesInfo>

}