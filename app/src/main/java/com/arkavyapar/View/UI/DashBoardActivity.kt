package com.arkavyapar.View.UI

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.arkavyapar.App
import com.arkavyapar.Constant.Animation
import com.arkavyapar.Constant.Constants
import com.arkavyapar.R
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.Utils.StringUtils
import com.arkavyapar.Utils.ToastUtils
import com.arkavyapar.Utils.Utils
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject


class DashBoardActivity : AppCompatActivity() , View.OnClickListener ,OnMapReadyCallback {
    private var googleApiClient: GoogleApiClient ? = null
    private var updater: Runnable? = null
    var drawer_layout: DrawerLayout? = null
    var openNavigationDraw: ImageView? = null
    var container: LinearLayout? = null
    lateinit var mActivity: Activity
    var userProfileIV: CircleImageView? = null
    var userNameTV: TextView? = null
    var userRollTV: TextView? = null
    var menu_listingProduct: TextView? = null
    var menu_notifications: TextView? = null
    var menu_Transaction: TextView? = null
    var menu_BuyerList: TextView? = null
    var menu_ProfileSettings: TextView? = null
    var menu_CustomerCare: TextView? = null
    var menu_Logout: TextView? = null
    var todyRateView: LinearLayout? = null
    var side_nav_view: NavigationView? = null
    private var locationManager: LocationManager? = null
    //Floating Card
    var floatingCardView: CardView? = null
    var userName_floatCard: TextView? = null
    var role_floatCard: TextView? = null
    var Addrrss_floatCard: TextView? = null
    var ChangeLocation: TextView? = null
    private lateinit var mMap: GoogleMap
    var isMarkerSet:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        iniView()
        getMyCurrentLocation(false)
        setData()
        startTimerThread()
        Animation.setAnimation(mActivity)
    }

    private fun setData() {
        LocalModel.instance!!.showProgressDialog(mActivity, "")
        try{
            var details = JSONObject(
                App.instance!!.mPrefs!!.getString(
                    StringUtils.signUpBundel,
                    ""
                )
            )
            userNameTV!!.setText(details.getString(StringUtils.go_fullName))
            userName_floatCard!!.setText(details.getString(StringUtils.go_fullName))
            userProfileIV!!.setImageBitmap(convertBase64ToBitmap(details.getString(StringUtils.profilePicture)))
            userRollTV!!.setText(details.getString(StringUtils.userRole))
            Addrrss_floatCard!!.setText(details.getString(StringUtils.go_address_1))
            role_floatCard!!.text=details.getString(StringUtils.userRole)
            LocalModel.instance!!.cancelProgressDialog()
        }catch (E: java.lang.Exception){
            LocalModel.instance!!.cancelProgressDialog()
        }
    }

    private fun iniView() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        drawer_layout =  findViewById(R.id.drawer_layout);
        openNavigationDraw = findViewById(R.id.openNavigationDraw);
        container =  findViewById(R.id.container);
        side_nav_view = findViewById(R.id.side_nav_view);
        userProfileIV =  findViewById(R.id.userProfileIV);
        userNameTV =  findViewById(R.id.userNameTV);
        userRollTV =  findViewById(R.id.userRollTV);
        //Menu
        menu_listingProduct =  findViewById(R.id.menu_listingProduct);
        menu_listingProduct!!.setOnClickListener(this)
        menu_notifications =  findViewById(R.id.menu_notifications);
        menu_notifications!!.setOnClickListener(this)
        menu_Transaction =  findViewById(R.id.menu_Transaction);
        menu_Transaction!!.setOnClickListener(this)
        menu_BuyerList =  findViewById(R.id.menu_BuyerList);
        menu_BuyerList!!.setOnClickListener(this)
        menu_ProfileSettings =  findViewById(R.id.menu_ProfileSettings);
        menu_ProfileSettings!!.setOnClickListener(this)
        menu_CustomerCare =  findViewById(R.id.menu_CustomerCare);
        menu_CustomerCare!!.setOnClickListener(this)
        menu_Logout  =  findViewById(R.id.menu_Logout);
        menu_Logout!!.setOnClickListener(this)
        //Menu
        todyRateView =  findViewById(R.id.todyRateView);
        mActivity = this@DashBoardActivity
        //Floating Card
        floatingCardView =  findViewById(R.id.floatingCardView);
        floatingCardView!!.setOnClickListener(this)
        userName_floatCard =  findViewById(R.id.userName_floatCard);
        role_floatCard =  findViewById(R.id.userRoalrole_floatCard);
        Addrrss_floatCard =  findViewById(R.id.Addrrss_floatCard);
        ChangeLocation =  findViewById(R.id.ChangeLocation);
        ChangeLocation!!.setOnClickListener(this)
        //Floating Card
    }

    fun openNavigationDraw(view: View) {drawer_layout!!.openDrawer(Gravity.LEFT);
    }

    override fun onClick(v: View?) {
       when(v!!.id){
           R.id.menu_Logout -> {
               Utils.launchActivityWithFinish(mActivity, LoginActivity::class.java)
               App.instance!!.mPrefs!!.setBoolean(StringUtils.loginStatus, false)
           }
       }
    }
    private fun convertBase64ToBitmap(b64: String): Bitmap? {
        val imageAsBytes: ByteArray = Base64.decode(b64.toByteArray(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    }
    private fun getMyCurrentLocation(setMarker: Boolean) {
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                App.instance!!.mFusedLocationProviderClient!!.getLastLocation().addOnSuccessListener(
                    this,
                    OnSuccessListener<Location?> { location ->
                        if (location != null) {
                            setMyLocation(location, setMarker)

                        }
                    }).addOnFailureListener(OnFailureListener {
                    LocalModel.instance!!.cancelProgressDialog()
                })
            }
        }else{
            trunOnGPS()
        }

    }

    private fun setMyLocation(location: Location, setMarker: Boolean) {
        if (mMap!=null){
            try {
                var position = LatLng(location.latitude, location.longitude)

                if (setMarker){
                    mMap.clear()
                    var currentPosition = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(currentPosition)
                            .title("HERE I AM!!")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.here_i_am))
                            .draggable(false)
                    )
                    val circleOptions = CircleOptions()
                        .center(currentPosition)
                        .radius(500.0)
                        .strokeWidth(0f)
                        .strokeColor(Color.TRANSPARENT)
                        .fillColor(Color.parseColor("#2495D03F"))
                    val circleOptionss = CircleOptions()
                        .center(currentPosition)
                        .radius(250.0)
                        .strokeWidth(0f)
                        .strokeColor(Color.TRANSPARENT)
                        .fillColor(Color.parseColor("#5995D03F"))
                    // mMap.addCircle(circleOptions)
                    mMap.addCircle(circleOptions)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition))
                    val cameraPosition = CameraPosition.Builder()
                        .target(position)
                        .zoom(16f).build()
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    isMarkerSet=true
                }else{
                    var Address = Constants.getAllAddress(
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    )
                    Addrrss_floatCard!!.setText(Address.getString("address"))
                }
            } catch (e: Exception) {
            }
        }

    }

    private fun trunOnGPS() {
        ToastUtils.longToast("PLEASE TURN ON GPS IN HIGH ACCURACY")
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        getMyCurrentLocation(true)

    }


    private fun startTimerThread() {
        try {
            val thread: Thread = object : Thread() {
                override fun run() {
                    try {
                        while (!this.isInterrupted) {
                            sleep(10000)
                            runOnUiThread {
                                if (!isMarkerSet) {
                                    getMyCurrentLocation(true)
                                }
                            }
                        }
                    } catch (e: InterruptedException) {
                    }
                }
            }

            thread.start()
        } catch (e: Exception) {
        }

    }
}