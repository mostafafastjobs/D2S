package com.example.nytarticlestask.view.main.listeners

import androidx.recyclerview.widget.RecyclerView
import com.example.nytarticlestask.BuildConfig
import com.example.nytarticlestask.network.model.Results
import com.example.nytarticlestask.view.main.MainVM

class OnArticleImpel(private val viewModel: MainVM): OnArticlesListener {
    override fun onItemClick(data: Results) {

    }

    override fun loadMore(recyclerView: RecyclerView) {
        viewModel.loadData(BuildConfig.ARTICLES_API_KEY)
    }
}