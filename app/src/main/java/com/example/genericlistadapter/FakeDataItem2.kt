package com.example.genericlistadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.genericlistadapter.adapter.GenericListAdapter
import com.example.genericlistadapter.adapter.model.BaseItem
import com.example.genericlistadapter.adapter.model.BaseItemType
import kotlinx.android.synthetic.main.item_fake_2.view.tvTitle222

data class FakeDataItem2(
    val name: String,
    val email: String,
    val address: String
): BaseItem() {
    override fun areItemsTheSame(other: Any?): Boolean = other is FakeDataItem2

    override fun areContentsTheSame(other: Any?): Boolean {
        return other is FakeDataItem2 && this == other
    }
}

class FakeDataItem2Type: BaseItemType<FakeDataItem2>() {

    companion object {
        const val VIEW_TYPE = 12
    }

    override fun getViewType(): Int = VIEW_TYPE

    override fun isSameModule(item: BaseItem): Boolean = item is FakeDataItem2

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericListAdapter.BaseViewHolder<FakeDataItem2> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fake_2, parent, false)
        return FakeDataItem2ViewHolder(view)
    }
}

class FakeDataItem2ViewHolder(view: View): GenericListAdapter.BaseViewHolder<FakeDataItem2>(view){
    override fun onBind(item: FakeDataItem2) {
        super.onBind(item)
        itemView.tvTitle222.text = String.format("%s|%s|%s", item.email, item.address, item.name)
        itemView.setOnClickListener {
            Toast.makeText(itemView.context, "Clicked item 2 ${item.name}", Toast.LENGTH_SHORT).show()
        }
    }
}
