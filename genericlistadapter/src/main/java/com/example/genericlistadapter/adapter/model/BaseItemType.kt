package com.example.genericlistadapter.adapter.model

import android.view.ViewGroup
import com.example.genericlistadapter.adapter.GenericListAdapter

abstract class BaseItemType<T : BaseItem> {
    abstract fun getViewType(): Int

    abstract fun isSameModule(item: BaseItem): Boolean

    fun areItemsTheSame(mItem: T, other: Any?): Boolean {
        return mItem.areItemsTheSame(other)
    }

    fun areContentsTheSame(mItem: T, other: Any?): Boolean {
        return mItem.areContentsTheSame(other)
    }

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericListAdapter.BaseViewHolder<T>

    open fun onBindViewHolder(holder: GenericListAdapter.BaseViewHolder<T>, item: T) {
        // Add customize binds here
        holder.onBind(item)
    }
}
