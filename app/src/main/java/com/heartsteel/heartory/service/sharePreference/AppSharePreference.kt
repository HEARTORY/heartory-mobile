package com.heartsteel.heartory.service.sharePreference

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.heartsteel.heartory.service.model.domain.User

class AppSharePreference @Inject constructor(private val context: Context) {
    companion object{
        const val APP_SHARE_KEY = "com.heartsteel.heartory"
    }

    val prefs = getSharedPreferences()!!
    private fun getSharedPreferences(): SharedPreferences?{
        return context.getSharedPreferences(APP_SHARE_KEY,Context.MODE_PRIVATE)
    }

    private fun encode(normalText: String): String {
        return Base64.encodeToString(normalText.toByteArray(), Base64.DEFAULT)
    }

    private fun decode(encodedText: String): String {
        return String(Base64.decode(encodedText.toByteArray(), Base64.DEFAULT))
    }

    //    Boolean
    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean = prefs.getBoolean(key, defValue)

    //    Float
    fun putFloat(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    fun getFloat(key: String, defValue: Float): Float = prefs.getFloat(key, defValue)

    //    Int
    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defValue: Int): Int = prefs.getInt(key, defValue)


    //    Long
    fun putLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun getLong(key: String, defValue: Long): Long = prefs.getLong(key, defValue)


    //    String
    fun putString(key: String, value: String) {
        prefs.edit().putString(key, encode(value)).apply()
    }

    fun getString(key: String, defValue: String): String =
        decode(prefs.getString(key, defValue) ?: "")


    //    model
    fun <T> putObject(key: String, `object`: T?) {
        if (`object` != null) {
            prefs.edit().putString(key, encode(Gson().toJson(`object`))).apply()
        } else {
            prefs.edit().putString(key, "").apply()
        }
    }

    fun <T> getObject(key: String, classOff: Class<T>): T? {
        val jsonObjects = decode(prefs.getString(key, "") ?: "")
        return if (jsonObjects.isNotEmpty()) {
            Gson().fromJson(jsonObjects, classOff)
        } else {
            null
        }
    }

    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    fun saveUser(user: User) {
        putObject("user", user)
    }
    fun getUser(): User?{
        val user = getObject("user", User::class.java)
        Log.d("UserRepository", "User get: ${user}")
        return user
    }

    fun isLoggedIn(): Boolean {
        return getObject("user", User::class.java) != null
    }

    fun clearUser() {
        remove("user")
    }

}