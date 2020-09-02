package yutori.tf.hangul.db

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceController {
    private  var pref: SharedPreferences?=null

    private fun getPref(cont: Context) {
        if (pref == null) {
            pref = cont.getSharedPreferences(SHARED_PREFS_CONFIGURATION, Context.MODE_PRIVATE)
        }
    }

    fun load(context: Context) {
        getPref(context)
    }

    @JvmOverloads
    fun getPrefLongData(key: String, defValue: Long = 0): Long {
        return pref!!.getLong(key, defValue)
    }

    @JvmOverloads
    fun getPrefIntegerData(key: String, defValue: Int = 0): Int {
        return pref!!.getInt(key, defValue)
    }

    @JvmOverloads
    fun getPrefStringData(key: String, defValue: String = ""): String? {
        return pref?.getString(key, defValue)
    }

//    @JvmOverloads
//    fun getPrefBooleanData(key: String, defValue: Boolean = false): Boolean {
//        return pref?.getBoolean(key, defValue)
//    }

    fun setPrefData(key: String, value: Boolean) {
        val editor = pref?.edit()

        editor?.putBoolean(key, value)
        editor?.commit()
    }

    fun setPrefData(key: String, value: Int) {
        val editor = pref?.edit()

        editor?.putInt(key, value)
        editor?.commit()
    }

    fun setPrefData(key: String, value: Long) {
        val editor = pref?.edit()

        editor?.putLong(key, value)
        editor?.commit()
    }

    fun setPrefData(key: String, value: String) {
        val editor = pref?.edit()

        editor?.putString(key, value)
        editor?.apply()
    }


    companion object {
        private val SHARED_PREFS_CONFIGURATION = "GithubConfiguration"

        @Volatile private var sharedPreferencesManager: SharedPreferenceController? = null

        val instance: SharedPreferenceController?
            get() {
                if (sharedPreferencesManager == null) {
                    synchronized(SharedPreferenceController::class.java) {
                        if (sharedPreferencesManager == null)
                            sharedPreferencesManager = SharedPreferenceController()
                    }
                }
                return sharedPreferencesManager
            }
    }
}