package net.sph.fm92.domains

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nytarticlestask.util.OnListLoadMoreListener
import com.example.nytarticlestask.view.main.listeners.OnArticlesListener

class ListLoadMoreCallback(private val listener: OnArticlesListener)
    : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {
            recyclerView.layoutManager?.let {
                val visibleItemCount = it.childCount
                val totalItemCount = it.itemCount
                // grid also extends from linearLayoutManager
                val pastVisibleItems = (it as LinearLayoutManager).findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    listener.loadMore(recyclerView)
                }
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }
}