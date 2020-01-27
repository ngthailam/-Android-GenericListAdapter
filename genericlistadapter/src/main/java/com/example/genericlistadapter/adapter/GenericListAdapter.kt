package com.example.genericlistadapter.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.genericlistadapter.adapter.model.BaseItem
import com.example.genericlistadapter.adapter.model.BaseItemType
import com.example.genericlistadapter.adapter.model.HeaderItem
import com.example.genericlistadapter.adapter.model.HeaderItemType
import com.example.genericlistadapter.adapter.model.LoadMoreItem
import com.example.genericlistadapter.adapter.model.LoadMoreItemType
import com.example.genericlistadapter.adapter.model.SkeletonItem
import com.example.genericlistadapter.adapter.model.SkeletonItemType
import com.example.genericlistadapter.utils.LIB_TAG
import com.example.genericlistadapter.utils.SkeletonOptions
import com.example.genericlistadapter.utils.VIEW_TYPE_HEADER
import com.example.genericlistadapter.utils.VIEW_TYPE_LOADMORE
import com.example.genericlistadapter.utils.VIEW_TYPE_SKELETON
import com.example.genericlistadapter.utils.animation.DefaultEnterAnimType
import com.example.genericlistadapter.utils.animation.EnterAnimType
import com.example.genericlistadapter.view.GenericListAdapterView
import java.lang.IllegalArgumentException
import java.util.concurrent.atomic.AtomicBoolean

class GenericListAdapter private constructor() :
    ListAdapter<BaseItem, GenericListAdapter.BaseViewHolder<out BaseItem>>(
        DiffUtilCallback()
    ) {

    companion object {
        const val DEFAULT_LAST_ANIMATED_POSITION = -1
    }

    /** A hashmap containing all view types */
    private var hashMap: HashMap<Int, BaseItemType<BaseItem>> = hashMapOf()
    private var headerItemType: HeaderItemType? = null
    private var loadMoreItemType: LoadMoreItemType? = null
    private var skeletonItemType: SkeletonItemType? = null
    private var skeletonOptions: SkeletonOptions? = null

    /** State management */
    private var isLoadingMore: AtomicBoolean = AtomicBoolean(false)
    private var isRefreshing: AtomicBoolean = AtomicBoolean(false)

    /** Helper animations */
    // helper for making animation for on scroll to item ( trigger once per item per refresh )
    private var lastAnimatedPosition = DEFAULT_LAST_ANIMATED_POSITION
    private var animationType: EnterAnimType? = null

    private var adapterView: GenericListAdapterView? = null

    private constructor(builder: Builder) : this() {
        this.hashMap = builder.hashMap
        this.headerItemType = builder.headerItemType
        this.loadMoreItemType = builder.loadMoreItemType
        this.skeletonItemType = builder.skeletonItemType
        this.animationType = builder.animationType
        builder.adapterView?.let {
            this.adapterView = it.apply { setSkeletonOptions(builder.skeletonOptions) }
            this.skeletonOptions = builder.skeletonOptions
        }
    }

    /** Use in place of ListAdapter`s submit list with custom behavior */
    fun setData(data: List<BaseItem>?) {
        if (data.isNullOrEmpty()) {
            resetState()
            submitList(null)
            return
        }

        val tempList = data.toMutableList()
        if (hasHeader()) tempList.add(0, HeaderItem())
        resetState()
        submitList(tempList)
    }

    /** Call when load more is invoked to add load more indicator to bottom of the list */
    fun onLoadMore() {
        // Fast quit if already loading more
        if (isLoadingMore.get() || isRefreshing.get() || itemCount == 0) return
        // Add load more item
        if (isLoadingMore.compareAndSet(false, true)) {
            val tempList = currentList.toMutableList()
            tempList.add(LoadMoreItem())
            submitList(tempList)
        }
    }

    /** Call when you refresh the list */
    fun onRefresh() {
        // Show skeleton if not already refreshing
        if (isRefreshing.compareAndSet(false, true)) {
            showSkeleton()
            lastAnimatedPosition = DEFAULT_LAST_ANIMATED_POSITION
        }
    }

    fun getIsLoadingMore(): Boolean = isLoadingMore.get()

    private fun showSkeleton() {
        adapterView?.startShimmerAnimation()
        submitList(
            List(skeletonOptions?.itemCount ?: 0) {
                SkeletonItem()
            }
        )
    }

    private fun resetState() {
        isLoadingMore.set(false)
        isRefreshing.set(false)
        adapterView?.stopShimmerAnimation()
    }

    override fun getItemViewType(position: Int): Int {
        when (val item = getItem(position)) {
            is HeaderItem -> return VIEW_TYPE_HEADER
            is LoadMoreItem -> return VIEW_TYPE_LOADMORE
            is SkeletonItem -> return VIEW_TYPE_SKELETON
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out BaseItem> {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                headerItemType?.onCreateViewHolder(parent, viewType)
                    ?: throw Throwable("GenericListAdapter #onCreateViewHolder Builder headerItemType is null")
            }
            VIEW_TYPE_LOADMORE -> {
                loadMoreItemType?.onCreateViewHolder(parent, viewType)
                    ?: throw Throwable("GenericListAdapter #onCreateViewHolder Builder loadMoreItemType is null")
            }
            VIEW_TYPE_SKELETON -> {
                skeletonItemType?.onCreateViewHolder(parent, viewType)
                    ?: throw Throwable("GenericListAdapter #onCreateViewHolder Builder skeletonItemType is null")
            }
            else -> {
                hashMap[viewType]?.onCreateViewHolder(parent, viewType)
                    ?: throw Throwable("GenericListAdapter #onCreateViewHolder return null with parent ${parent::class.java.simpleName}, viewType $viewType ")
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<out BaseItem>, position: Int) {
        hashMap.keys.forEach loop@{ key ->
            val item = getItem(position)
            hashMap[key]?.let {
                if (it.isSameModule(item)) {
                    it.onBindViewHolder(holder as BaseViewHolder<BaseItem>, item)
                    setItemEnterAnimation(holder.itemView, position)
                    return@loop
                }
            }
        }
    }

    // //////////////////////////
    //          HELPER FUNCTIONS
    // //////////////////////////
    private fun hasHeader(): Boolean = this.headerItemType != null

    /**
     * set the enter animation for item, only items that are newly added below will have this animation ( through #lastPosition )
     * @param viewToAnimate: The view to be animated
     * @param position: position of item in list
     */
    private fun setItemEnterAnimation(viewToAnimate: View, position: Int) {
        if (position > lastAnimatedPosition) {
            try {
                val animation = AnimationUtils.loadAnimation(viewToAnimate.context, animationType?.animRes!!)
                viewToAnimate.startAnimation(animation)
            } catch (t: Throwable) {
                Log.e(LIB_TAG, "GenericListAdapter #setItemEnterAnimation error")
                t.printStackTrace()
            }
            lastAnimatedPosition = position
        }
    }

    abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
        open fun onBind(item: T) {
            // Implement here
        }
    }

    abstract class EmptyViewHolder(view: View) : BaseViewHolder<Any>(view)

    // //////////////////////////
    //          BUILDER FOR ADAPTER
    // //////////////////////////
    class Builder {

        internal val hashMap: HashMap<Int, BaseItemType<BaseItem>> = hashMapOf()
        internal var headerItemType: HeaderItemType? = null
        internal var loadMoreItemType: LoadMoreItemType? = null
        internal var skeletonItemType: SkeletonItemType? = null
        internal var animationType: EnterAnimType? = null
        internal var skeletonOptions: SkeletonOptions? = null
        internal var adapterView: GenericListAdapterView? = null

        fun addItemModule(viewType: Int, abc: BaseItemType<out BaseItem>): Builder {
            hashMap[viewType] = abc as? BaseItemType<BaseItem>
                ?: throw Throwable("GenericListAdapter #Builder #addItemModule ItemType must be of type BaseItemType<T: BaseItemType>")

            return this
        }

        fun addHeaderItem(headerItemType: HeaderItemType? = null): Builder {
            this.headerItemType = headerItemType ?: HeaderItemType()
            return this
        }

        fun addLoadMoreItem(loadMoreItemType: LoadMoreItemType? = null): Builder {
            this.loadMoreItemType = loadMoreItemType ?: LoadMoreItemType()
            return this
        }

        fun addSkeletonItem(skeletonItemType: SkeletonItemType? = null): Builder {
            this.skeletonItemType = skeletonItemType ?: SkeletonItemType()
            return this
        }

        fun addItemAnimation(animationType: EnterAnimType? = null): Builder {
            this.animationType = animationType ?: DefaultEnterAnimType()
            return this
        }

        fun setSkeletonOptions(skeletonOptions: SkeletonOptions? = null): Builder {
            this.skeletonOptions = skeletonOptions ?: SkeletonOptions()
            return this
        }

        fun attachTo(attachedView: GenericListAdapterView): Builder {
            adapterView = attachedView
            return this
        }

        fun setDefault(): Builder {
            addHeaderItem()
            addLoadMoreItem()
            addSkeletonItem()
            addItemAnimation()
            setSkeletonOptions()
            return this
        }

        fun build(): GenericListAdapter {
            return GenericListAdapter(this)
        }
    }
}
