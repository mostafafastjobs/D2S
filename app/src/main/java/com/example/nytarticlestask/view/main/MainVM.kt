package com.example.nytarticlestask.view.main

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.nytarticlestask.db.dao.ArticlesDataDao
import com.example.nytarticlestask.db.entity.ArticlesTable
import com.example.nytarticlestask.network.model.Media
import com.example.nytarticlestask.network.model.MediaMetadata
import com.example.nytarticlestask.network.model.Results
import com.example.nytarticlestask.useCases.NetworkConnectionUseCase
import com.example.nytarticlestask.useCases.impl.ArticlesUseCases
import com.example.nytarticlestask.util.BaseViewModel
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainVM(
    private val networkConnectionUseCase: NetworkConnectionUseCase,
    private val articlesUseCases: ArticlesUseCases,
    private val dao: ArticlesDataDao
) : BaseViewModel() {

    val networkStateLiveData: MutableLiveData<NetworkConnectionUseCase.NetworkStates> =
        MutableLiveData<NetworkConnectionUseCase.NetworkStates>().apply {
            value = NetworkConnectionUseCase.NetworkStates.NoNetwork
        }

    protected val liveDataAction: MutableLiveData<ArticlesAction> =
        MutableLiveData<ArticlesAction>()

    private var cachedData: MutableList<Results> = mutableListOf()


    fun refresh() {
        articlesUseCases.refresh()
    }

    fun loadData(apiKey: String) {
        articlesUseCases.load(apiKey, object : ArticlesUseCases.OnLoadListener {

            override fun loading() {
                liveDataAction.value = ArticlesAction.Loading
            }

            override fun loaded(data: MutableList<Results>) {
                liveDataAction.value = ArticlesAction.Result(data)
                bindData(data)
            }

            override fun error(ex: Throwable) {
                liveDataAction.value = ArticlesAction.Error(ex)
                Timber.e(ex)
            }
        })
    }


    /**
     * Network Connection States
     */
    fun updateNetworkState(state: NetworkConnectionUseCase.NetworkStates) {
        networkStateLiveData.value = state
    }

    fun registerNetworkConnectionCallback(context: Context) {
        networkConnectionUseCase.onStart(context)
    }

    fun unregisterNetworkConnectionCallback(context: Context) {
        networkConnectionUseCase.onDestroy(context)
    }

    fun observeNetworkConnection(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<NetworkConnectionUseCase.NetworkStates>
    ) {
        networkConnectionUseCase.observe(
            lifecycleOwner,
            observer
        )
    }

    fun observeNetworkState(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<NetworkConnectionUseCase.NetworkStates>
    ) {
        networkStateLiveData.observe(
            lifecycleOwner,
            observer
        )
    }

    fun observeArticlesAction(lifecycle: LifecycleOwner, observer: Observer<ArticlesAction>) {
        liveDataAction.observe(lifecycle, observer)
    }

    /**
     * Cache Data
     */

    fun bindData(data: MutableList<Results>) {
        if (data.size > 0) {
            for (index in data.indices) {
                if (data[index].media.isEmpty()) {
                    val articlesTable = ArticlesTable(
                        data[index].title.toString(),
                        data[index].published_date.toString(),
                        "Empty"
                    )
                    dao.insertArticle(articlesTable).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            object : CompletableObserver {
                                override fun onSubscribe(d: Disposable) {
                                }

                                override fun onComplete() {
                                }

                                override fun onError(e: Throwable) {
                                }
                            }
                        )
                } else {
                    val articlesTable = ArticlesTable.Parse(data[index])
                    dao.insertArticle(articlesTable).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            object : CompletableObserver {
                                override fun onSubscribe(d: Disposable) {
                                }

                                override fun onComplete() {
                                }

                                override fun onError(e: Throwable) {
                                }
                            }
                        )
                }
            }
        }
    }

    fun getCachedData(state: NetworkConnectionUseCase.NetworkStates) {
        (networkStateLiveData as MutableLiveData<NetworkConnectionUseCase.NetworkStates>).value =
            state
        if (networkStateLiveData.value == NetworkConnectionUseCase.NetworkStates.NoNetwork) {
            dao.getAll().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    object : SingleObserver<MutableList<ArticlesTable>> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onSuccess(t: MutableList<ArticlesTable>) {
                            if (t.size > 0) {
                                for (index in t.indices) {
                                    if (t[index].imageUrl == "Empty") {
                                        var results = Results(
                                            title = t[index].title,
                                            published_date = t[index].published_date
                                        )
                                        cachedData.add(results)
                                    } else {
                                        var mediaMetadata: MutableList<MediaMetadata> =
                                            mutableListOf()
                                        mediaMetadata.add(MediaMetadata(url = t[index].imageUrl))
                                        var media: Media = Media(`media-metadata` = mediaMetadata)
                                        var mediaList: MutableList<Media> = mutableListOf()
                                        mediaList.add(media)
                                        var results = Results(
                                            title = t[index].title,
                                            published_date = t[index].published_date,
                                            media = mediaList
                                        )
                                        cachedData.add(results)
                                    }
                                }
                            }
                            liveDataAction.value = ArticlesAction.Result(cachedData)
                        }

                        override fun onError(e: Throwable) {
                        }
                    }
                )
        }
    }

}