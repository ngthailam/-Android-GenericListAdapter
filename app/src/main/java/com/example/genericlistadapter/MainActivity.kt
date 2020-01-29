package com.example.genericlistadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.genericlistadapter.adapter.GenericListAdapter
import com.example.genericlistadapter.adapter.model.BaseItem
import com.example.genericlistadapter.view.RecyclerViewOptionsAnimatedAndMargin
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: GenericListAdapter
    private var lastIndex = 0
    private var fakeList = mutableListOf<BaseItem>()

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
            fakeList.removeAt(fakeList.size - 1)
            adapter.setData(fakeList)
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
            .addItemModule(FakeDataItem2Type.VIEW_TYPE, FakeDataItem2Type())
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

    private fun getInitialList(): List<BaseItem> {
        lastIndex = 6
        return listOf(
            FakeDataItem("1", 1),
            FakeDataItem2("2", "email1", "add1"),
            FakeDataItem("3", 3),
            FakeDataItem2("4", "email2", "add2"),
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

    private fun  generateFakeData(num: Int = 1): List<BaseItem> {
        for (i in lastIndex until lastIndex + num) {
            println("ZZLL i $i")
            fakeList.add(FakeDataItem("$i", i))
        }
        lastIndex += num
        println("ZZLL gen $lastIndex")
        return fakeList
    }
}
