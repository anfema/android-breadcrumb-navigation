package com.anfema.breadcrumpnavigation

import android.content.Intent

interface BreadcrumpComponent
{
    fun getIntent(): Intent?
    fun finish()
    fun normalStartActivity(intent: Intent)
    fun getBreadcrumbTitle(): String
}