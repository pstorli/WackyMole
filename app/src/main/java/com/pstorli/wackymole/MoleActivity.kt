package com.pstorli.wackymole

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.pstorli.wackymole.model.MoleViewModel
import com.pstorli.wackymole.util.Consts.LEVEL_TIME
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
    lateinit var board:             GridView                                                        // The board
    lateinit var level:             TextView                                                        // The level
    lateinit var score:             TextView                                                        // The score
    lateinit var time:              TextView                                                        // The time
    lateinit var moleViewModel:     MoleViewModel                                                   // The view model
    lateinit var toolbar:           Toolbar                                                         // The toolbar
    lateinit var playPauseMenuItem: MenuItem

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
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
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

        // Set up toolbar and toolbar menu.
        setUpToolbar ()

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

                // Now finish the onCreate
                delayedCreate (width,height)
            }
        })
    }

    /**
     * Load GUI things from layout.
     */
    fun findGUIStuff () {

        // Get the board.
        board = findViewById (R.id.board)

        // What level are we at?
        level = findViewById (R.id.level)

        // Have we scored?
        score = findViewById (R.id.score)

        // Does anybody really know what time it is?
        time  = findViewById (R.id.time)

    }

    /**
     * Do this after we get the avail area.
     */
    fun delayedCreate (width: Int, height: Int) {

        // Load up some items from the layout.
        findGUIStuff ()

        // Get / Create the view model.
        moleViewModel = getViewModel ()

        // Set the square size. Height and width are the same. Use grass size as size for all.
        moleViewModel.squareSize = BitmapFactory.decodeResource (this.resources, R.drawable.grass).width

        // Adjust for the margin
        val margin: Int = this.resources.getDimension(R.dimen.mole_margin).toInt()+this.resources.getDimension(R.dimen.margin_adj).toInt()

        // Reduce size of grid to allow toolbar and score lines to be shown.
        val heightAdj = this.resources.getDimension(R.dimen.height_adj).toInt()

        // Set the screen size.
        moleViewModel.setBoardSize (width, height-heightAdj, margin)

        // Set the number of columns
        board.numColumns = moleViewModel.cols

        // Set the adapter for the board (grid view).
        board.adapter = MoleAdapter (moleViewModel)

        // Restore prev level, score and time
        moleViewModel.restore ()

        // Let them know how the game is played.
        this.toast (R.string.pressPlay)
    }

    // *********************************************************************************************
    // Toolbar
    // *********************************************************************************************

    /**
     * Create the toolbar
     */
    fun setUpToolbar () {
        // Add the toolbar.
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.showOverflowMenu()

        // Display application icon in the toolbar
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setLogo(R.drawable.ic_launcher)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)
    }

    /**
     * The option menu has play/pause button, reset and help buttons.
     */
    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu (menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.mole_menu, menu)

        // We want the overflow menu to also display icons as well as text.
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }

        // Save the playPause menu item for later.
        playPauseMenuItem = menu.findItem(R.id.playPause)

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * A menu item was selected.
     */
    override fun onOptionsItemSelected (item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.playPause -> {                                                                      // play or pause
                playPausePressed()
                true
            }

            R.id.reset -> {                                                                         // reset game
                resetPressed ()
                true
            }

            R.id.help -> {                                                                           // help!
                helpPressed ()
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    /**
     * The play / pause menu was selected.
     */
    fun playPausePressed () {
        "play / pause menu pressed.".debug()

        // If play, pause.
        if (moleViewModel.time>0) {
            // Pause Game. Change icon to play.
            playPauseMenuItem.setTitle (getString(R.string.play))
            playPauseMenuItem.icon = application.get (R.drawable.play)

            // Stop the presses.
            moleViewModel.time = 0
        }
        // If paused, play
        else {
            // Change icon to pause.
            playPauseMenuItem.setTitle (getString(R.string.pause))
            playPauseMenuItem.icon = application.get (R.drawable.pause)

            // Play ball!
            moleViewModel.time = LEVEL_TIME
        }

        // Anyone got the time?
        updateTime ()

        // Save level, score and time
        moleViewModel.save ()
    }

    /**
     * Update the time text.
     */
    fun updateTime () {
        // Update the time.
        time.text = moleViewModel.time.toString()
    }

    /**
     * The reset menu was selected.
     */
    fun resetPressed () {
        "reset menu pressed.".debug()
    }

    /**
     * The help menu was selected.
     */
    fun helpPressed () {
        "help menu pressed.".debug()
    }
}