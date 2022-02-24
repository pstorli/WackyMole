package com.pstorli.wackymole.model

import android.app.Application
import android.content.Context
import android.graphics.Point
import com.pstorli.wackymole.model.MoleType.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pstorli.wackymole.util.Consts.LEVEL
import com.pstorli.wackymole.util.Consts.SCORE
import com.pstorli.wackymole.util.Consts.SQUARE_SIZE
import com.pstorli.wackymole.util.Consts.TIME
import com.pstorli.wackymole.util.Consts.ZERO
import com.pstorli.wackymole.util.MoleMachine
import com.pstorli.wackymole.util.MolePrefs
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * This class is where all our data is kept, classic MVVM.
 * What is not so classic is that this app has no repo's to get data from.
 *
 * Also unique is the MoleMachine, which runs in the background and is
 * responsible for messing with the moles (the board.)
 */
class MoleModel (application: Application)  : AndroidViewModel (application) {

    // *********************************************************************************************
    // Vars
    // *********************************************************************************************

    var rows        = ZERO                                 // How many rows / cols do we have?
    var cols        = ZERO
    var squareSize  = SQUARE_SIZE                          // What is the square size?
    var level       = ZERO                                 // What level are we on?
    var score       = ZERO                                 // What's the score?
    var time        = ZERO                                 // How much time do we really have?
    val prefs       = MolePrefs (application)              // Secure / Encrypted shared prefs.
    val update      = MutableLiveData<Boolean> ()          // Boink this to notify activity to update the board.
    var moleMachine = MoleMachine (this)             // The mole machine moles the moles around in a confusing way, or at least in the most confusing way possible.

    // *********************************************************************************************
    // Late Vars
    // *********************************************************************************************

    lateinit var    screenSize:     Point         // What is the screen size?

    // This is the list of moles (images) we are displaying on the board,
    // on screen will be displayed as row/col. Instead of using the drawable ids,
    // we are using the enumerated type MoleType
    lateinit var    moles:          Array<MoleType?>

    // *********************************************************************************************
    // Mole Machine vars and helpful methods.
    // *********************************************************************************************

    // Are we running? Or just sitting there.
    // We need to be able to update the running state from
    // different scopes.
    // Sometimes I sits and thinks and sometimes I just sit.
    var moleMachineRunning: AtomicBoolean = AtomicBoolean(false)

    // A list of clicks to process.
    private val clicks: MutableList<Int> = mutableListOf()

    /**
     * Something was clicked. Keep the clicks as a list,
     * as user is allowed to go click crazy.
     */
    fun clicked (pos: Int) {
        synchronized(this) {
            clicks.add(pos)
        }
    }

    // *********************************************************************************************
    // Functions
    // *********************************************************************************************

    /**
     * Start the mole machine.
     */
    init {
        viewModelScope.launch {
            // Lauch this bad boy, er mole, on the Default background thread.
            moleMachine.start()
        }
    }

    /**
     * Did we just whack-a-mole?
     * (IE Does this position contain a mole?)
     */
    fun whackedMole (pos: Int): Boolean {
        return MOLE1 == moles[pos] || MOLE2 == moles[pos] || MOLE3 == moles[pos]
    }

    /**
     * Compute the rows / cols
     */
    fun setBoardSize (width: Int, height: Int, margin: Int) {
        // Get the screen size.
        screenSize = Point (width,height)

        // Compute rows / cols
        rows = screenSize.y / (squareSize+margin)
        cols = screenSize.x / (squareSize+margin)

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

    /**
     * Save the score, level and time
     */
    fun save () {
        prefs.setPref(LEVEL, level)
        prefs.setPref(SCORE, score)
        prefs.setPref(TIME,  time)
    }

    /**
     * Restore the score, level and time.
     */
    fun restore () {
        level = prefs.getPref(LEVEL, ZERO)
        score = prefs.getPref(SCORE, ZERO)
        time  = prefs.getPref (TIME, ZERO)
    }

    /**
     * Going down, all hands abandon ship!
     * Do any cleanup before we all are gone.
     */
    override fun onCleared() {
        super.onCleared()

        // Stop the machine.
        moleMachineRunning.set (false)
    }
}