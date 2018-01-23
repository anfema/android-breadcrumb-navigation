package com.anfema.breadcrumpnavigation.scrolling

import android.view.View

interface Breadcrumb
{
    val view: View

    var onBreadcrumbActiveListener: OnBreadcrumbActiveListener?

    fun onInactive()
    fun onHover()
    fun onActive()
    fun onSelected()
    fun setText(text: CharSequence)
    fun activateExpandedMode()
    fun deactivateExpandedMode()
}