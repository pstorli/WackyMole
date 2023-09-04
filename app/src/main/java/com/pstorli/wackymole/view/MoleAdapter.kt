package com.pstorli.wackymole.view

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.pstorli.wackymole.R
import com.pstorli.wackymole.util.MoleType
import com.pstorli.wackymole.util.MoleType.*
import com.pstorli.wackymole.model.MoleModel

/**
 * This class manage the board (grid)
 */
class MoleAdapter (var model: MoleModel) : BaseAdapter () {
    // *********************************************************************************************
    // Images
    // *********************************************************************************************
    lateinit var bomb:  Bitmap       // A bomb!
    lateinit var grass: Bitmap       // Our grass
    lateinit var hole:  Bitmap       // A hole

    // Our moles, real russian ones.
    lateinit var mole0: Bitmap       // Mole1
    lateinit var mole1: Bitmap       // Mole1
    lateinit var mole2: Bitmap       // Mole2
    lateinit var mole3: Bitmap       // Mole3

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
        mole0 = BitmapFactory.decodeResource(resources, R.drawable.mole0)
        mole1 = BitmapFactory.decodeResource(resources, R.drawable.mole1)
        mole2 = BitmapFactory.decodeResource(resources, R.drawable.mole2)
        mole3 = BitmapFactory.decodeResource(resources, R.drawable.mole3)
    }

    /**
     * Given an enum of MoleType, return the appropriate drawable.
     * If the type is null, return R.drawable.grass
     */
    fun getDrawable (type: MoleType?): Int {
        val drawableId: Int
        when (type) {
            BOMB  -> drawableId = R.drawable.bomb
            GRASS -> drawableId = R.drawable.grass
            HOLE  -> drawableId = R.drawable.hole
            MOLE0 -> drawableId = R.drawable.mole0
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
    override fun getItem(position: Int): MoleType {
        return model.moles[position]?:GRASS
    }

    /**
     * Get the item ID.
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Return the appropriate view.
     */
    @SuppressLint("InflateParams")
    override fun getView (position: Int, convertView: View?, parent: ViewGroup?): View {
        // Possibly resuse an existing view?
        var view = convertView
        val holder: MoleHolder

        // Got view?
        if (null == view) {
            // Load up the mole view.
            view             = LayoutInflater.from (model.context()).inflate(R.layout.mole_layout, null)

            // Create the biew holder
            holder           = MoleHolder()

            // Find the image view.
            holder.imageView = view?.findViewById<ImageView> (R.id.mole_view)

            // Assign the holder to the view.
            view.tag = holder
        }
        else {
            holder = view.getTag() as MoleHolder
        }

        // Set the correct image.
        holder.imageView?.setImageResource(getDrawable(model.moles[position]))

        // View should not be null now.
        return view!!
    }
}