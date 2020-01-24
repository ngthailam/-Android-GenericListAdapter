package com.example.genericlistadapter.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.genericlistadapter.adapter.model.BaseItemType

open class DiffUtilCallback : DiffUtil.ItemCallback<BaseItemType>() {
    override fun areItemsTheSame(oldItem: BaseItemType, newItem: BaseItemType): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: BaseItemType, newItem: BaseItemType): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}
