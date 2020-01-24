package com.example.genericlistadapter.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.genericlistadapter.adapter.model.BaseItem

open class DiffUtilCallback : DiffUtil.ItemCallback<BaseItem>() {
    override fun areItemsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}
