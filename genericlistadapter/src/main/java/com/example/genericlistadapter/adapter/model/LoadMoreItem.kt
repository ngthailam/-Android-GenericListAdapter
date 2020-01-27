package com.example.genericlistadapter.adapter.model

import android.view.View
import android.view.ViewGroup
import com.example.genericlistadapter.R
import com.example.genericlistadapter.adapter.GenericListAdapter
import com.example.genericlistadapter.utils.VIEW_TYPE_LOADMORE
import com.example.genericlistadapter.utils.inflateView

open class LoadMoreItem : BaseItem() {

    override fun areItemsTheSame(other: Any?): Boolean = other is LoadMoreItem

    override fun areContentsTheSame(other: Any?): Boolean = other is LoadMoreItem
}

open class LoadMoreItemType : BaseItemType<LoadMoreItem>() {

    override fun getViewType(): Int = VIEW_TYPE_LOADMORE

    override fun isSameModule(item: BaseItem): Boolean = item is LoadMoreItem

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericListAdapter.BaseViewHolder<LoadMoreItem> {
        val view = parent.inflateView(R.layout.gla_loadmore_item)
        return LoadMoreItemViewHolder(view)
    }
}

open class LoadMoreItemViewHolder(view: View) : GenericListAdapter.BaseViewHolder<LoadMoreItem>(view)
