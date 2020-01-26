package com.example.genericlistadapter.adapter.model

import android.view.View
import android.view.ViewGroup
import com.example.genericlistadapter.R
import com.example.genericlistadapter.adapter.GenericListAdapter
import com.example.genericlistadapter.utils.VIEW_TYPE_HEADER
import com.example.genericlistadapter.utils.inflateView

open class HeaderItem : BaseItem() {

    override fun areItemsTheSame(other: Any?): Boolean = other is HeaderItem

    override fun areContentsTheSame(other: Any?): Boolean = other is HeaderItem
}

open class HeaderItemType : BaseItemType<HeaderItem>() {

    override fun getViewType(): Int = VIEW_TYPE_HEADER

    override fun isSameModule(item: BaseItem): Boolean = item is HeaderItem

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericListAdapter.BaseViewHolder {
        val view = parent.inflateView(R.layout.gla_header_item)
        return HeaderItemViewHolder(view)
    }
}

open class HeaderItemViewHolder(view: View) : GenericListAdapter.BaseViewHolder(view)
