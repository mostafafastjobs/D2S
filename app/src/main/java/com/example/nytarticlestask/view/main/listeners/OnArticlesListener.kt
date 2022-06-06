package com.example.nytarticlestask.view.main.listeners

import androidx.recyclerview.widget.RecyclerView
import com.example.nytarticlestask.network.model.Results


interface OnArticlesListener {
    fun onItemClick(data: Results)
    fun loadMore(recyclerView: RecyclerView)
}