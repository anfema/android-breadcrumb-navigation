package com.anfema.breadcrumbsample

import android.app.Application
import android.content.Context
import com.anfema.breadcrumbnavigation.scrolling.BreadcrumbInject
import com.anfema.breadcrumbnavigation.scrolling.OnBreadcrumbActiveListener

class BreadcrumbSampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // configure breadcrumb library
        BreadcrumbInject.breadcrumbHeight = resources.getDimensionPixelSize(R.dimen.breadcrumb_height)
        BreadcrumbInject.breadcrumbView = fun(context: Context, onBreadcrumbActiveListener: OnBreadcrumbActiveListener) = BreadcrumbView(context, onBreadcrumbActiveListener)
    }
}