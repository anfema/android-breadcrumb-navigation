package com.anfema.breadcrumpnavigation.scrolling

import android.view.View
import android.view.ViewGroup
import com.anfema.breadcrumpnavigation.BreadcrumbActivity

abstract class BreadcrumbScrollActivity : BreadcrumbActivity(), OnBreadcrumbExpandedListener
{
    abstract val expandIconView: View?

    override fun onStart() {
        super.onStart()
        setupScrollView()
    }

    private fun setupScrollView(){
        val scrollView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as? BreadcrumbScrollView
        val breadcrumbTrail = breadcrumbHelper.getBreadcrumbTrail(intent)
        scrollView?.setup(breadcrumbHelper, breadcrumbTrail, expandIconView, expandedListener = this)
    }
}