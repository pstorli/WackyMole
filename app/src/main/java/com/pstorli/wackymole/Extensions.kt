package com.pstorli.wackymole

import android.graphics.Insets
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics

// *************************************************************************************************
// Logginghelpers
// *************************************************************************************************

/**
 * Log an error message.
 */
fun Exception.logError ()
{
    this.message?.let { Consts.logError(it) }
}

/**
 * Log an error message.
 */
fun Exception.logError (tag: String)
{
    this.message?.let { Consts.logError(tag, it) }
}

/**
 * Log an error message.
 */
fun String.logError(t: Throwable? = null)
{
    Consts.logError(this, t)
}

/**
 * Log an error message.
 */
fun String.logError()
{
    Consts.logError(this)
}

/**
 * Log an error message.
 */
fun String.logError(tag: String)
{
    Consts.logError(tag, this)
}

/**
 * Log an error message.
 */
fun String.logWarning()
{
    logWarning(this)
}

/**
 * Log an error message.
 */
fun String.logWarning(tag: String)
{
    Consts.logWarning (tag, this)
}

/**
 * Log an info message.
 */
fun String.logInfo()
{
    Consts.logInfo(this)
}

/**
 * Log an info message.
 */
fun String.logInfo(tag: String)
{
    Consts.logInfo(tag, this)
}

/**
 * Log a debug message.
 */
fun String.debug()
{
    Consts.debug(this)
}

/**
 * Log a debug message.
 */
fun String.debug(tag: String)
{
    Consts.debug(tag, this)
}

// *************************************************************************************************
// Screen Size
// *************************************************************************************************

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