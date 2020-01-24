package com.example.genericlistadapter.view

import androidx.recyclerview.widget.RecyclerView

interface GenericListAdapterViewManager {
    fun initialize(adapter: RecyclerView.Adapter<*>): GenericListAdapterView
    fun addLinearLoadMoreListener(onLoadMore: () -> Unit): GenericListAdapterView
    fun setRecyclerViewOptions(options: RecyclerViewOptions?): GenericListAdapterView
    fun getRecyclerView(): RecyclerView?
}
