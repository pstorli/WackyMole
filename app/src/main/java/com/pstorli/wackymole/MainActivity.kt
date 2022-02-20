package com.pstorli.wackymole

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pstorli.wackymole.model.MoleViewModel


class MainActivity : AppCompatActivity() {

    // *********************************************************************************************
    // Vars
    // *********************************************************************************************
    lateinit var board:           GridLayout                                                        // The board
    val          movieViewModel = ViewModelProvider(this).get(MoleViewModel::class.java)      // The view model.

    // *********************************************************************************************
    // Images
    // *********************************************************************************************
    lateinit var bomb:            Bitmap                                                            // A bomb!
    lateinit var grass:           Bitmap                                                            // Our grass
    lateinit var hole:            Bitmap                                                            // A hole

    // Our moles, real russian ones.
    lateinit var mole1:           Bitmap                                                            // Mole1
    lateinit var mole2:           Bitmap                                                            // Mole2
    lateinit var mole3:           Bitmap                                                            // Mole3

    /**
     * This is where we start!
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load up the bitmaps to use on the board.
        loadImages ()

        // Fetch the board.
        board = findViewById(R.id.board)

        // Determine how many rows / cols we can have.
        movieViewModel.setBoardSize (windowManager.getScreenSize())

        // Tell the board (grid) how many rows / cols it can have.
        board.setColumnCount(movieViewModel.cols)
        board.setRowCount(movieViewModel.rows)
    }

    /**
     * Load the images for later use on the board.
     */
    fun loadImages () {
        // Is frankincense a balm?
        bomb = BitmapFactory.decodeResource(this.getResources(), R.drawable.bomb)

        // Get some grass, cuz no one rides for free!
        grass = BitmapFactory.decodeResource(this.getResources(), R.drawable.grass)

        // Diabetes can make you a real A hole.
        hole = BitmapFactory.decodeResource(this.getResources(), R.drawable.hole)

        // Send in the clowns, I mean moles.
        mole1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.mole1)
        mole2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.mole2)
        mole3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.mole3)
    }

    /**
     * Clear the board by setting all images to grass.
     */
    fun clearBoard () {
        for (col in 0 until movieViewModel.cols) {
            for (row in 0 until movieViewModel.rows) {
                var image = ImageView (this)
                // board.addView (image.setImageBitmap (grass))
            }
        }
    }
}