package com.example.genericlistadapter.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.genericlistadapter.adapter.model.BaseItemType

class GenericListAdapter private constructor() : ListAdapter<BaseItemType, GenericListAdapter.BaseViewHolder>(
    DiffUtilCallback()
) {

    private constructor(builder: Builder) : this() {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /** Builder class for the adapter following Builder Pattern */
    class Builder {
        fun build(): GenericListAdapter {
            return GenericListAdapter(this)
        }
    }
}
