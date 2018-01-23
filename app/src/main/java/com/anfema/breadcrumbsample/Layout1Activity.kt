package com.anfema.breadcrumbsample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.anfema.breadcrumbnavigation.scrolling.BreadcrumbScrollActivity
import com.anfema.breadcrumbnavigation.scrolling.OnBreadcrumbExpandedListener
import kotlinx.android.synthetic.main.activity_layout1.*


open class Layout1Activity : BreadcrumbScrollActivity(), OnBreadcrumbExpandedListener {
    override fun onBreadcrumbExpanded() {
        Log.d(javaClass.simpleName, "onBreadcrumbExpanded")
    }

    override fun onBreadcrumbCollapsed() {
        Log.d(javaClass.simpleName, "onBreadcrumbCollapsed")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout1)
        setupBreadcrumbScrollView(img_breadcrumb_expand, onBreadcrumbExpandedListener = this)

        btn_other.setOnClickListener {
            startBreadcrumbActivity(Intent(this, Layout2Activity::class.java), "Layout2")
        }
    }
}
