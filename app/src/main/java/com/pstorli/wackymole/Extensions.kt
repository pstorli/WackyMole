package com.pstorli.wackymole

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Insets
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.pstorli.wackymole.util.Consts
import kotlin.random.Random

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

/**
 * Return true If the random number generated
 * is less than this float value.
 */
fun Float.doit (): Boolean {
    val random1 = Random(0)
    return random1.nextFloat()<this
}

/**
 * Get a drawable resource.
 */
fun Application.get (id: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, id, theme)
}

@SuppressLint("ResourceAsColor")
fun Context.getResIdFromAttr(@AttrRes attrResId: Int): Int {
    val typedValue = TypedValue()
    val theme: Resources.Theme = getTheme()
    theme.resolveAttribute(attrResId, typedValue, true)
    return typedValue.resourceId // this.getColor (typedValue.resourceId)
}

@SuppressLint("ResourceAsColor")
fun Context.getColorFromAttr(@AttrRes attrResId: Int): Int {
    return getColor (getResIdFromAttr(attrResId))
}

/**
 * Show a toast message.
 */
fun Activity.toast (stringResId: Int)
{
    toast (this.getString(stringResId))
}

/**
 * Show a toast message.
 */
fun Activity.toast (text: String)
{
    // Get the snackbar.
    val snackBar = Snackbar.make (window.decorView.findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)

    // Set tint
    val backgroundColor = getColorFromAttr(R.attr.toolbarBackgroundColor)
    snackBar.setBackgroundTint(backgroundColor)
    snackBar.view.setBackgroundColor(backgroundColor)

    // change snackbar text color
    val textColor: Int = getColorFromAttr(R.attr.toolbarTextColor)
    snackBar.setActionTextColor(textColor)
    snackBar.setTextColor(textColor)

    // Show scooby snacks
    snackBar.show()
}
