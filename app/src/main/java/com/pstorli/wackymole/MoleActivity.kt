package com.pstorli.wackymole

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.pstorli.wackymole.model.MoleViewModel
import com.pstorli.wackymole.view.MoleAdapter


/**
 * This is the MoleActivity, usually called the MainActivity.
 *
 * I usually leave it as MainActivity, but this is a game about moles,
 *
 * so I'm just having fun doing something to benefit mankind, or
 *
 * at least anyone curious about how to create android, kotlin apps
 * using Google's MVVM technology.
 */
class MoleActivity : AppCompatActivity() {

    // *********************************************************************************************
    // Vars
    // *********************************************************************************************
    lateinit var board: GridView                                                                    // The board

    /**
     * Return / Create the view model.
     *
     * The ViewModelProvider will return the same view model
     * for anyone who asks for it when they supply this (MoleActivity) activity.
     *
     * So best to ask for it now so that it is associated with this activity.
     */
    fun getViewModel ():MoleViewModel  {
        return ViewModelProvider(this).get(MoleViewModel::class.java)
    }

    /**
     * Handle configuration changes.
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            "ORIENTATION LANDSCAPE".logInfo()
        } else {
            "ORIENTATION PORTRAIT".logInfo()
        }
    }

    /**
     * This is where we start!
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout for the activity.
        setContentView(R.layout.mole_main)

        // We want to know the size of the visible area, so use tree observer to do things before drawn to screen.
        val constraintLayout = findViewById<View>(R.id.main_mole) as ConstraintLayout
        constraintLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Remove tree observer so we don't get called again.
                constraintLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Get the avail area.
                val width  = constraintLayout.width
                val height = constraintLayout.height

                // No wfinish the onCreate
                delayedCreate (width,height)
            }
        })
    }

    /**
     * Do this after we get the avail area.
     */
    fun delayedCreate (width: Int, height: Int) {
        // Get / Create the view model.
        val moleViewModel = getViewModel ()

        // Set the square size. Height and width are the same. Use grass size as size for all.
        moleViewModel.squareSize = BitmapFactory.decodeResource (this.resources, R.drawable.grass).width

        // Adjust for the margin
        val margin: Int = this.resources.getDimension(R.dimen.mole_margin).toInt()+this.resources.getDimension(R.dimen.margin_adj).toInt()

        // Set the screen size.
        moleViewModel.setBoardSize (width, height, margin)

        // Get the board.
        board = findViewById (R.id.board)

        // Set the number of columns
        board.numColumns = moleViewModel.cols

        // Set the adapter for the board (grid view).
        board.adapter = MoleAdapter (moleViewModel)
    }
}