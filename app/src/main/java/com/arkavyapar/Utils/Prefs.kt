package com.arkavyapar.Utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.preference.PreferenceManager

class Prefs {

    var context:Context?=null
    var prefs:SharedPreferences?=null


    constructor(mcontext: Context){
        context=mcontext
        prefs =  PreferenceManager.getDefaultSharedPreferences(context);
    }

    fun getString(key: String, def: String): String? {
        return prefs!!.getString(key, def)
    }

     fun getInt(key: String, def: Int): Int {
        return prefs!!.getString(key, Integer.toString(def))!!.toInt()
    }

     fun getFloat(key: String, def: Float): Float {
        return prefs!!.getString(key, java.lang.Float.toString(def))!!.toFloat()
    }

     fun getLong(key: String, def: Long): Long {
        return prefs!!.getString(key, java.lang.Long.toString(def))!!.toLong()
    }

     fun setString(key: String, `val`: String) {
        val e: Editor = prefs!!.edit()
        e.putString(key, `val`)
        e.commit()
    }

     fun setBoolean(key: String, `val`: Boolean) {
        val e: Editor = prefs!!.edit()
        e.putBoolean(key, `val`)
        e.commit()
    }

     fun setInt(key: String, `val`: Int) {
        val e: Editor = prefs!!.edit()
        e.putString(key, Integer.toString(`val`))
        e.commit()
    }

     fun setLong(key: String, `val`: Long) {
        val e: Editor = prefs!!.edit()
        e.putString(key, java.lang.Long.toString(`val`))
        e.commit()
    }

     fun getBoolean(key: String, def: Boolean): Boolean {
        return prefs!!.getBoolean(key, def)
    }

     fun getStringValue(key: String, `val`: String): String? {
        return prefs!!.getString(key, `val`)
    }


}