package com.anfema.breadcrumbsample

import android.content.Intent
import android.os.Bundle
import com.anfema.breadcrumb.scrolling.BreadcrumbScrollActivity
import com.anfema.breadcrumpnavigation.scrolling.NoExpandIconCallbacks
import com.anfema.breadcrumpnavigation.scrolling.OnBreadcrumbExpandedListener
import kotlinx.android.synthetic.main.activity_layout3.*

class Layout3Activity : BreadcrumbScrollActivity(), OnBreadcrumbExpandedListener by NoExpandIconCallbacks()
{
    override val expandIconView = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout3)

        btn_main.setOnClickListener {
            val intent = Intent(this, Layout1Activity::class.java)
            startActivity(intent)
        }
    }

    override fun getBreadcrumbTitle() = "Level3"
}
