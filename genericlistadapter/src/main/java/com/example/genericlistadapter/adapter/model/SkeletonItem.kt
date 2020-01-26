package com.example.genericlistadapter.adapter.model

import android.view.View
import android.view.ViewGroup
import com.example.genericlistadapter.R
import com.example.genericlistadapter.adapter.GenericListAdapter
import com.example.genericlistadapter.utils.VIEW_TYPE_LOADMORE
import com.example.genericlistadapter.utils.inflateView

open class SkeletonItem : BaseItem() {

    override fun areItemsTheSame(other: Any?): Boolean = other is SkeletonItem

    override fun areContentsTheSame(other: Any?): Boolean = other is SkeletonItem
}

open class SkeletonItemType : BaseItemType<SkeletonItem>() {

    override fun getViewType(): Int = VIEW_TYPE_LOADMORE

    override fun isSameModule(item: BaseItem): Boolean = item is SkeletonItem

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericListAdapter.BaseViewHolder {
        val view = parent.inflateView(R.layout.gla_skeleton_item)
        return SkeletonItemViewHolder(view)
    }
}

open class SkeletonItemViewHolder(view: View) : GenericListAdapter.BaseViewHolder(view)
