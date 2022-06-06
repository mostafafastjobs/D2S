package com.example.nytarticlestask.view.main.listeners

import androidx.recyclerview.widget.RecyclerView
import com.example.nytarticlestask.network.model.Results


class OnArticlesCallBack : OnArticlesListener {

    private var listener: OnArticlesListener? = null

    fun setListener(listener: OnArticlesListener) {
        this.listener = listener
    }

    override fun onItemClick(data: Results) {
        listener?.onItemClick(data)
    }


    override fun loadMore(recyclerView: RecyclerView) {
        listener?.loadMore(recyclerView)
    }

}