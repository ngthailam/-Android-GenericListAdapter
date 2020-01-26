package com.example.genericlistadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.genericlistadapter.adapter.GenericListAdapter
import com.example.genericlistadapter.view.RecyclerViewOptionsAnimatedAndMargin
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: GenericListAdapter
    private var lastIndex = -1
    val fakeList = mutableListOf<FakeDataItem>()

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
            .addLoadMoreItem()
            .build()
        // Init view
        glaView.initialize(adapter)
            .setRecyclerViewOptions(RecyclerViewOptionsAnimatedAndMargin(this))
            .addLinearLoadMoreListener {
                println("ZZLL load more invoked....")
                adapter.onLoadMore()
                setDataWithDelay(3, 10)
            }
        // Set data to list
        setDataWithDelay(0, 10)

    }

    private fun setDataWithDelay(delaySeconds: Int = 0, numFakeData: Int = 5) {
        Handler().postDelayed({
            adapter.setData(generateFakeData(numFakeData))
        }, delaySeconds * 1000L)
    }

    private fun  generateFakeData(num: Int = 1): List<FakeDataItem> {
        for (i in lastIndex until lastIndex + num) {
            fakeList.add(FakeDataItem("${lastIndex + i}", lastIndex + i))
        }
        lastIndex = num - 1
        return fakeList
    }
}
