package com.anfema.breadcrumpnavigation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * If you cannot inherit your from [BreadcrumbActivity] directly, use [BreadcrumbActivityHelper].
 */
abstract class BreadcrumbActivity : AppCompatActivity(), BreadcrumbComponent
{
    protected lateinit var breadcrumbHelper: BreadcrumbActivityHelper

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        breadcrumbHelper = BreadcrumbActivityHelper(this)
    }

    override fun startBreadcrumbActivity(intent: Intent, breadcrumbTitle: String, options: Bundle?)
    {
        breadcrumbHelper.addBreadcrumbEssentials(intent, breadcrumbTitle)
        super.startActivity(intent)
    }
}