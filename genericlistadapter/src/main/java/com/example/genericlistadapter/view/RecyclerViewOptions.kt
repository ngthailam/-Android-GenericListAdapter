package com.example.genericlistadapter.view

import android.content.Context
import android.util.Log
import android.view.View.OVER_SCROLL_ALWAYS
import android.view.View.OVER_SCROLL_NEVER
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.genericlistadapter.R
import com.example.genericlistadapter.utils.LIB_TAG
import com.example.genericlistadapter.utils.MarginDividerDecoration

abstract class RecyclerViewOptions {
    abstract val layoutManager: RecyclerView.LayoutManager?
    abstract val itemAnimator: RecyclerView.ItemAnimator?
    abstract val itemDecoration: RecyclerView.ItemDecoration?
    // Use View.OVER_SCROLL_ALWAYS and the likes
    abstract val overScrollMode: Int
}

class RecyclerViewOptionsNone(private val context: Context) : RecyclerViewOptions() {
    override val layoutManager: RecyclerView.LayoutManager? = LinearLayoutManager(context)
    override val itemAnimator: RecyclerView.ItemAnimator? = null
    override val itemDecoration: RecyclerView.ItemDecoration? = null
    override val overScrollMode: Int = OVER_SCROLL_ALWAYS
}

class RecyclerViewOptionsAnimatedAndMargin(private val context: Context) : RecyclerViewOptions() {
    override val layoutManager: RecyclerView.LayoutManager? = LinearLayoutManager(context)
    override val itemAnimator: RecyclerView.ItemAnimator?
        get() {
            return DefaultItemAnimator().apply {
                // Remove animation when item is cleared,
                // this will make transition to other layouts smoother
                removeDuration = 0L
            }
        }
    override val itemDecoration: RecyclerView.ItemDecoration?
        get() {
            return try {
                MarginDividerDecoration(context, R.dimen.gla_dp_16).apply {
                    setDrawable(
                        ContextCompat.getDrawable(context, R.drawable.gla_divider_item)!!
                    )
                }
            } catch (t: Throwable) {
                Log.e(LIB_TAG, "RecyclerViewOptionsDefault #itemDecoration error")
                t.printStackTrace()
                null
            }
        }

    override val overScrollMode: Int = OVER_SCROLL_NEVER
}
