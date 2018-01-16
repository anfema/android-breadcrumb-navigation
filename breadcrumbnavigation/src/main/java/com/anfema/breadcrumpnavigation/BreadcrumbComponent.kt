package com.anfema.breadcrumpnavigation

import android.content.Intent

interface BreadcrumbComponent
{
    fun getIntent(): Intent?
    fun finish()
    fun normalStartActivity(intent: Intent)
    fun getBreadcrumbTitle(): String
}