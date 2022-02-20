package com.pstorli.wackymole.model

import android.app.Application
import android.content.Context
import android.graphics.Point
import com.pstorli.wackymole.model.MoleType.*
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

    // his is the list of moles (images) we are displaying on the board,
    // on screen will be displayed as row/col. Instead of using the drawable ids,
    // we are using the enumerated type MoleType
    lateinit var moles: Array<MoleType?>

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

        // Create the mole array. If null, assume MoleType.GRASS
        moles = Array (rows*cols) {null}
    }

    /**
     * Convert the position into a row / col value.
     */
    fun getRowCol (pos: Int): Point {
        val row = pos / cols
        val col = pos % cols

        return Point (row,col)
    }

    /**
     * Convert row/col into a position.
     */
    fun getPosFromRowCol (row: Int, col: Int): Int {
        return row * rows + col
    }

    /**
     * Clear the board by setting all images to grass.
     */
    fun clearBoard () {
        for (col in 0 until cols) {
            for (row in 0 until rows) {
                // Get the position
                val pos = getPosFromRowCol (row, col)

                // Set it to grass.
                moles[pos] = GRASS
            }
        }
    }

    /**
     * Get Context?
     */
    fun context (): Context {
        return getApplication<Application>().applicationContext
    }
}