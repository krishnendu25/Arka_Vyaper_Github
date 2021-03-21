package com.arkavyapar

import android.app.Application
import android.content.Context
import android.os.Process
import androidx.multidex.MultiDex
import com.arkavyapar.Utils.Prefs
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.hoori.controller.ApiClient
import com.arkavyapar.hoori.controller.ApiInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException


class App : Application() {

    public var apiInterface: ApiInterface? = null
    public var mPrefs: Prefs?=null
    public var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    override fun onCreate() {
        super.onCreate()
        initApplication()
        val pid = Process.myPid()
        val whiteList = "logcat -P '$pid'"
        try {
            Runtime.getRuntime().exec(whiteList).waitFor()
        } catch (e: IOException) {
        } catch (e: Exception) {
        }
        apiInterface = ApiClient.getRetrofit()!!.create(ApiInterface::class.java)
    }

    private fun initApplication() {
        instance = this
        mPrefs= Prefs(instance!!)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    fun getInstance(): App? {
        return instance
    }
    companion object {

        var instance: App? = null
            private set

    }
}