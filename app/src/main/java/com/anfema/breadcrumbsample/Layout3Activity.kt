package com.anfema.breadcrumbsample

import android.content.Intent
import android.os.Bundle
import com.anfema.breadcrumb.scrolling.BreadcrumbScrollActivity
import kotlinx.android.synthetic.main.activity_layout3.*

class Layout3Activity : BreadcrumbScrollActivity()
{
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
