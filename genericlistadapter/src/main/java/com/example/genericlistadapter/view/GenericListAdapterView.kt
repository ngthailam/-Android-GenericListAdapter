package com.example.genericlistadapter.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.genericlistadapter.R
import com.example.genericlistadapter.adapter.GenericListAdapter
import com.example.genericlistadapter.utils.LIB_TAG
import com.example.genericlistadapter.utils.RecyclerViewEndlessScrollListener
import com.example.genericlistadapter.utils.SkeletonOptions
import io.supercharge.shimmerlayout.ShimmerLayout
import kotlinx.android.synthetic.main.gla_recycler_view.view.*

/**
 *
 */
class GenericListAdapterView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShimmerLayout(context, attributeSet, defStyleAttr), GenericListAdapterViewManager {

    private var endlessScrollListener: RecyclerViewEndlessScrollListener? = null
    private var internalRecyclerView: RecyclerView? = null

    /**
     * Main function to invoke to initialize view
     * @param adapter Adapter that will be attached to display to the RecyclerView
     */
    override fun initialize(adapter: GenericListAdapter): GenericListAdapterView {
        // set up recycler view
        initRecyclerView()
        // set adapter to recycler view
        internalRecyclerView?.adapter = adapter
        return this
    }

    /**
     * Add load more scroll listener, invoke when scroll to bottom
     * see [RecyclerViewEndlessScrollListener] for details
     * @param onLoadMore function to invoke on scroll to bottom to load more
     */
    override fun addLinearLoadMoreListener(onLoadMore: () -> Unit): GenericListAdapterView {
        // Check to ensure layout manager is LinearLayoutManager
        require(internalRecyclerView?.layoutManager is LinearLayoutManager) {
            "TransactionHistoryRecyclerView#layoutManager must be LinearLayoutManager, remember to call #setRecyclerViewOptions first"
        }

        // Lazy init listener and attach it
        (internalRecyclerView?.layoutManager as LinearLayoutManager).let { layoutManager ->
            // Lazy init
            if (endlessScrollListener == null) {
                endlessScrollListener = object : RecyclerViewEndlessScrollListener(layoutManager) {
                    override fun onLoadMore() {
                        onLoadMore.invoke()
                    }
                }
            }

            // Add listener to internal recycler view
            endlessScrollListener?.let { internalRecyclerView?.addOnScrollListener(it) }
        }

        return this
    }

    /**
     * Set options for recycler view including layoutManager, itemAnimator and itemDecoration
     * @param options see [RecyclerViewOptions]
     */
    override fun setRecyclerViewOptions(options: RecyclerViewOptions?): GenericListAdapterView {
        internalRecyclerView?.apply {
            layoutManager = options?.layoutManager
            itemAnimator = options?.itemAnimator
            overScrollMode = View.OVER_SCROLL_NEVER
            options?.itemDecoration?.let { addItemDecoration(it) }
        }
        return this
    }

    /** Returns internal recycler view */
    override fun getRecyclerView(): RecyclerView? {
        return try {
            internalRecyclerView
        } catch (t: Throwable) {
            Log.e(LIB_TAG, "GenericListAdapterView #getRecyclerView error")
            t.printStackTrace()
            null
        }
    }

    fun setSkeletonOptions(options: SkeletonOptions?) {
        options?.let {
            setShimmerAngle(it.angle)
            setShimmerColor(it.color)
            setShimmerAnimationDuration(it.duration)
        }
    }

    fun setIsLoading(isLoading: Boolean) {
        endlessScrollListener?.isLoading(isLoading)
    }

    fun getIsLoading(): Boolean = endlessScrollListener?.getIsLoading() ?: false

    /** Lazy initialze internal RecyclerView */
    private fun initRecyclerView() {
        if (internalRecyclerView != null) return
        // Inflate recycler view as direct child
        View.inflate(context, R.layout.gla_recycler_view, this)
        internalRecyclerView = glaInternalRv
    }
}
