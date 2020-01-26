package com.example.genericlistadapter.adapter.model

import android.view.View
import android.view.ViewGroup
import com.example.genericlistadapter.R
import com.example.genericlistadapter.adapter.GenericListAdapter
import com.example.genericlistadapter.utils.inflateView

class HeaderItem : BaseItem() {

    override fun areItemsTheSame(other: Any?): Boolean = other is HeaderItem

    override fun areContentsTheSame(other: Any?): Boolean = other is HeaderItem
}

class HeaderItemType : BaseItemType<HeaderItem>() {

    companion object {
        const val VIEW_TYPE = -5
    }

    override fun getViewType(): Int = VIEW_TYPE

    override fun isSameModule(item: BaseItem): Boolean = item is HeaderItem

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericListAdapter.BaseViewHolder {
        val view = parent.inflateView(R.layout.gla_header_item)
        return HeaderItemViewHolder(view)
    }
}

class HeaderItemViewHolder(view: View) : GenericListAdapter.BaseViewHolder(view)
