package com.anfema.breadcrumbsample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.anfema.breadcrumb.scrolling.BreadcrumbScrollActivity
import kotlinx.android.synthetic.main.activity_layout1.*


open class Layout1Activity : BreadcrumbScrollActivity()
{
    override val expandIconView: ImageView?
        get() = img_breadcrumb_expand

    override fun onBreadcrumbExpanded()
    {
        Log.d(javaClass.simpleName, "onBreadcrumbExpanded")
    }

    override fun onBreadcrumbCollapsed()
    {
        Log.d(javaClass.simpleName, "onBreadcrumbCollapsed")
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout1)

        btn_other.setOnClickListener {
            startActivity(Intent(this, Layout2Activity::class.java))
        }
    }

    override fun getBreadcrumbTitle() = "Main"
}
