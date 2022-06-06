package com.example.nytarticlestask.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.nytarticlestask.BuildConfig
import com.example.nytarticlestask.view.main.listeners.OnArticleImpel
import com.example.nytarticlestask.view.main.listeners.OnArticlesListener
import com.example.nytarticlestask.view.main.observers.NetworkObserver
import com.example.nytarticlestask.view.main.observers.ArticlesActionObserver
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val view: MainView by inject()
    private val mainVM: MainVM by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view.inflate(layoutInflater, null)?.let { layout ->
            setContentView(layout)
        }

        view.setupViews(this)
        view.setArticlesListener(OnArticleImpel(mainVM))
        mainVM.registerNetworkConnectionCallback(applicationContext)
        mainVM.refresh()
        bindObservers()


    }

    private fun bindObservers() {

        mainVM.loadData(BuildConfig.ARTICLES_API_KEY)

        mainVM.observeNetworkConnection(
            this@MainActivity,
            Observer {
                mainVM.updateNetworkState(it)
                mainVM.getCachedData(it)
            }
        )

        mainVM.observeNetworkState(
            this@MainActivity,
            NetworkObserver(view)
        )

        mainVM.observeArticlesAction(
            this@MainActivity,
            ArticlesActionObserver(view)
        )


    }

    override fun onDestroy() {
        super.onDestroy()
        mainVM.unregisterNetworkConnectionCallback(applicationContext)
    }


}