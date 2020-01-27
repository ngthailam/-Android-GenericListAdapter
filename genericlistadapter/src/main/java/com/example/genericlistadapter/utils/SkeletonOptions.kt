package com.example.genericlistadapter.utils

import com.example.genericlistadapter.R

class SkeletonOptions(
    val itemCount: Int = DEFAULT_ITEM_COUNT,
    val color: Int = R.color.shimmer_color,
    val angle: Int = DEFAULT_ANGLE,
    val duration: Int = DEFAULT_DURATION
) {

    companion object {
        const val DEFAULT_ITEM_COUNT = 5
        const val DEFAULT_ANGLE = 20
        const val DEFAULT_DURATION = 1_000
    }
}
