package com.pstorli.wackymole

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.AdapterView
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.pstorli.wackymole.model.MoleModel
import com.pstorli.wackymole.view.MoleAdapter
import com.pstorli.wackymole.util.Consts
import com.pstorli.wackymole.util.Consts.LEVEL_TIME
import com.pstorli.wackymole.util.Consts.ZERO

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
    // Late Vars
    // *********************************************************************************************

    // The board
    private lateinit var board:             GridView

    // The level
    private lateinit var level:             TextView

    // The mole view model
    private lateinit var moleModel:         MoleModel

    // Menu Item toggles between play and pause
    private lateinit var playPauseMenuItem: MenuItem

    // The score
    private lateinit var score:             TextView

    // The time
    private lateinit var time:              TextView
    private var timer:                      CountDownTimer? = null

    // The toolbar
    private lateinit var toolbar:           Toolbar

    // *********************************************************************************************
    // Late Sound Vars
    // *********************************************************************************************

    // Media player used for sound effects explosion and whack/hit/click sound.
    private lateinit var bombSound:        MediaPlayer
    private lateinit var hitSound:         MediaPlayer

    /**
     * Return / Create the view model.
     *
     * The ViewModelProvider will return the same view model
     * for anyone who asks for it when they supply this (MoleActivity) activity.
     *
     * So best to ask for it now so that it is associated with this activity.
     */
    fun getViewModel ():MoleModel  {
        return ViewModelProvider(this).get(MoleModel::class.java)
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
        // Create media player for sound effects.
        bombSound = MediaPlayer.create(this, R.raw.smack)
        hitSound  = MediaPlayer.create(this, R.raw.smack)

        // Load up some items from the layout.
        findGUIStuff ()

        // Get / Create the view model.
        moleModel = getViewModel ()

        // Set the square size. Height and width are the same. Use grass size as size for all.
        moleModel.squareSize = BitmapFactory.decodeResource (this.resources, R.drawable.grass).width

        // Adjust for the margin
        val margin: Int = this.resources.getDimension(R.dimen.mole_margin).toInt()+this.resources.getDimension(R.dimen.margin_adj).toInt()

        // Reduce size of grid to allow toolbar and score lines to be shown.
        val heightAdj = this.resources.getDimension(R.dimen.height_adj).toInt()

        // Set the screen size.
        moleModel.setBoardSize (width, height-heightAdj, margin)

        // Set the number of columns
        board.numColumns = moleModel.cols

        // Set the adapter for the board (grid view).
        board.adapter = MoleAdapter (moleModel)

        // Listen for them to click the board.
        board.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            // Notyify model's mole nachine that something was clicked.
            moleModel.clicked (position)

            // Play hit sound if we wacked a mole.
            if (moleModel.whackedMole(position)) {
                // Yes, we whacked that bad boy. mole.
                hitSound.start()
            }
        }

        // Restore prev level, score, time and board.
        moleModel.restore ()

        // What level are we at?
        updateLevelText()

        // What's the score?
        updateScoreText()

        // Anyone got the time?
        updateTimeText()

        // Set up observer to update the board (moles)
        // from the live data MoleModel.update
        moleModel.update.observe(this) {
            // Reload the board from the view model.
            (board.adapter as MoleAdapter).notifyDataSetChanged()
        }

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
            // play or pause
            R.id.playPause -> {
                playPausePressed()
                true
            }

            // reset game
            R.id.reset -> {
                resetPressed ()
                true
            }

            // help!
            R.id.help -> {
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

        // Start the mole machine?
        if (!moleModel.moleMachineRunning.get()) {
            // Fire up the engines!
            moleModel.start()
        }

        // If play, (runnnig and not paused.)
        if (moleModel.time.get()>ZERO) {
            // Was / Is the timer running?
            if (null != timer) {
                timer?.cancel()
            }

            pause()
        }
        // If paused, play
        else {
            // Create the countdown timer?
            if (null == timer) {
                // Create the timer.
                timer = object : CountDownTimer(LEVEL_TIME * Consts.SECOND, Consts.SECOND) {
                    /**
                     * Ticme is ticking.
                     */
                    override fun onTick(millisUntilFinished: Long) {
                        // Get current time and subtract a sec off it.
                        var newTime:Int = moleModel.time.get()
                        newTime--

                        // Update time and time text.
                        moleModel.time.set(newTime)
                        updateTimeText()
                    }

                    /**
                     * Outta time!
                     */
                    override fun onFinish() {
                        moleModel.time.set(ZERO)
                        updateTimeText()
                        pause()
                    }
                }

                // Start the timer.
                timer?.start()
            }

            play()
        }

        // Anyone got the time?
        updateTimeText()
    }

    fun play () {
        // Change icon to pause.
        playPauseMenuItem.setTitle (getString(R.string.pause))
        playPauseMenuItem.icon = application.get (R.drawable.pause)

        // Play ball!
        moleModel.time.set(LEVEL_TIME.toInt())
    }

    fun pause () {
        // Pause Game. Change icon to play.
        playPauseMenuItem.setTitle (getString(R.string.play))
        playPauseMenuItem.icon = application.get (R.drawable.play)

        // Stop the presses.
        moleModel.time.set (ZERO)
    }

    /**
     * Update the level text.
     */
    fun updateLevelText () {
        // Update the level.
        level.text = moleModel.level.toString()
    }

    /**
     * Update the level text.
     */
    fun updateScoreText () {
        // Update the level.
        score.text = moleModel.score.toString()
    }

    /**
     * Update the time text.
     */
    fun updateTimeText () {
        // Update the time.
        time.text = moleModel.time.toString()
    }

    /**
     * The reset menu was selected.
     */
    fun resetPressed () {
        "reset menu pressed.".debug()

        // Reset level and score.
        moleModel.reset()

        updateLevelText ()
        updateScoreText ()
    }

    /**
     * The help menu was selected.
     */
    fun helpPressed () {
        "help menu pressed.".debug()
    }

    /**
     * Going down, all hands abandon ship!
     * Do any cleanup before we all are gone.
     */
    override fun onDestroy() {
        super.onDestroy()

        // Save level and score
        moleModel.save ()
    }
}