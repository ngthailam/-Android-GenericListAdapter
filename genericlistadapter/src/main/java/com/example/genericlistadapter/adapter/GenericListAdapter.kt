package com.example.genericlistadapter.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.genericlistadapter.adapter.model.BaseItem
import com.example.genericlistadapter.adapter.model.BaseItemType
import com.example.genericlistadapter.adapter.model.HeaderItem
import com.example.genericlistadapter.adapter.model.HeaderItemType
import java.lang.IllegalArgumentException

class GenericListAdapter private constructor() :
    ListAdapter<BaseItem, GenericListAdapter.BaseViewHolder>(
        DiffUtilCallback()
    ) {
    /** A hashmap containing all view types */
    private var hashMap: HashMap<Int, BaseItemType<BaseItem>> = hashMapOf()
    private var headerItemType: HeaderItemType? = null

    private constructor(builder: Builder) : this() {
        this.hashMap = builder.hashMap
        this.headerItemType = builder.headerItemType as? HeaderItemType
    }

    /** Use in place of ListAdapter`s submit list with custom behavior */
    fun setData(data: List<BaseItem>) {
        val tempList = data.toMutableList()
        if (hasHeader()) tempList.add(0, HeaderItem())
        submitList(tempList)
    }

    override fun getItemViewType(position: Int): Int {
        when (val item = getItem(position)) {
            is HeaderItem -> return HeaderItemType.VIEW_TYPE
            else -> {
                hashMap.keys.forEach { key ->
                    hashMap[key]?.let {
                        if (it.isSameModule(item)) return it.getViewType()
                    }
                }
            }
        }

        throw IllegalArgumentException("GenericListAdapter #getItemViewType view does not have a view type")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            HeaderItemType.VIEW_TYPE -> {
                headerItemType?.onCreateViewHolder(parent, viewType)
                    ?: throw Throwable("GenericListAdapter #onCreateViewHolder Builder setHasHeader is set to false")
            }

            else -> {
                hashMap[viewType]?.onCreateViewHolder(parent, viewType)
                    ?: throw Throwable("GenericListAdapter #onCreateViewHolder return null with parent ${parent::class.java.simpleName}, viewType $viewType ")
            }
        }
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

    private fun hasHeader(): Boolean = this.headerItemType != null

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /** Builder class for the adapter following Builder Pattern */
    class Builder {

        internal val hashMap: HashMap<Int, BaseItemType<BaseItem>> = hashMapOf()
        internal var headerItemType: BaseItemType<BaseItem>? = null

        fun addItemModule(viewType: Int, abc: BaseItemType<out BaseItem>): Builder {
            hashMap[viewType] = abc as? BaseItemType<BaseItem>
                ?: throw Throwable("GenericListAdapter #Builder #addItemModule ItemType must be of type BaseItemType<T: BaseItemType>")

            return this
        }

        fun addHeaderItem(headerItemType: BaseItemType<BaseItem>? = null): Builder {
            this.headerItemType = headerItemType ?: HeaderItemType() as? BaseItemType<BaseItem>
            return this
        }

        fun build(): GenericListAdapter {
            return GenericListAdapter(this)
        }
    }
}
