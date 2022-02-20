package com.pstorli.wackymole.view

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import com.pstorli.wackymole.R
import com.pstorli.wackymole.model.MoleType
import com.pstorli.wackymole.model.MoleType.*
import com.pstorli.wackymole.model.MoleViewModel

/**
 * This class manage the board (grid)
 */
class MoleAdapter (var model: MoleViewModel) : BaseAdapter () {

    // *********************************************************************************************
    // Images
    // *********************************************************************************************
    lateinit var bomb:  Bitmap                                                                      // A bomb!
    lateinit var grass: Bitmap                                                                      // Our grass
    lateinit var hole:  Bitmap                                                                      // A hole

    // Our moles, real russian ones.
    lateinit var mole1: Bitmap                                                                      // Mole1
    lateinit var mole2: Bitmap                                                                      // Mole2
    lateinit var mole3: Bitmap                                                                      // Mole3

    init {
        // Load up the moles (bitmaps/pngs) to use on the board.
        loadImages ()
    }

    /**
     * Load the images for later use on the board.
     */
    fun loadImages () {
        // Get some resources and pull yourself up by your boot straps.
        val resources : Resources = model.context().resources

        // What is frankincense? A balm. You gave the baby a bomb? Life of Brian.
        bomb = BitmapFactory.decodeResource(resources, R.drawable.bomb)

        // Get some grass, cuz no one rides for free!
        grass = BitmapFactory.decodeResource(resources, R.drawable.grass)

        // Diabetes can make you a real A hole.
        hole = BitmapFactory.decodeResource(resources, R.drawable.hole)

        // Send in the clowns, I mean moles.
        mole1 = BitmapFactory.decodeResource(resources, R.drawable.mole1)
        mole2 = BitmapFactory.decodeResource(resources, R.drawable.mole2)
        mole3 = BitmapFactory.decodeResource(resources, R.drawable.mole3)
    }

    /**
     * Given an enum of MoleType, return the appropriate drawable.
     * If the type is null, return R.drawable.grass
     */
    fun getDrawable (type: MoleType?): Int {
        var drawableId: Int = R.drawable.grass
        when (type) {
            BOMB  -> drawableId = R.drawable.bomb
            GRASS -> drawableId = R.drawable.grass
            HOLE  -> drawableId = R.drawable.hole
            MOLE1 -> drawableId = R.drawable.mole1
            MOLE2 -> drawableId = R.drawable.mole2
            MOLE3 -> drawableId = R.drawable.mole3

            // Default to GRASS (R.drawable.grass) if nothing else fits.
            // Gas, grass or ass, no one rides for free.
            else ->
                drawableId = R.drawable.grass
        }

        // return what we found.
        return drawableId
    }

    /**
     * How many moles are we dealing with?
     */
    override fun getCount(): Int {
        return model.moles.size
    }

    /**
     * Get a mole. If null mole, its just GRASS.
     */
    override fun getItem(p0: Int): MoleType {
        return model.moles[p0]?:GRASS
    }

    /**
     * Get the item ID.
     * TODO: Check if returning 0 causes any trouble?
     */
    override fun getItemId(p0: Int): Long {
        return 0
    }

    /**
     * Return the appropriate view.
     */
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val imageView = ImageView(model.context())
        imageView.setImageResource(getDrawable (model.moles[position]))
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
        imageView.setLayoutParams(AbsListView.LayoutParams(70, 70))
        return imageView
    }
}