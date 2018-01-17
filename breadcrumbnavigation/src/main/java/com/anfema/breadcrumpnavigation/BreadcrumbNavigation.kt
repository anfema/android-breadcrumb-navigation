package com.anfema.breadcrumpnavigation

import android.content.Intent

interface BreadcrumbNavigation
{
    fun goBackMultipleSteps(steps: Int)
    fun getBreadcrumbTrail(intent: Intent?): java.util.ArrayList<String>
}