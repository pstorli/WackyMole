package com.pstorli.wackymole.util

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.pstorli.wackymole.logError
import com.pstorli.wackymole.util.Consts.TAG

/**
 * Secure / Encrypted shared prefs.
 */
class MolePrefs (application: Application) {

    lateinit var sharedPreferences: SharedPreferences

    init {
        loadSharedPreference(application)
    }

    /**
     * Set up shared prefs.
     */
    private fun loadSharedPreference(application: Application): SharedPreferences {
        try {
            sharedPreferences = EncryptedSharedPreferences.create(
                application,
                TAG,
                getMasterKey(application),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
        catch (ex: Exception) {
            ex.logError()
        }

        return sharedPreferences
    }

    /**
     * Save a preference
     */
    fun setPref (key:String, value:String)
    {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    /**
     * Save a preference
     */
    fun setPref (key:String, value:Int)
    {
        sharedPreferences.edit()
            .putInt(key, value)
            .apply()
    }

    /**
     * Save a preference
     */
    fun setPref (key:String, value:Boolean)
    {
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    /**
     * Get a string
     */
    fun getPref (key:String, defValue: String = ""): String
    {
        var result = defValue
        if (sharedPreferences.contains(key)) {
            sharedPreferences.getString(key, defValue).let {
                result = it!!
            }
        }
        return result
    }

    /**
     * Get an int pref.
     */
    fun getPref (key:String, defValue: Int=0): Int
    {
        var result = defValue
        if (sharedPreferences.contains(key)) {
            result = sharedPreferences.getInt(key, defValue)
        }
        return result
    }

    /**
     * Get an int pref.
     */
    fun getPref (key:String, defValue: Boolean=false): Boolean
    {
        var result = defValue
        if (sharedPreferences.contains(key)) {
            result = sharedPreferences.getBoolean(key, defValue)
        }
        return result
    }

    /**
     * Get the master key.
     */
    private fun getMasterKey(application: Application): MasterKey {
        return MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }
}