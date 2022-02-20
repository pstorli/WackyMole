package com.pstorli.wackymole

import android.graphics.Insets
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics

// *********************************************************************************************
// Screen Size
// *********************************************************************************************

/**
 * Get the screen width/height.
 */
fun WindowManager.getScreenSize (): Point {
    // *********************************************************************************************
    // Current / Correct way to do this.
    // *********************************************************************************************
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        val insetsWidth = insets.right + insets.left
        val insetsHeight = insets.top + insets.bottom

        // Legacy size that Display#getSize reports
        val bounds: Rect = windowMetrics.getBounds()
        Point ((bounds.width() - insetsWidth), (bounds.height() - insetsHeight))
    } else {
        // *********************************************************************************************
        // This is the older, backup, less recommended way.
        // Note: I'd like to make warning below go away, but it is already
        // doing the correct way with the version check above.
        // *********************************************************************************************
        val displayMetrics = DisplayMetrics()
        defaultDisplay.getMetrics(displayMetrics)
        defaultDisplay.getMetrics(displayMetrics)
        Point (displayMetrics.widthPixels, displayMetrics.heightPixels)
    }
}