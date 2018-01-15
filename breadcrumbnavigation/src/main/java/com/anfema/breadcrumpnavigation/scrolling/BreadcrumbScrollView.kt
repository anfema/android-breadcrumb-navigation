package com.anfema.breadcrumb.scrolling

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import com.anfema.breadcrumpnavigation.BreadcrumbNavigation
import com.anfema.breadcrumpnavigation.scrolling.OnBreadcrumbExpandedListener
import kotlin.math.max

class BreadcrumbScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : NestedScrollView(context, attrs, defStyle), OnBreadcrumbActiveListener
{
    init
    {
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    val breadcrumbHeight by lazy {
        BreadcrumbInject.breadcrumbHeight
    }

    private val initialScroll: Int
        get()
        {
            return max(0, breadcrumbHeight * breadcrumbTitles.size)
        }

    val contentView by lazy { getChildAt(0) as ViewGroup }

    var breadcrumbTitles: List<String> = emptyList()
        set(value)
        {
            field = value
            value.reversed().forEachIndexed { index, title ->
                val bcv = BreadcrumbInject.breadcrumbView.invoke(context, this)
                bcv.setText(title)
                contentView.addView(bcv.view, 0)
                val lp = bcv.view.layoutParams
                lp.height = breadcrumbHeight
                bcv.view.layoutParams = lp
                bcv.view.setOnClickListener {
                    if (breadcrumbsExpanded)
                    {
                        bcv.onActive()
                        breadcrumbNavigation?.goBackMultipleSteps(value.size - index)
                    }
                }
            }
            jumpToInitialScrollState()
        }

    var onBreadcrumbExpandedListener: OnBreadcrumbExpandedListener? = null
    var onBreadcrumbSelectedListener: OnBreadcrumbSelectedListener? = null
    var breadcrumbNavigation: BreadcrumbNavigation? = null

    interface OnBreadcrumbSelectedListener
    {
        fun onBreadcrumbSelected(position: Int)
    }

    private fun isBreadcrumbsVisible() = scrollY < initialScroll

    var breadcrumbsExpanded = false
    private var isAnimating = false
    fun animateToInitialScrollState(interpolator: Interpolator)
    {
        if (!isAnimating && !breadcrumbsExpanded)
        {
            // stop fling
            fling(0)

            isAnimating = true

            val animator = ObjectAnimator.ofInt(this, "scrollY", initialScroll)
                    .setDuration(300)

            animator.interpolator = interpolator

            animator.addListener(object : Animator.AnimatorListener
            {
                override fun onAnimationEnd(p0: Animator?)
                {
                    isAnimating = false
                }

                override fun onAnimationCancel(p0: Animator?)
                {
                    isAnimating = false
                }

                override fun onAnimationStart(p0: Animator?) = Unit
                override fun onAnimationRepeat(p0: Animator?) = Unit
            })
            animator.start()
        }
    }

    fun jumpToInitialScrollState()
    {
        post { scrollTo(0, initialScroll) }
    }

    fun expandBreadcrumbs()
    {
        post { scrollTo(0, 0) }
    }

    private var selectedBreadcrumbPosition = -1
    private var selectedBreadcrumbActive = false
    override fun onScrollChanged(x: Int, y: Int, oldX: Int, oldY: Int)
    {
        super.onScrollChanged(x, y, oldX, oldY)

        if (!isTouchActive && isBreadcrumbsVisible())
        {
            // bounce back to initial scroll state (hide breadcrumbs)
            postDelayed({ animateToInitialScrollState(AccelerateDecelerateInterpolator()) }, 100)
        }

        if (!breadcrumbsExpanded)
        {
            updateBreadcrumbSelectionState(y)
        }

        if (isTouchActive && breadcrumbsExpanded)
        {
            breadcrumbsExpanded = false
            onBreadcrumbExpandedListener?.onBreadcrumbCollapsed()
        }
    }

    private fun updateBreadcrumbSelectionState(scrollY: Int)
    {
        val newBreadcrumpSelected = (scrollY + breadcrumbHeight / 2) / breadcrumbHeight
        if (newBreadcrumpSelected != selectedBreadcrumbPosition)
        {
            if (positionedAtBreadcrumb(newBreadcrumpSelected))
            {
                getBreadcrumbViewAt(selectedBreadcrumbPosition)?.onUnselect()
                getBreadcrumbViewAt(newBreadcrumpSelected)?.onFirstSelect()
                selectedBreadcrumbActive = false
            }
            else
            {
                getBreadcrumbViewAt(selectedBreadcrumbPosition)?.onUnselect()
            }
            selectedBreadcrumbPosition = newBreadcrumpSelected
        }
    }

    private fun getBreadcrumbViewAt(position: Int): Breadcrumb?
    {
        return contentView.getChildAt(position) as? Breadcrumb
    }

    var isTouchActive = false
    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        val onTouchEvent = super.onTouchEvent(event)

        if (event.actionMasked == MotionEvent.ACTION_DOWN)
        {
            isTouchActive = true
        }

        if (event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_CANCEL)
        {
            isTouchActive = false

            if (isBreadcrumbsVisible())
            {
                animateToInitialScrollState(DecelerateInterpolator())
            }

            if (positionedAtBreadcrumb(selectedBreadcrumbPosition) && selectedBreadcrumbActive)
            {
                onBreadcrumbSelectedListener?.onBreadcrumbSelected(selectedBreadcrumbPosition)
            }
        }

        return onTouchEvent
    }

    private fun positionedAtBreadcrumb(breadcrumbPosition: Int) = breadcrumbPosition >= 0 && breadcrumbPosition < breadcrumbTitles.size

    override fun onSelectedBreadcrumbActive()
    {
        selectedBreadcrumbActive = true
    }
}
