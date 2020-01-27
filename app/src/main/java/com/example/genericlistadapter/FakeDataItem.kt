package com.example.genericlistadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.genericlistadapter.adapter.GenericListAdapter
import com.example.genericlistadapter.adapter.model.BaseItem
import com.example.genericlistadapter.adapter.model.BaseItemType
import kotlinx.android.synthetic.main.item_fake.view.*

data class FakeDataItem(
    val id: String,
    val number: Int
): BaseItem() {
    override fun areItemsTheSame(other: Any?): Boolean {
        return other is FakeDataItem && other.id == id
    }

    override fun areContentsTheSame(other: Any?): Boolean {
        return other is FakeDataItem && other.number == number
    }

}

class FakeDataItemType() : BaseItemType<FakeDataItem>()  {
    override fun isSameModule(item: BaseItem): Boolean {
        return item is FakeDataItem
    }

    companion object {
        const val VIEW_TYPE = 1
    }

    override fun getViewType(): Int = VIEW_TYPE

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericListAdapter.BaseViewHolder<FakeDataItem> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fake, parent, false)
        return FakeDataItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: GenericListAdapter.BaseViewHolder<FakeDataItem>,
        item: FakeDataItem
    ) {
        holder.onBind(item)
    }
}

class FakeDataItemViewHolder(view: View): GenericListAdapter.BaseViewHolder<FakeDataItem>(view) {
    override fun onBind(item: FakeDataItem) {
        super.onBind(item)
        itemView.tvTitle.text = item.id
        itemView.setOnClickListener {
            Toast.makeText(itemView.context, "item ${item.id}", Toast.LENGTH_SHORT).show()
        }
    }
}