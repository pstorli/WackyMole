package com.pstorli.wackymole.util

import com.pstorli.wackymole.doit
import com.pstorli.wackymole.model.MoleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

import com.pstorli.wackymole.model.MoleType.BOMB
import com.pstorli.wackymole.model.MoleType.GRASS
import com.pstorli.wackymole.model.MoleType.HOLE
import com.pstorli.wackymole.model.MoleType.MOLE1
import com.pstorli.wackymole.model.MoleType.MOLE2
import com.pstorli.wackymole.model.MoleType.MOLE3
import com.pstorli.wackymole.rnd
import com.pstorli.wackymole.util.Consts.HUNDRED

import com.pstorli.wackymole.util.Consts.ONE
import com.pstorli.wackymole.util.Consts.ZERO

/**
 * This class handles popping up, down and sideways the moles.
 * (This class should also be executed on a background coroutine,
 * such as from the view model scope.)
 */
class MoleMachine (var moleModel: MoleModel) {

    // *********************************************************************************************
    // Vars
    // *********************************************************************************************
    var DELAY          = 666L   // The delay for the mole machine to do something. (Decrease as level increases.)
    val SLEEP          = 5000L  // The delay for the mole machine to sleep if we are paused.

    // Mole Percent Probabilities. If random num less than value below, doit.
    val GRASS_TO_HOLE  = 70     // The probability from 0 to 100 of grass changing to a hole.
    val HOLE_TO_GRASS  = 20     // The probability from 0 to 100 of hole changing back to grass.
    val HOLE_TO_MOLE1  = 88     // The probability from 0 to 100 of hole changing to a mole1.
    val HOLE_TO_MOLE2  = 66     // The probability from 0 to 100 of hole changing to a mole2.
    val HOLE_TO_MOLE3  = 44     // The probability from 0 to 100 of hole changing to a mole3.
    val MOLE_TO_HOLE   = 50     // The probability from 0 to 100 of mole changing back to a hole.

    /**
     * Run on the Default thread which is used for CPU intensive tasks.
     */
    suspend fun start () = withContext (Dispatchers.Default) {

        // Started?
        if (!moleModel.moleMachineRunning.get()) {
            // Keep running until we stop.
            moleModel.moleMachineRunning.set(true)

            // Don't stop running, until it gets set to false.
            while (moleModel.moleMachineRunning.get()) {

                // If the time is zero, we are paused.
                if (ZERO == moleModel.time.get()) {
                    // We are paused, so take a 1 sec break.
                    delay(SLEEP)
                } else {

                    // Pause a sec, or less, before we take action.
                    delay(DELAY)

                    // Check for bombs.
                    var update = bombCheck()

                    // Now do something.
                    if (messWithMoles()) {
                        update = true
                    }

                    // Update the board
                    if (update) {
                        moleModel.refreshBoard ()
                    }
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
        for (pos in 0..moleModel.rndPos()) {
            // Got Bomb?
            if (BOMB == moleModel.moles [pos]) {
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
        // Did we modify the model.moles
        var messedWith = false

        // First pick a square to play with.
        val pos = moleModel.rndPos ()

        // What is there now?
        val what = moleModel.moles [pos]

        // Based on what is there, decide what to do.
        when (what) {
            BOMB    -> {
                if (handleHoleWithBomb (pos)) {
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

            GRASS   -> {
                if (handleHoleWithGrass(pos)) {
                    messedWith = true
                }
            }

            // Sadly no fall through from GRASS to else in Kotlin?
            else -> {
                if (handleHoleWithGrass        (pos)) {
                    messedWith = true
                }
            }
        }

        return messedWith
    }

    /**
     * This square has a bomb in it.
     */
    fun handleHoleWithBomb (pos: Int): Boolean {
        // Go back to being a hole.
        return moleModel.change (pos, HOLE)
    }

    /**
     * This square has only grass.
     */
    fun handleHoleWithGrass (pos: Int): Boolean {
        var changed = false

        // Should we change to grass?
        if (GRASS_TO_HOLE.doit()) {
            // Go back to being a hole.
            changed = moleModel.change(pos, HOLE)
        }

        return changed
    }

    /**
     * This square has a mole in it.
     */
    fun handleHoleWithMole (pos: Int): Boolean {
        var changed = false

        // Should we change the hole with a mole back to just a hole?
        if (MOLE_TO_HOLE.doit()) {
            // Go back to being a hole.
            changed = moleModel.change(pos, HOLE)
        }

        return changed
    }

    /**
     * This square has a hole in it.
     */
    fun handleHoleWithHole (pos: Int): Boolean {
        var changed = false

        // Should we change back to grass?
        if (HOLE_TO_GRASS.doit()) {
            // Go back to being grass.
            changed = moleModel.change(pos, GRASS)
        }
        else {
            // Get a probability from 0..100
            val moleProb = HUNDRED.rnd()

            // What type of mole?
            if (moleProb > HOLE_TO_MOLE3) {
                // Create mole3
                changed = moleModel.change(pos, MOLE3)
            } else if (moleProb > HOLE_TO_MOLE2) {
                // Create mole2
                changed = moleModel.change(pos, MOLE2)
            } else if (moleProb > HOLE_TO_MOLE1) {
                // Create mole1
                changed = moleModel.change(pos, MOLE1)
            }
        }

        return changed
    }
}