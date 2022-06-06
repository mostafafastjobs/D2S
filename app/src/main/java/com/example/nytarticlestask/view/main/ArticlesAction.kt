package com.example.nytarticlestask.view.main

import com.example.nytarticlestask.network.model.Results

sealed class ArticlesAction {

    object Loading : ArticlesAction()
    object NoData : ArticlesAction()
    class Result(val list: MutableList<Results>) : ArticlesAction()
    class Error(val exception: Throwable) : ArticlesAction()

}