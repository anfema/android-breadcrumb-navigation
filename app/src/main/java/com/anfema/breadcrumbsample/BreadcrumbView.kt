package com.anfema.breadcrumbsample

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Color
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.TextView
import com.anfema.breadcrumb.scrolling.Breadcrumb
import com.anfema.breadcrumb.scrolling.OnBreadcrumbActiveListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.completable.CompletableDelay
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class BreadcrumbView(context: Context, override var onBreadcrumbActiveListener: OnBreadcrumbActiveListener?) : TextView(context), Breadcrumb
{
    override val view: View
        get() = this

    var delayedExecution: Disposable? = null

    init
    {
        setBackgroundColor(Color.BLACK)
        setTextColor(Color.WHITE)
    }

    override fun onInactive()
    {
        Log.d("BreadcrumbView", "unselected")
        setBackgroundColor(Color.BLACK)
        delayedExecution?.let {
            delayedExecution?.dispose()
            delayedExecution = null
        }
    }

    override fun onHover()
    {
        Log.d("BreadcrumbView", "selected")
        setBackgroundColor(Color.GRAY)
        delayedExecution = CompletableDelay.timer(500, TimeUnit.MILLISECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onActive()
                }, {})
    }

    override fun onActive()
    {
        setBackgroundColor(Color.BLUE)
        onBreadcrumbActiveListener?.onSelectedBreadcrumbActive()
    }

    fun onSelected()
    {
        vibrate()
    }

    @Suppress("DEPRECATION")
    private fun vibrate()
    {
        val vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        val vibrateDuration = 100L // in milliseconds
        if (Build.VERSION.SDK_INT >= 26)
        {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrateDuration, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        else
        {
            vibrator.vibrate(vibrateDuration)
        }
    }
}
