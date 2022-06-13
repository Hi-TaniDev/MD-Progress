package com.example.hitani.preferences

import android.content.Context

internal class UserPreferences (context: Context){
    companion object{
        const val PREF_NAME = "user_pref"
        const val EMAIL_KEY = "email"
        const val TOKEN = "token"
        const val IS_LOGIN ="state"
    }

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val editor = preferences.edit()

    fun put(key: String, value: String){
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String{
        return preferences.getString(key, null).toString()
    }
    fun put(key:String, value: Boolean){
        editor.putBoolean(key,value)
        editor.apply()
    }
    fun getBoolean(key: String): Boolean{
        return preferences.getBoolean(key, false)
    }
    fun logOut(){
        editor.clear()
        editor.apply()
    }
}