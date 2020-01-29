package com.example.genericlistadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.genericlistadapter.adapter.GenericListAdapter
import com.example.genericlistadapter.adapter.model.BaseItemType
import com.example.genericlistadapter.adapter.model.SkeletonItem
import com.example.genericlistadapter.adapter.model.SkeletonItemType
import com.example.genericlistadapter.adapter.model.SkeletonItemViewHolder

class CustomSkeletonItem : SkeletonItemType() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericListAdapter.BaseViewHolder<SkeletonItem> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gla_example_skeleton_item, parent, false)
        return SkeletonItemViewHolder(view)
    }
}