package com.anfema.breadcrumpnavigation

import android.content.Intent
import android.os.Bundle

interface BreadcrumbComponent
{
    fun getIntent(): Intent
    fun finish()
    fun startActivity(intent: Intent)
    fun startBreadcrumbActivity(intent: Intent, breadcrumbTitle: String, options: Bundle? = null)
}