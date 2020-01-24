package com.example.genericlistadapter.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewEndlessScrollListener(
    private var linearLayoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var isLoading = true
    private var endOfThePage = false

    fun resetState() {
        this.previousTotal = 0
        this.endOfThePage = false
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (endOfThePage) {
            return
        }

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = linearLayoutManager.itemCount
        val firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
        if (isLoading) {
            if (totalItemCount > previousTotal) {
                isLoading = false
                previousTotal = totalItemCount
            }
        }

        if (!isLoading && totalItemCount <= firstVisibleItem + visibleItemCount) {
            isLoading = true

            onLoadMore()
        }
    }

    fun setEndOfThePage(endOfThePage: Boolean) {
        this.endOfThePage = endOfThePage
    }

    fun isLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    abstract fun onLoadMore()
}
