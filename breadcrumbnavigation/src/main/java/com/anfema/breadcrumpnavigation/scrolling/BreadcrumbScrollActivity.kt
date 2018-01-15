package com.anfema.breadcrumb.scrolling

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.anfema.breadcrumpnavigation.BreadcrumbActivity
import com.anfema.breadcrumpnavigation.scrolling.OnBreadcrumbExpandedListener

abstract class BreadcrumbScrollActivity : BreadcrumbActivity(), OnBreadcrumbExpandedListener
{
    private var breadcrumbsAdded = false
    private var scrolledToInitialState = false

    abstract val expandIconView: View?

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        breadcrumbsAdded = false
        scrolledToInitialState = false
    }

    override fun onStart()
    {
        super.onStart()
        if (!breadcrumbsAdded)
        {
            val scrollView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as BreadcrumbScrollView
            scrollView.breadcrumbTitles = breadcrumbHelper.getBreadcrumbTrail(intent)
            breadcrumbsAdded = true

            scrollView.breadcrumbNavigation = this

            emulateScrollViewFillViewport(scrollView)

            expandIconView?.let {
                scrollView.onBreadcrumbExpandedListener = this
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
                    breadcrumbHelper.goBackMultipleSteps(scrollView.breadcrumbTitles.size - position)
                }
            }
        }
    }

    protected fun expandBreadcrumbs(scrollView: BreadcrumbScrollView)
    {
        // expand breadcrumbs
        scrollView.breadcrumbsExpanded = true
        scrollView.expandBreadcrumbs()
        onBreadcrumbExpanded()
    }

    protected fun collapseBreadcrumbs(scrollView: BreadcrumbScrollView)
    {
        // collapse breadcrumbs
        scrollView.breadcrumbsExpanded = false
        scrollView.animateToInitialScrollState(DecelerateInterpolator())
        onBreadcrumbCollapsed()
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