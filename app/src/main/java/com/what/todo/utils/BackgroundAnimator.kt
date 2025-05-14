package com.what.todo.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

object BackgroundAnimator {
    fun animateBackground(view: View) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.2f, 1f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0.3f, 0.5f, 0.3f)

        ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }
} 