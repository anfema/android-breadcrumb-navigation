package com.anfema.breadcrumpnavigation.scrolling

import android.os.Bundle
import android.view.View
import com.anfema.breadcrumpnavigation.BreadcrumbActivity

abstract class BreadcrumbScrollActivity : BreadcrumbActivity(), OnBreadcrumbExpandedListener
{
    protected lateinit var breadcrumbScrollHelper: BreadcrumbScrollActivityHelper<BreadcrumbScrollActivity>

    abstract val expandIconView: View?

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        breadcrumbScrollHelper = BreadcrumbScrollActivityHelper(this, this, expandIconView)
        breadcrumbScrollHelper.onCreate()
    }

    override fun onStart()
    {
        super.onStart()
        breadcrumbScrollHelper.onStart()
    }
}