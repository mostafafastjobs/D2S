package com.example.nytarticlestask.view.main

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nytarticlestask.R
import com.example.nytarticlestask.network.model.Results
import com.example.nytarticlestask.view.main.adapters.ArticlesResultsAdapter
import com.example.nytarticlestask.view.main.listeners.OnArticlesCallBack
import com.example.nytarticlestask.view.main.listeners.OnArticlesListener
import kotlinx.android.synthetic.main.activity_main.view.*
import net.sph.fm92.domains.ListLoadMoreCallback
import timber.log.Timber

class MainViewImpl() : MainView, ResultStateView {

    private var rootView: View? = null
    private val viewCallback = OnArticlesCallBack()
    private  var adapter =  ArticlesResultsAdapter(viewCallback)
    var searchedData: MutableList<Results> = ArrayList()

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): View? {
        rootView = inflater.inflate(R.layout.activity_main, container, false)

        setupRecyclerView()
        return rootView
    }

    override fun setArticlesListener(listener: OnArticlesListener) {
        viewCallback.setListener(listener)
    }

    override fun setupViews(activity: AppCompatActivity) {
    }

    override fun changeNetworkState(state: MainView.State) {
        rootView.let {
            when (state) {
                is MainView.State.Connected -> {
                    it!!.no_internet_message.visibility = View.GONE
                }
                is MainView.State.NoNetwork -> {
                    it!!.no_internet_message.visibility = View.VISIBLE
                }
                else -> {}
            }
        }
    }


    override fun changeState(state: ResultStateView.State) {
        when (state) {
            is ResultStateView.State.NoData -> {
            }
            is ResultStateView.State.Loading -> {
                loadingView()
            }
            is ResultStateView.State.Results -> {
                loadData(state.list)
            }
            is ResultStateView.State.Error -> {
                errorView(state.exception)
            }
            else -> {}
        }
    }


    private fun loadingView() {
        rootView.let {
            it!!.loader.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        rootView?.let {
            it.recyler_News.layoutManager = LinearLayoutManager(it!!.rootView.context)
            it.recyler_News.adapter = adapter
            it.recyler_News.addOnScrollListener(ListLoadMoreCallback(viewCallback))
            adapter.clearList()
        }
    }

    private fun loadData(list: MutableList<Results>) {
        rootView.let {
            it!!.recyler_News.visibility = View.VISIBLE
            it.loader.visibility = View.GONE
            adapter.setData(list)
            onTextChange(list)
        }
    }


    private fun errorView(exception: Throwable) {
        Timber.e(exception)
    }

    private fun onTextChange(list: MutableList<Results>) {
        rootView.let {
            it!!.text_edit_search.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.trim()?.length!! > 0) {
                        filterData(s.toString(), searchedData, list)
                        adapter.clearList()
                        adapter.setData(searchedData)
                        adapter.notifyDataSetChanged()
                    } else if (s?.trim()?.length!! == 0) {
                        adapter.setData(list)
                        adapter.notifyDataSetChanged()
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }


    fun filterData(value: String, filterData: MutableList<Results>, allData: MutableList<Results>) {
        filterData.clear()
        for (i in 0 until allData.size) {
            if (allData[i].title!!.contains(value)) {
                filterData.add(allData[i])
            }
        }
    }

}