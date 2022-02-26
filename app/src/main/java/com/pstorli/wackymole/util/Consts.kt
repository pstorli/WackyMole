package com.pstorli.wackymole.util

import android.util.Log
import com.pstorli.wackymole.BuildConfig.DEBUG // Debug flag from Build Config

object Consts {

    // *********************************************************************************************
    // Initial Values
    // *********************************************************************************************
    const val LEVEL_TIME    = 60L
    const val NEGATIVE      = -1
    const val SECOND        = 1000L // 1000 Milli seconds == 1 Second
    const val SQUARE_SIZE   = 96
    const val ZERO          = 0

    // This is the initial game speed.
    // Game speed increases with level.
    val GAME_SPEED          = 500L
    val GAME_SPEED_FASTEST  = 100L
    val GAME_SPEED_DEC      = 50L

    // *********************************************************************************************
    // Log Tags
    // *********************************************************************************************
    const val TAG           = "WackyMole"

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