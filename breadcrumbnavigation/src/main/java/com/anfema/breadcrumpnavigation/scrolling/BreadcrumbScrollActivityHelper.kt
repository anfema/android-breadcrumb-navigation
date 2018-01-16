package com.anfema.breadcrumpnavigation.scrolling

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.anfema.breadcrumpnavigation.BreadcrumbActivityHelper
import com.anfema.breadcrumpnavigation.BreadcrumbComponent

open class BreadcrumbScrollActivityHelper<out T>(val activity: T, val expandedListener: OnBreadcrumbExpandedListener?, val expandIconView: View?)
    : BreadcrumbActivityHelper(activity) where T : Activity, T : BreadcrumbComponent
{
    private var breadcrumbsAdded = false
    private var scrolledToInitialState = false

    fun onCreate()
    {
        breadcrumbsAdded = false
        scrolledToInitialState = false
    }

    fun onStart()
    {
        if (!breadcrumbsAdded)
        {
            val scrollView = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as? BreadcrumbScrollView

            if (scrollView != null)
            {
                scrollView.breadcrumbTitles = getBreadcrumbTrail(activity.intent)
                breadcrumbsAdded = true

                scrollView.breadcrumbNavigation = this

                emulateScrollViewFillViewport(scrollView)

                expandIconView?.let {
                    scrollView.onBreadcrumbExpandedListener = expandedListener
                    it.setOnClickListener {
                        if (scrollView.breadcrumbsExpanded)
                        {
                            collapseBreadcrumbs(scrollView)
                        }
                        else
                        {
                            expandBreadcrumbs(scrollView)
                        }
                    }
                }

                // listen to breadcrumbTitles selected
                scrollView.onBreadcrumbSelectedListener = object : BreadcrumbScrollView.OnBreadcrumbSelectedListener
                {
                    override fun onBreadcrumbSelected(position: Int)
                    {
                        goBackMultipleSteps(scrollView.breadcrumbTitles.size - position)
                    }
                }
            }
        }
    }

    fun expandBreadcrumbs(scrollView: BreadcrumbScrollView)
    {
        // expand breadcrumbs
        scrollView.breadcrumbsExpanded = true
        scrollView.expandBreadcrumbs()
        expandedListener?.onBreadcrumbExpanded()
    }

    fun collapseBreadcrumbs(scrollView: BreadcrumbScrollView)
    {
        // collapse breadcrumbs
        scrollView.breadcrumbsExpanded = false
        scrollView.animateToInitialScrollState(DecelerateInterpolator())
        expandedListener?.onBreadcrumbCollapsed()
    }

    private fun emulateScrollViewFillViewport(scrollView: BreadcrumbScrollView)
    {
        // disable built-in fill viewport calculation and replace with custom one
        scrollView.isFillViewport = false

        // custom implementation of fill viewport which adds enough bottom padding to always hide the breadcrumb trail initially by scrolling down.
        // Therefore, filling the viewport is mandatory.
        val contentView = scrollView.contentView
        contentView.viewTreeObserver.addOnGlobalLayoutListener {
            val contentLp = contentView.layoutParams as FrameLayout.LayoutParams
            val contentViewTotalHeight = contentView.height + contentLp.topMargin + contentLp.bottomMargin
            val breadcrumbsOverscrollHeight = scrollView.breadcrumbTitles.size * scrollView.breadcrumbHeight
            val spaceToFill = scrollView.height - (contentViewTotalHeight - breadcrumbsOverscrollHeight)
            if (spaceToFill > 0)
            {
                contentView.setPadding(contentView.paddingLeft, contentView.paddingTop, contentView.paddingRight, contentView.paddingBottom + spaceToFill)
                if (!scrolledToInitialState)
                {
                    scrollView.jumpToInitialScrollState()
                    scrolledToInitialState = true
                }
            }
        }
    }
}