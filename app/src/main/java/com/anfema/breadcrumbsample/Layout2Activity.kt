package com.anfema.breadcrumbsample

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.anfema.breadcrumbnavigation.scrolling.BreadcrumbScrollActivity
import kotlinx.android.synthetic.main.activity_layout2.*

class Layout2Activity : BreadcrumbScrollActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout2)
        setupBreadcrumbScrollView(null, null)

        btn_level3.setOnClickListener {
            val intent = Intent(this, Layout3Activity::class.java)
            startBreadcrumbActivity(intent, "Layout3")
        }

        rv.apply {
            layoutManager = LinearLayoutManager(this@Layout2Activity)
            adapter = Layout2Adapter(arrayOf("Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item",
                    "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla",
                    "Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item", "Item", "Item", "Item", "Blablablabla", "Item", "Item", "Item"))
        }
    }
}
