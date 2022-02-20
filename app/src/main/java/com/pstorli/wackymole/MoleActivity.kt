package com.pstorli.wackymole

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
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
     * return / Create the view model.
     * The ViewModelProvider will return the same view model
     * for anyone who asks for it when they supply this (MoleActivity) activity.
     * So best to ask for it now so that it is associated with this activity.
     */
    fun getViewModel ():MoleViewModel  {
        return ViewModelProvider(this).get(MoleViewModel::class.java)
    }

    /**
     * This is where we start!
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout for the activity.
        setContentView(R.layout.activity_main)

        // Get / Create the view model.
        val moleViewModel = getViewModel ()

        // Set the square size. Height and width are the same. Use grass size as size for all.
        moleViewModel.squareSize = BitmapFactory.decodeResource (this.resources, R.drawable.grass).width

        // Determine how many rows / cols we can have.
        moleViewModel.setBoardSize (windowManager.getScreenSize())

        // Get the board.
        board = findViewById (R.id.board)

        // Set the adapter for the board (grid view).
        board.adapter = MoleAdapter (moleViewModel)

    }
}