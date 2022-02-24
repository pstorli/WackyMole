package com.pstorli.wackymole.util

import com.pstorli.wackymole.doit
import com.pstorli.wackymole.model.MoleModel
import com.pstorli.wackymole.model.MoleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

import com.pstorli.wackymole.model.MoleType.BOMB
import com.pstorli.wackymole.model.MoleType.GRASS
import com.pstorli.wackymole.model.MoleType.HOLE
import com.pstorli.wackymole.model.MoleType.MOLE1
import com.pstorli.wackymole.model.MoleType.MOLE2
import com.pstorli.wackymole.model.MoleType.MOLE3
import java.util.concurrent.atomic.AtomicBoolean

/**
 * This class handles popping up, down and sideways the moles.
 * (This class should also be executed on a background coroutine,
 * such as from the view model scope.)
 */
class MoleMachine (var model: MoleModel) {

    // *********************************************************************************************
    // Vars
    // *********************************************************************************************
    val DELAY          = 100L   // The delay for the mole machine to do something.
    val SLEEP          = 1000L  // The delay for the mole machine to sleep if we are paused.

    val DO_SOMETHING   = 0.5F   // The probability from 0 to 1 of doing something.
    val MOLE_TO_HOLE   = 0.3F   // The probability from 0 to 1 of mole changing back to a hole.
    val HOLE_TO_GRASS  = 0.1F   // The probability from 0 to 1 of hole changing back to grass.
    val GRASS_TO_HOLE  = 0.4F   // The probability from 0 to 1 of grass changing to a hole.
    val HOLE_TO_MOLE   = 0.7F   // The probability from 0 to 1 of hole changing to a mole.
    val HOLE_TO_MOLE1  = 0.8F   // The probability from 0 to 1 of hole changing to a mole1.
    val HOLE_TO_MOLE2  = 0.4F   // The probability from 0 to 1 of hole changing to a mole2.
    val HOLE_TO_MOLE3  = 0.1F   // The probability from 0 to 1 of hole changing to a mole3.

    /**
     * Run on the Default thread which is used for CPU intensive tasks.
     */
    suspend fun start () = withContext (Dispatchers.Default) {
        // Keep running until we stop.
        model.moleMachineRunning.set(true)
        
        // Don't stop running, until it gets set to false.
        while (model.moleMachineRunning.get()) {
            // If the time is zero, we are paused.
            if (0 == model.time) {
                // We are paused, so take a 1 sec break.
                delay (SLEEP)
            }
            else {

                // Pause a sec, or less, before we take action.
                delay (DELAY)

                // Check for bombs.
                var update = bombCheck ()

                // Now do something.
                if (messWithMoles ()) {
                    update = true
                }

                // Update the board
                if (update) {
                    model.update.postValue (true)
                }
            }
        }
    }

    /**
     * We want bombs to fizzle out quickly,
     * not onesy twosy like messWithMoles
     */
    fun bombCheck (): Boolean {
        var foundBomb = false

        // Loop through all the squares looking for bombs.
        for (pos in 0..model.moles.size-1) {
            // Got Bomb?
            if (BOMB == model.moles [pos]) {
                handleHoleWithBomb (pos)

                foundBomb = true
            }
        }

        return foundBomb
    }

    /**
     * Let's mess with the moles.
     */
    fun messWithMoles (): Boolean {
        var messedWith = false

        // First pick a square to play with.
        val pos = (0..model.moles.size-1).random()

        // What is there now?
        val what = model.moles [pos]

        // Based on what is there, decide what to do.
        when (what) {
            BOMB    -> {
                if (handleHoleWithBomb (pos)) {
                    messedWith = true
                }
            }
            GRASS   -> {
                if (handleGrass        (pos)) {
                    messedWith = true
                }
            }
            HOLE    -> {
                if (handleHoleWithHole (pos)) {
                    messedWith = true
                }
            }
            MOLE1   -> {
                if (handleHoleWithMole (pos)) {
                    messedWith = true
                }
            }
            MOLE2   -> {
                if (handleHoleWithMole (pos)) {
                    messedWith = true
                }
            }
            MOLE3   -> {
                if (handleHoleWithMole (pos)) {
                    messedWith = true
                }
            }
            else -> {
                // Nuttin tooooooooo do here. Crickets chirping.
            }
        }

        return messedWith
    }

    /**
     * Change this square to this mole type
     */
    fun change (pos: Int, what: MoleType): Boolean {
        // This is this positions new state.
        model.moles [pos] = what

        return true
    }

    /**
     * This square has a bomb in it.
     */
    fun handleHoleWithBomb (pos: Int): Boolean {
        // Go back to being a hole.
        return change (pos, HOLE)
    }

    /**
     * This square has a mole in it.
     */
    fun handleHoleWithMole (pos: Int): Boolean {
        var changed = false

        // Should we do something?
        if (DO_SOMETHING.doit()) {
            // Should we change the hole with a mole back to just a hole?
            if (MOLE_TO_HOLE.doit()) {
                // Go back to being a hole.
                changed = change(pos, HOLE)
            }
        }

        return changed
    }

    /**
     * This square has a hole in it.
     */
    fun handleHoleWithHole (pos: Int): Boolean {
        var changed = false

        // Should we do something?
        if (DO_SOMETHING.doit()) {
            // Should we change to grass?
            if (HOLE_TO_GRASS.doit()) {
                // Go back to being grass.
                changed = change(pos, GRASS)
            }
            // Should we change to a mole?
            else if (HOLE_TO_MOLE.doit()) {
                // What type of mole?
                if (HOLE_TO_MOLE1.doit()) {
                    // Go back to being a hole.
                    changed = change(pos, MOLE1)
                }
                else if (HOLE_TO_MOLE2.doit()) {
                    // Go back to being a hole.
                    changed = change(pos, MOLE2)
                }
                else if (HOLE_TO_MOLE3.doit()) {
                    // Go back to being a hole.
                    changed = change(pos, MOLE3)
                }
            }
        }

        return changed
    }

    /**
     * This square has only grass.
     */
    fun handleGrass (pos: Int): Boolean {
        var changed = false

        // Should we do something?
        if (DO_SOMETHING.doit()) {
            // Should we change to grass?
            if (GRASS_TO_HOLE.doit()) {
                // Go back to being a hole.
                changed = change(pos, HOLE)
            }
        }

        return changed
    }
}