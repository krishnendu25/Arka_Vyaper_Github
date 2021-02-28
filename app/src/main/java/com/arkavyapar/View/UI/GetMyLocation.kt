package com.arkavyapar.View.UI

import android.Manifest
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.arkavyapar.App
import com.arkavyapar.Constant.Animation
import com.arkavyapar.Constant.Constants
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.ToastUtils
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener


class GetMyLocation : AppCompatActivity()
{
    lateinit var mActivity: Activity
    private var locationManager: LocationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_my_location)
        iniView()
        Animation.setAnimation(mActivity)
        getMyCurrentLocation()

    }

    private fun iniView() {
        mActivity = this@GetMyLocation
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
    }

    fun setManualAddress(view: View) {
        val intent= Intent(this, AdreessActivity::class.java)
        startActivity(intent)
    }


    private fun getMyCurrentLocation() {
        LocalModel.instance!!.showProgressDialog(mActivity,"Getting location....")
            if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PERMISSION_GRANTED
                ) {
                    App.instance!!.mFusedLocationProviderClient!!.getLastLocation().addOnSuccessListener(
                        this,
                        OnSuccessListener<Location?> { location ->
                            val handler = Handler()
                            val runnable = Runnable {
                                LocalModel.instance!!.cancelProgressDialog()
                                if (location != null) {
                                    locationFetched(location)
                                }
                            }
                            handler.postDelayed(runnable, 3500)
                        }).addOnFailureListener(OnFailureListener {    LocalModel.instance!!.cancelProgressDialog() })
                }
            }else{
                LocalModel.instance!!.cancelProgressDialog()
                trunOnGPS()
            }
     
    }

    private fun locationFetched(location: Location) {
        var Address = Constants.getAllAddress(LatLng(location.latitude,location.longitude))
        val intent= Intent(this, AdreessActivity::class.java)
        intent.putExtra(StringUtils.intent_Address,Address.toString())
        if (Build.VERSION.SDK_INT > 20) {
            val options = ActivityOptions.makeSceneTransitionAnimation(mActivity)
            startActivity(intent, options.toBundle())
        } else {
           startActivity(intent)
        }

    }

    private fun trunOnGPS() {
        ToastUtils.longToast("Please Trun On GPS")
        startActivity( Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    override fun onResume() {
        super.onResume()
        getMyCurrentLocation()
    }
}