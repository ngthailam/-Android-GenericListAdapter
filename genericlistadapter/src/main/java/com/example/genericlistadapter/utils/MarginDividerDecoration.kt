package com.example.genericlistadapter.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class MarginDividerDecoration(context: Context, @DimenRes marginRes: Int) :
    RecyclerView.ItemDecoration() {

    private val margin by lazy {
        context.resources.getDimensionPixelSize(marginRes)
    }
    private var mDivider: Drawable? = null

    private val mBounds = Rect()

    fun setDrawable(drawable: Drawable) {
        mDivider = drawable
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || mDivider == null) {
            return
        }
        canvas.save()
        val left: Int = margin
        val right: Int = parent.width - margin

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom = mBounds.bottom + Math.round(child.translationY)
            val top = bottom - mDivider!!.intrinsicHeight
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0)
            return
        }
        outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
    }
}