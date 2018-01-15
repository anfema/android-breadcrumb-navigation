package com.anfema.breadcrumb.scrolling

import android.view.View

interface Breadcrumb
{
    val view: View

    var onBreadcrumbActiveListener: OnBreadcrumbActiveListener?

    fun onFirstSelect()
    fun onActive()
    fun onUnselect()
    fun setText(text: CharSequence)
}