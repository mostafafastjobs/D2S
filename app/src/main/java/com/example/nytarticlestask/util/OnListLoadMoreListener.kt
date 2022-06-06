package com.example.nytarticlestask.util

import androidx.recyclerview.widget.RecyclerView

interface OnListLoadMoreListener {
    fun loadMore(recyclerView: RecyclerView)
}