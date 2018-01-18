package com.anfema.breadcrumpnavigation.scrolling

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
import android.widget.FrameLayout
import com.anfema.breadcrumpnavigation.BreadcrumbNavigation
import kotlin.math.max

class BreadcrumbScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : NestedScrollView(context, attrs, defStyle), OnBreadcrumbActiveListener {
    val breadcrumbHeight by lazy {
        BreadcrumbInject.breadcrumbHeight
    }

    val contentView by lazy { getChildAt(0) as ViewGroup }

    private var breadcrumbTitles: List<String> = emptyList()
        set(value) {
            field = value
            value.reversed().forEachIndexed { index, title ->
                val bcv = BreadcrumbInject.breadcrumbView.invoke(context, this)
                bcv.setText(title)
                contentView.addView(bcv.view, 0)
                val lp = bcv.view.layoutParams
                lp.height = breadcrumbHeight
                bcv.view.layoutParams = lp
                bcv.view.setOnClickListener {
                    if (breadcrumbsExpanded) {
                        bcv.onActive()
                        breadcrumbNavigation?.goBackMultipleSteps(value.size - index)
                    }
                }
            }
            jumpToInitialScrollState()
        }

    private val initialScroll: Int
        get() {
            return max(0, breadcrumbHeight * breadcrumbTitles.size)
        }

    init {
        overScrollMode = View.OVER_SCROLL_NEVER
        breadcrumbTitles = emptyList()
        isFillViewport = true
    }

    var onBreadcrumbExpandedListener: OnBreadcrumbExpandedListener? = null
    var onBreadcrumbSelectedListener: OnBreadcrumbSelectedListener? = null
    var breadcrumbNavigation: BreadcrumbNavigation? = null

    interface OnBreadcrumbSelectedListener {
        fun onBreadcrumbSelected(position: Int)
    }

    fun setup(breadcrumbNavigation: BreadcrumbNavigation?, breadcrumbTrail: List<String>, expandIconView: View?, expandedListener: OnBreadcrumbExpandedListener?) {
        if (breadcrumbTitles.isNotEmpty()) {
            // ensure setup method only is called only once
            return
        }

        breadcrumbTitles = breadcrumbTrail

        this.breadcrumbNavigation = breadcrumbNavigation

        expandIconView?.let {
            onBreadcrumbExpandedListener = expandedListener
            it.setOnClickListener {
                if (breadcrumbsExpanded) {
                    collapseBreadcrumbs()
                } else {
                    expandBreadcrumbs()
                }
            }
        }

        // listen to breadcrumbTitles selected
        onBreadcrumbSelectedListener = object : BreadcrumbScrollView.OnBreadcrumbSelectedListener {
            override fun onBreadcrumbSelected(position: Int) {
                breadcrumbNavigation?.let {
                    it.goBackMultipleSteps(breadcrumbTitles.size - position)
                    getBreadcrumbViewAt(position)?.onSelected()
                }
            }
        }
    }

    fun expandBreadcrumbs() {
        post { scrollTo(0, 0) }
        breadcrumbsExpanded = true
        onBreadcrumbExpandedListener?.onBreadcrumbExpanded()
    }

    fun collapseBreadcrumbs() {
        animateToInitialScrollState(DecelerateInterpolator())
        breadcrumbsExpanded = false
        onBreadcrumbExpandedListener?.onBreadcrumbCollapsed()
    }

    var breadcrumbsExpanded = false
    private var isAnimating = false
    fun animateToInitialScrollState(interpolator: Interpolator) {
        if (!isAnimating && !breadcrumbsExpanded) {
            // stop fling
            fling(0)

            isAnimating = true

            val animator = ObjectAnimator.ofInt(this, "scrollY", initialScroll)
                    .setDuration(300)

            animator.interpolator = interpolator

            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(p0: Animator?) {
                    isAnimating = false
                }

                override fun onAnimationCancel(p0: Animator?) {
                    isAnimating = false
                }

                override fun onAnimationStart(p0: Animator?) = Unit
                override fun onAnimationRepeat(p0: Animator?) = Unit
            })
            animator.start()
        }
    }

    fun jumpToInitialScrollState() {
        post { scrollTo(0, initialScroll) }
    }

    private fun isBreadcrumbsVisible() = scrollY < initialScroll

    companion object {
        const val NOT_SCROLLED_TO_BREADCRUMB = -1
    }

    private var selectedBreadcrumbPosition = NOT_SCROLLED_TO_BREADCRUMB
    private var selectedBreadcrumbActive = false
    override fun onScrollChanged(x: Int, y: Int, oldX: Int, oldY: Int) {
        super.onScrollChanged(x, y, oldX, oldY)

        if (!isTouchActive && isBreadcrumbsVisible()) {
            // bounce back to initial scroll state (hide breadcrumbs)
            postDelayed({ animateToInitialScrollState(AccelerateDecelerateInterpolator()) }, 100)
        }

        if (!breadcrumbsExpanded) {
            updateBreadcrumbSelectionState(y)
        }

        if (isTouchActive && breadcrumbsExpanded) {
            breadcrumbsExpanded = false
            onBreadcrumbExpandedListener?.onBreadcrumbCollapsed()
        }
    }

    private fun updateBreadcrumbSelectionState(scrollY: Int) {
        val newBreadcrumpSelected = (scrollY + breadcrumbHeight / 2) / breadcrumbHeight
        if (newBreadcrumpSelected != selectedBreadcrumbPosition) {
            if (positionedAtBreadcrumb(newBreadcrumpSelected)) {
                getBreadcrumbViewAt(selectedBreadcrumbPosition)?.onInactive()
                getBreadcrumbViewAt(newBreadcrumpSelected)?.onHover()
                selectedBreadcrumbPosition = newBreadcrumpSelected
                selectedBreadcrumbActive = false
            } else {
                getBreadcrumbViewAt(selectedBreadcrumbPosition)?.onInactive()
                selectedBreadcrumbPosition = NOT_SCROLLED_TO_BREADCRUMB
            }
        }
    }

    private fun getBreadcrumbViewAt(position: Int): Breadcrumb? {
        return contentView.getChildAt(position) as? Breadcrumb
    }

    var isTouchActive = false
        set(value) {
            field = value
            if (!value) {
                if (isBreadcrumbsVisible()) {
                    animateToInitialScrollState(DecelerateInterpolator())
                }

                if (positionedAtBreadcrumb(selectedBreadcrumbPosition) && selectedBreadcrumbActive) {
                    onBreadcrumbSelectedListener?.onBreadcrumbSelected(selectedBreadcrumbPosition)
                }
            }
        }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val onTouchEvent = super.onTouchEvent(event)

        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            isTouchActive = true
        }

        if (event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_CANCEL) {
            isTouchActive = false
        }

        return onTouchEvent
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        val onStartNestedScroll = super.onStartNestedScroll(child, target, nestedScrollAxes)
        isTouchActive = true
        return onStartNestedScroll
    }

    override fun onStopNestedScroll(target: View) {
        super.onStopNestedScroll(target)
        isTouchActive = false
    }

    private fun positionedAtBreadcrumb(breadcrumbPosition: Int) = breadcrumbPosition >= 0 && breadcrumbPosition < breadcrumbTitles.size

    override fun onSelectedBreadcrumbActive() {
        selectedBreadcrumbActive = true
    }

    /**
     * Overwrite fillViewport logic so enough space is filled up to be able to scroll to
     * "initialScroll" state in which the breadcrumbs are hidden
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (!isFillViewport) {
            return
        }

        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == View.MeasureSpec.UNSPECIFIED) {
            return
        }

        if (childCount > 0) {
            val child = getChildAt(0)
            var height = measuredHeight
            if (child.measuredHeight - initialScroll < height) {
                val lp = child.layoutParams as FrameLayout.LayoutParams

                val childWidthMeasureSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec,
                        paddingLeft + paddingRight, lp.width)
                height -= paddingTop
                height -= paddingBottom
                height += initialScroll
                val childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            }
        }
    }
}
