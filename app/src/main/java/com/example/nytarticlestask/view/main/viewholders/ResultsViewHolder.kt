package com.example.nytarticlestask.view.main.viewholders

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nytarticlestask.R
import com.example.nytarticlestask.network.model.Results
import com.example.nytarticlestask.view.main.listeners.OnArticlesListener
import kotlinx.android.synthetic.main.item_news.view.*
import org.koin.core.component.KoinComponent

class ResultsViewHolder(
    private val view: View,
    private val listener: OnArticlesListener
) : RecyclerView.ViewHolder(view), View.OnClickListener, KoinComponent {

    private var data: Results? = null

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        data.let {
            when (v) {
                view -> {
                    listener.onItemClick(it!!)
                }
            }
        }
    }

    fun bind(data: Results) {
        this.data = data

        if (data.media.size > 0) {
            if (!data.media[0].`media-metadata`[0].url.isNullOrEmpty()) {
                Glide.with(view.context)
                    .load(data.media[0].`media-metadata`[0].url)
                    .into(itemView.article_image)
            }
        } else {
            Glide.with(view.context)
                .load(R.drawable.placeholder_image)
                .into(itemView.article_image)
        }

        itemView.tv_DatePublished.text = data.published_date.toString()
        itemView.tv_Title.text = data.title
    }


    companion object {
        fun inflate(
            context: Context,
            viewGroup: ViewGroup,
            listener: OnArticlesListener
        ): ResultsViewHolder {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.item_news, viewGroup, false)
            return ResultsViewHolder(view, listener)
        }
    }
}