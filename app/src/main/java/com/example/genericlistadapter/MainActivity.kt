package com.example.genericlistadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.genericlistadapter.adapter.GenericListAdapter
import com.example.genericlistadapter.view.RecyclerViewOptionsAnimatedAndMargin
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: GenericListAdapter
    private var lastIndex = 0
    private var fakeList = mutableListOf<FakeDataItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //
        setupUI()
        setOnClicks()
    }

    private fun setOnClicks() {
        btnAdd.setOnClickListener {
            addItem()
        }
        btnRemove.setOnClickListener {

        }
        btnClear.setOnClickListener {
            adapter.setData(null)
        }
        btnRefresh.setOnClickListener {
            fakeList = getInitialList().toMutableList()
            adapter.setData(fakeList)
        }
    }

    private fun setupUI() {
        // Init Adapter
        adapter = GenericListAdapter.Builder()
            .attachTo(glaView)
            .addHeaderItem()
            .addItemModule(FakeDataItemType.VIEW_TYPE, FakeDataItemType())
            .addLoadMoreItem()
            .addSkeletonItem(CustomSkeletonItem())
            .addItemAnimation()
            .setSkeletonOptions()
            .build()
        // Init view
        glaView.initialize(adapter)
            .setRecyclerViewOptions(RecyclerViewOptionsAnimatedAndMargin(this))
            .addLinearLoadMoreListener {
                if (adapter.getIsLoadingMore()) return@addLinearLoadMoreListener
                adapter.onLoadMore()
                setDataWithDelay(3)
            }
        // Set initial state
        adapter.onRefresh()
        // Set data to list
        fakeList.addAll(getInitialList())
        adapter.setData(fakeList)
    }

    private fun getInitialList(): List<FakeDataItem> {
        lastIndex = 6
        return listOf(
            FakeDataItem("1", 1),
            FakeDataItem("2", 2),
            FakeDataItem("3", 3),
            FakeDataItem("4", 4),
            FakeDataItem("5", 5)
        )
    }

    private fun addItem() {
        fakeList.add(
            FakeDataItem(lastIndex.toString(), lastIndex)
        )
        lastIndex++
        adapter.setData(fakeList)
    }

    private fun setDataWithDelay(delaySeconds: Int = 0, numFakeData: Int = 5) {
        Handler().postDelayed({
            adapter.setData(generateFakeData(numFakeData))
        }, delaySeconds * 1000L)
    }

    private fun  generateFakeData(num: Int = 1): List<FakeDataItem> {
        for (i in lastIndex until lastIndex + num) {
            println("ZZLL i $i")
            fakeList.add(FakeDataItem("$i", i))
        }
        lastIndex += num
        println("ZZLL gen $lastIndex")
        return fakeList
    }
}
