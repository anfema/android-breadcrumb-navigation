package com.anfema.breadcrumpnavigation

import android.content.Intent

class BreadcrumbComponentHelper(private val breadcrumbComponent: BreadcrumpComponent)
{
    interface BreadcrumpComponent
    {
        fun getIntent(): Intent?
        fun finish()
        fun normalStartActivity(intent: Intent)
        fun getBreadcrumbTitle(): String
    }

    fun checkIfGoBackIntent()
    {
        val backStepsRemaining = breadcrumbComponent.getIntent()?.getIntExtra(BREADCRUMB_BACK_STEPS_REMAINING, 0) ?: 0
        if (backStepsRemaining > 0)
        {
            goBackMultipleSteps(backStepsRemaining)
        }
    }

    fun goBackMultipleSteps(steps: Int)
    {
        when
        {
            steps < 1  -> return // no action required
            steps == 1 -> breadcrumbComponent.finish() // only current breadcrumbComponent has to be closed
            steps > 1  ->
            {
                // at least parent breadcrumbComponent has to be closed too
                breadcrumbComponent.getIntent()?.getParcelableExtra<Intent>(BREADCRUMB_PARENT_INTENT)?.let {
                    it.putExtra(BREADCRUMB_BACK_STEPS_REMAINING, steps - 1)
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    breadcrumbComponent.normalStartActivity(it)
                }
            }
        }
    }

    fun addBreadcrumbEssentials(intent: Intent)
    {
        intent.putExtra(BREADCRUMB_PARENT_INTENT, breadcrumbComponent.getIntent())
        intent.putExtra(BREADCRUMB_BACK_STEPS_REMAINING, 0)
        intent.putExtra(BREADCRUMB_PARENT_TRAIL, getBreadcrumbTrail(breadcrumbComponent.getIntent()))
    }

    fun getBreadcrumbTrail(intent: Intent?): java.util.ArrayList<String>
    {
        val parentTitles = intent?.getStringArrayListExtra(BREADCRUMB_PARENT_TRAIL) ?: ArrayList()
        val breadcrumbTitles = ArrayList(parentTitles)
        breadcrumbTitles.add(breadcrumbComponent.getBreadcrumbTitle())
        return breadcrumbTitles
    }

    companion object
    {
        private const val BREADCRUMB_PARENT_INTENT = "BREADCRUMB_PARENT_INTENT"
        private const val BREADCRUMB_BACK_STEPS_REMAINING = "BREADCRUMB_BACK_STEPS_REMAINING"
        private const val BREADCRUMB_PARENT_TRAIL = "BREADCRUMB_PARENT_TRAIL"
    }
}