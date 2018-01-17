package com.anfema.breadcrumpnavigation

import android.content.Intent

open class BreadcrumbActivityHelper(private val breadcrumbComponent: BreadcrumbComponent) : BreadcrumbNavigation
{
    init
    {
        checkIfGoBackIntent()
    }

    fun checkIfGoBackIntent()
    {
        val backStepsRemaining = breadcrumbComponent.getIntent()?.getIntExtra(BREADCRUMB_BACK_STEPS_REMAINING, 0) ?: 0
        if (backStepsRemaining > 0)
        {
            goBackMultipleSteps(backStepsRemaining)
        }
    }

    override fun goBackMultipleSteps(steps: Int)
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
                    breadcrumbComponent.startActivity(it)
                }
            }
        }
    }

    fun addBreadcrumbEssentials(intent: Intent, breadcrumbTitle: String)
    {
        intent.putExtra(BREADCRUMB_PARENT_INTENT, breadcrumbComponent.getIntent())
        intent.putExtra(BREADCRUMB_BACK_STEPS_REMAINING, 0)
        val breadcrumbTrail = getBreadcrumbTrail(breadcrumbComponent.getIntent())
        val nextBreadcrumbTrail = addCurrentTitleToBreadcrumbs(breadcrumbTrail, breadcrumbTitle)
        intent.putExtra(BREADCRUMB_TRAIL, nextBreadcrumbTrail)
    }

    override fun getBreadcrumbTrail(intent: Intent?): java.util.ArrayList<String>
    {
        return intent?.getStringArrayListExtra(BREADCRUMB_TRAIL) ?: ArrayList()
    }

    private fun addCurrentTitleToBreadcrumbs(parentTitles: java.util.ArrayList<String>, breadcrumbTitle: String): ArrayList<String>
    {
        val breadcrumbTitles = ArrayList(parentTitles)
        breadcrumbTitles.add(breadcrumbTitle)
        return breadcrumbTitles
    }

    companion object
    {
        private const val BREADCRUMB_PARENT_INTENT = "BREADCRUMB_PARENT_INTENT"
        private const val BREADCRUMB_BACK_STEPS_REMAINING = "BREADCRUMB_BACK_STEPS_REMAINING"
        private const val BREADCRUMB_TRAIL = "BREADCRUMB_TRAIL"
    }
}