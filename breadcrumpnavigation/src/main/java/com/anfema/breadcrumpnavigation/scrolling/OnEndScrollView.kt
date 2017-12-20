package com.anfema.breadcrumb.scrolling

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.max

open class OnEndScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : NestedScrollView(context, attrs, defStyle)
{
    init
    {
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    var onEndScrollListener: OnEndScrollListener? = null

    interface OnEndScrollListener
    {
        fun onEndScroll()
    }

    private var isFling: Boolean = false

    override fun fling(velocityY: Int)
    {
        isFling = true
        super.fling(velocityY)
    }

    override fun onScrollChanged(x: Int, y: Int, oldX: Int, oldY: Int)
    {
        super.onScrollChanged(x, y, oldX, oldY)
        Log.d("OnEndScrollView", "y: $y, oldY: $oldY, measuredHeight: $measuredHeight, height: $height, fling == $isFling")
        if (isFling)
        {
            val isAboutToStop = Math.abs(y - oldY) < 2
            val reachedTop = y == 0
            val reachedBottom = y >= max(measuredHeight, getChildAt(0).measuredHeight - measuredHeight)
            if (isAboutToStop || reachedTop || reachedBottom)
            {
                onEndScrollListener?.onEndScroll()
                Log.d("OnEndScrollView", "on end scroll")
                isFling = false
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        val onTouchEvent = super.onTouchEvent(event)

//        if (event.actionMasked == MotionEvent.ACTION_UP)
//        {
//            performClick()
//        }

        if (!isFling && (event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_CANCEL))
        {
            onEndScrollListener?.onEndScroll()
        }

        return onTouchEvent
    }
}
