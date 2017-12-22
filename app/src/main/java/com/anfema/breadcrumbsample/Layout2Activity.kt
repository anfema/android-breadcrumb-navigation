package com.anfema.breadcrumbsample

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.anfema.breadcrumb.scrolling.BreadcrumbScrollActivity
import com.anfema.breadcrumpnavigation.scrolling.NoExpandIconCallbacks
import com.anfema.breadcrumpnavigation.scrolling.OnBreadcrumbExpandedListener
import kotlinx.android.synthetic.main.activity_layout2.*

class Layout2Activity : BreadcrumbScrollActivity(), OnBreadcrumbExpandedListener by NoExpandIconCallbacks()
{
    override val expandIconView = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout2)


        btn_level3.setOnClickListener {
            val intent = Intent(this, Layout3Activity::class.java)
            startActivity(intent)
        }

        rv.apply {
            layoutManager = LinearLayoutManager(this@Layout2Activity)
            adapter = Layout2Adapter(arrayOf("Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item",
                    "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla",
                    "Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item"))
        }
    }

    override fun getBreadcrumbTitle() = "Other"
}
