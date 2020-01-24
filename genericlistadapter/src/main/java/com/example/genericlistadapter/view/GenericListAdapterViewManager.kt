package com.example.genericlistadapter.view

import androidx.recyclerview.widget.RecyclerView
import com.example.genericlistadapter.adapter.GenericListAdapter

interface GenericListAdapterViewManager {
    fun initialize(adapter: GenericListAdapter): GenericListAdapterView
    fun addLinearLoadMoreListener(onLoadMore: () -> Unit): GenericListAdapterView
    fun setRecyclerViewOptions(options: RecyclerViewOptions?): GenericListAdapterView
    fun getRecyclerView(): RecyclerView?
}
