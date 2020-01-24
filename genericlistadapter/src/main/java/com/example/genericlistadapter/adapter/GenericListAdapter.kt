package com.example.genericlistadapter.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.genericlistadapter.adapter.model.BaseItem
import com.example.genericlistadapter.adapter.model.BaseItemType
import java.lang.IllegalArgumentException

class GenericListAdapter private constructor() : ListAdapter<BaseItem, GenericListAdapter.BaseViewHolder>(
    DiffUtilCallback()
) {
    /** A hashmap containing all view types */
    private var hashMap: HashMap<Int, BaseItemType<BaseItem>> = hashMapOf()

    private constructor(builder: Builder) : this() {
        this.hashMap = builder.hashMap
    }

    /** Use in place of ListAdapter`s submit list with custom behavior */
    fun setData(data: List<BaseItem>) {
        submitList(data)
    }

    override fun getItemViewType(position: Int): Int {
        hashMap.keys.forEach { key ->
            val item = getItem(position)
            hashMap[key]?.let {
                if (it.isSameModule(item)) {
                    return it.getViewType()
                }
            }
        }
        throw IllegalArgumentException("GenericListAdapter #getItemViewType view does not have a view type")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return hashMap[viewType]?.onCreateViewHolder(parent, viewType)
            ?: throw Throwable("GenericListAdapter #onCreateViewHolder return null with parent ${parent::class.java.simpleName}, viewType $viewType ")
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        hashMap.keys.forEach loop@{ key ->
            val item = getItem(position)
            hashMap[key]?.let {
                if (it.isSameModule(item)) {
                    it.onBindViewHolder(holder, item)
                    return@loop
                }
            }
        }
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /** Builder class for the adapter following Builder Pattern */
    class Builder {

        val hashMap: HashMap<Int, BaseItemType<BaseItem>> = hashMapOf()

        fun addItemModule(viewType: Int, abc: BaseItemType<out BaseItem>): Builder {
            hashMap[viewType] = abc as? BaseItemType<BaseItem>
                ?: throw Throwable("GenericListAdapter #Builder #addItemModule ItemType must be of type BaseItemType<T: BaseItemType>")

            return this
        }

        fun build(): GenericListAdapter {
            return GenericListAdapter(this)
        }
    }
}
