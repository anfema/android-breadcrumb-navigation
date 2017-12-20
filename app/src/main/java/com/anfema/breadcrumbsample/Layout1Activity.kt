package com.anfema.breadcrumbsample

import android.content.Intent
import android.os.Bundle
import com.anfema.breadcrumb.scrolling.BreadcrumbScrollActivity
import kotlinx.android.synthetic.main.activity_layout1.*


open class Layout1Activity : BreadcrumbScrollActivity()
{
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
