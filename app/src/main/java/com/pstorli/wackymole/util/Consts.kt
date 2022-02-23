package com.pstorli.wackymole.util

import android.util.Log
import com.pstorli.wackymole.BuildConfig.DEBUG // Debug flag from Build Config

object Consts {

    // *********************************************************************************************
    // Initial Values
    // *********************************************************************************************
    const val LEVEL_TIME    = 60

    // *********************************************************************************************
    // Log Tags
    // *********************************************************************************************
    const val TAG           = "WackyMole"

    // *********************************************************************************************
    // Shared Prefs Keys
    // *********************************************************************************************
    const val LEVEL         = "LEVEL"
    const val SCORE         = "SCORE"
    const val TIME          = "TIME"

    // *********************************************************************************************
    // Log helper functions
    // *********************************************************************************************

    /**
     * Log an error message.
     */
    fun logError (ex: Exception)
    {
        logError (TAG, ex.toString())
    }

    /**
     * Log an error message.
     */
    fun logError (msg:String, t: Throwable? = null)
    {
        Log.e (TAG, msg, t)
    }

    /**
     * Log an error message.
     */
    fun logError (msg:String)
    {
        logError (TAG, msg)
    }

    /**
     * Log an error message.
     */
    fun logError (tag: String, msg:String)
    {
        Log.e (tag, msg)
    }

    /**
     * Log an error message.
     */
    fun logWarning (msg:String)
    {
        Log.w (TAG, msg)
    }

    /**
     * Log an error message.
     */
    fun logWarning (tag: String, msg:String)
    {
        Log.w (tag, msg)
    }

    /**
     * Log an info message.
     */
    fun logInfo (msg:String)
    {
        Log.i (TAG, msg)
    }

    /**
     * Log an info message.
     */
    fun logInfo (tag: String, msg:String)
    {
        Log.i (tag, msg)
    }

    /**
     * Log a debug message.
     */
    fun debug (msg:String)
    {
        debug (TAG, msg)
    }

    /**
     * Log a debug message.
     */
    fun debug (tag: String, msg:String)
    {
        if (DEBUG)  Log.d(tag, msg)
    }
}