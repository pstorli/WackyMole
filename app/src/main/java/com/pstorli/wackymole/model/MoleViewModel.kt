package com.pstorli.wackymole.model

import android.app.Application
import android.graphics.Point
import androidx.lifecycle.AndroidViewModel
import com.pstorli.wackymole.Consts

class MoleViewModel (application: Application)  : AndroidViewModel(application) {

    // /////////////////////////////////////////////////////////////////////////////////////////////
    // Vars
    // /////////////////////////////////////////////////////////////////////////////////////////////

    // What is the screen size?
    lateinit var screenSize: Point

    // How many rows / cols do we have?
    var rows  = 0
    var cols  = 0

    // What level are we on?
    var level = 0

    // How much time do we really have?
    var time = 60

    /**
     * Compute the rows / cols
     * Each image is 128x128 @See Consts.kt - SQUARE_SIZE
     */
    fun setBoardSize (size: Point) {
        // Get the screen size.
        screenSize = size

        // Compute rows / cols
        rows = screenSize.y / Consts.SQUARE_SIZE
        cols = screenSize.x / Consts.SQUARE_SIZE
    }
}