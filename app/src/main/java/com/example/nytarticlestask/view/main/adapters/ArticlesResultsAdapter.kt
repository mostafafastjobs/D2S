package com.example.nytarticlestask.view.main.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nytarticlestask.network.model.Results
import com.example.nytarticlestask.view.main.listeners.OnArticlesListener
import com.example.nytarticlestask.view.main.viewholders.ResultsViewHolder


class ArticlesResultsAdapter(private val listenerArticle: OnArticlesListener/*,var context: Context*/) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val articlesList: MutableList<Results> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ResultsViewHolder.inflate(parent.context, parent, listenerArticle)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ResultsViewHolder -> {
                val newsArticle = articlesList[position]

                holder.bind(newsArticle)
            }
        }
    }

    override fun getItemCount(): Int {
        return articlesList.size
    }

    fun setData(list: MutableList<Results>) {
        val oldSize = itemCount
        list.forEach {
            articlesList.add(it)
        }
        notifyItemRangeInserted(oldSize, itemCount)
    }

    fun clearList() {
        articlesList.clear()
        notifyDataSetChanged()
    }
}