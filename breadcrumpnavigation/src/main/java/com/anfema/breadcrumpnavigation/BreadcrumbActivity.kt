package com.anfema.breadcrumpnavigation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * If you cannot inherit your from [BreadcrumbActivity] directly, use [BreadcrumbComponentHelper].
 */
abstract class BreadcrumbActivity : AppCompatActivity(), BreadcrumpComponent, BreadcrumbNavigation
{
    protected lateinit var breadcrumbHelper: BreadcrumbComponentHelper

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        breadcrumbHelper = BreadcrumbComponentHelper(this)
        breadcrumbHelper.checkIfGoBackIntent()
    }

    override fun startActivity(intent: Intent)
    {
        breadcrumbHelper.addBreadcrumbEssentials(intent)
        super.startActivity(intent)
    }

    override fun normalStartActivity(intent: Intent)
    {
        super.startActivity(intent)
    }

    override fun goBackMultipleSteps(steps: Int)
    {
        breadcrumbHelper.goBackMultipleSteps(steps)
    }
}