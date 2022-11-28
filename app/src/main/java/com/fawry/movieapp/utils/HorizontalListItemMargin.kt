package com.fawry.movieapp.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class HorizontalListItemMargin(
    @IntRange(from = 0) private val top: Int = 0,
    @IntRange(from = 0) private val bottom: Int = 0,
    @IntRange(from = 0) private val left: Int = 0,
    @IntRange(from = 0) private val right: Int = 0,
    private val isRTL: Boolean = false,
) :
    ItemDecoration() {

    constructor(
        @IntRange(from = 0) horizontalMargin: Int = 0
    ) : this(0, 0, horizontalMargin, horizontalMargin)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val itemPosition = parent.getChildLayoutPosition(view)

        // add top & bottom margin to all items
        outRect.top = top
        outRect.bottom = bottom

        if (isRTL) { // ex: Arabic Language
            // add left margin to all items
            outRect.left = left
            // only add right margin to the first item
            if (itemPosition == 0) {
                outRect.right = right
            }
        } else { // ex: English Language
            // add right margin to all items
            outRect.right = right
            // only add left margin to the first item
            if (itemPosition == 0) {
                outRect.left = left
            }
        }

    }
}