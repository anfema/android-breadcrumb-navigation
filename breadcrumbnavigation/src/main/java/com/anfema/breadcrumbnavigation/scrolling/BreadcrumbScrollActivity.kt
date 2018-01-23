package com.anfema.breadcrumbnavigation.scrolling

import android.view.View
import android.view.ViewGroup
import com.anfema.breadcrumbnavigation.BreadcrumbActivity

/**
 * Inherit from this activity if you want to have breadcrumb views exposed at the top by pulling the
 * screen.
 *
 * In your activity you need to
 * - call [setupBreadcrumbScrollView] after [setContentView]
 * - make [BreadcrumbScrollView] the root element of your view
 *
 * In your custom application class you need to
 * - inject the breadcrumb Implementation and
 * - the height of the breadcrumb views.
 */
abstract class BreadcrumbScrollActivity : BreadcrumbActivity() {
    /**
     * This method needs to be called in onCreate() after setContentView()!
     */
    protected fun setupBreadcrumbScrollView(expandIconView: View?, onBreadcrumbExpandedListener: OnBreadcrumbExpandedListener?) {
        val scrollView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as? BreadcrumbScrollView
        val breadcrumbTrail = breadcrumbHelper.getBreadcrumbTrail(intent)
        scrollView?.setup(breadcrumbHelper, breadcrumbTrail, expandIconView, onBreadcrumbExpandedListener)
    }
}