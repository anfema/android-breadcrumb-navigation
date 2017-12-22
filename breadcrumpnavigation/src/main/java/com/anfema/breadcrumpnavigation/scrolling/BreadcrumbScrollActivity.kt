package com.anfema.breadcrumb.scrolling

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.anfema.breadcrumpnavigation.BreadcrumbActivity

abstract class BreadcrumbScrollActivity : BreadcrumbActivity()
{
    private var breadcrumbsAdded = false
    private var scrolledToInitialState = false

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

            // add bottom padding to ensure the scroll view can be scrolled far to the correct position
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
}