package com.example.nytarticlestask.useCases.impl

import android.util.Log
import com.example.nytarticlestask.network.model.ArticlesInfo
import com.example.nytarticlestask.network.model.Results
import com.example.nytarticlestask.useCases.NetworkUseCase
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ArticlesUseCases(private val networkUseCase: NetworkUseCase) {

    private val LIMIT = arrayOf<String>("1","7","30")
    private  var index = 0
    private var currentPage = LIMIT[index]

    private var isLoadingData = false


    fun load(apiKey: String, listener: OnLoadListener) {

        if (index > 2){
           Log.d("Pagination State .... ","No More Pagination ")
        }else {
            if (isLoadingData) {
                listener.loading()
                return
            }

            isLoadingData = true
            networkUseCase.callBackArticlesData(currentPage, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ArticlesInfo> {
                    override fun onSubscribe(d: Disposable) {
                        listener.loading()
                    }

                    override fun onSuccess(response: ArticlesInfo) {

                        if(response.results.isNotEmpty()) {
                            index++
                        }
                        isLoadingData = false
                        listener.loaded(response.results)

                    }

                    override fun onError(ex: Throwable) {
                        isLoadingData = false
                        listener.error(ex)
                    }
                })
        }


    }

    /**
     * Reset current pagination to 0
     */
    fun refresh() {
        currentPage = "1"
    }

    interface OnLoadListener {
        fun loading()
        fun loaded(data: MutableList<Results>)
        fun error(ex: Throwable)
    }
}