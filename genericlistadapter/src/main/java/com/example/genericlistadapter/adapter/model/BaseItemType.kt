package com.example.genericlistadapter.adapter.model

import android.view.ViewGroup
import com.example.genericlistadapter.adapter.GenericListAdapter

abstract class BaseItemType<T : BaseItem> {
    var onClickListener: OnItemClickListener<T>? = null

    abstract fun getViewType(): Int

    abstract fun isSameModule(item: BaseItem): Boolean

    fun areItemsTheSame(mItem: T, other: Any?): Boolean {
        return mItem.areItemsTheSame(other)
    }

    fun areContentsTheSame(mItem: T, other: Any?): Boolean {
        return mItem.areContentsTheSame(other)
    }

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericListAdapter.BaseViewHolder

    open fun onBindViewHolder(holder: GenericListAdapter.BaseViewHolder, item: T) {
        onClickListener?.let { listener ->
            holder.itemView.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    fun addItemClickListener(listener: OnItemClickListener<T>): BaseItemType<T> {
        onClickListener = listener
        return this
    }
}

interface OnItemClickListener<T> {
    fun onClick(item: T)
}
