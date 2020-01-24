package com.example.genericlistadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.genericlistadapter.adapter.GenericListAdapter
import com.example.genericlistadapter.view.RecyclerViewOptionsAnimatedAndMargin
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: GenericListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //
        setupUI()
    }

    private fun setupUI() {
        // Init Adapter
        adapter = GenericListAdapter.Builder()
            .addHeaderItem()
            .addItemModule(FakeDataItemType.VIEW_TYPE, FakeDataItemType())
            .build()
        // Init view
        glaView.initialize(adapter)
            .setRecyclerViewOptions(RecyclerViewOptionsAnimatedAndMargin(this))
        // Set data to list
        adapter.setData(
            listOf(
                FakeDataItem("1", 1),
                FakeDataItem("2", 2),
                FakeDataItem("3", 3)
            )
        )
    }
}
