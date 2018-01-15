package com.anfema.breadcrumb.scrolling

import android.content.Context

// TODO replace this singleton with dagger?
object BreadcrumbInject
{
    lateinit var breadcrumbView: (Context, OnBreadcrumbActiveListener) -> Breadcrumb

    var breadcrumbHeight: Int = 0
}