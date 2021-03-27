package com.arkavyapar.View.UI

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.App
import com.arkavyapar.Constant.Animation
import com.arkavyapar.Constant.Constants
import com.arkavyapar.Model.BuyerLocationInMapModel
import com.arkavyapar.Model.MapUserFilter
import com.arkavyapar.Model.ReponseModel.*
import com.arkavyapar.Model.UserDetailsInfoMap
import com.arkavyapar.R
import com.arkavyapar.Utils.*
import com.arkavyapar.Utils.Loader.LocalModel
import com.arkavyapar.View.Adapter.SellerBeyerLocation.MapFilterCallback
import com.arkavyapar.View.Adapter.SellerBeyerLocation.SellBYELocation
import com.arkavyapar.View.Interface.AlertTask
import com.arkavyapar.View.UI.Fragment.ProfileFragment
import com.arkavyapar.View.UI.Fragment.SellerDashBoard
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class DashBoardActivity : AppCompatActivity() , View.OnClickListener ,
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    BottomNavigationView.OnNavigationItemSelectedListener {
    var userDetails = Arrays.asList(StringUtils.I_am_Buyer, StringUtils.I_am_Seller)
    private var myCurrentLocation: LatLng = LatLng(22.11, 88.11)
    private var requestCall: Call<ByerLocation> ? = null
    private var googleApiClient: GoogleApiClient ? = null
    private var updater: Runnable? = null
    var drawer_layout: DrawerLayout? = null
    var openNavigationDraw: ImageView? = null
    var userCountList:RecyclerView?=null
    var container: LinearLayout? = null
    var container_map: RelativeLayout? = null
    lateinit var mActivity: Activity
    var userProfileIV: CircleImageView? = null
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    var userNameTV: TextView? = null
    var userRollTV: TextView? = null
    var searchView:RelativeLayout?=null
    var menu_listingProduct: TextView? = null
    var menu_notifications: TextView? = null
    var menu_Transaction: TextView? = null
    var menu_BuyerList: TextView? = null
    var openMapView:ImageView? = null
    var menu_ProfileSettings: TextView? = null
    var menu_CustomerCare: TextView? = null
    var menu_Logout: TextView? = null
    var todyRateView: LinearLayout? = null
    var side_nav_view: NavigationView? = null
    private var locationManager: LocationManager? = null
    //Floating Card
    var card_tag:ImageView?=null
    var floatingCardView: CardView? = null
    var userName_floatCard: TextView? = null
    var role_floatCard: TextView? = null
    var Addrrss_floatCard: TextView? = null
    var ChangeLocation: TextView? = null
    var floatingActionButton: FloatingActionButton? = null
    var bottomNavigation: CurvedBottomNavigationView? = null
    private lateinit var mMap: GoogleMap
    var isMarkerSet:Boolean=false
    var buyerListByLocation = ArrayList<BuyerLocationInMapModel>()
    var card_profilePictureIV: ImageView? = null
    var card_userName: TextView? = null
    var card_addressTV: TextView? = null
    var card_UserRoal: TextView? = null
    var card_callUser: TextView? = null
    var markerInfoWindow:RelativeLayout? = null
    var card_closeDialog:ImageView? = null
    private var autocompleteFragment: AutocompleteSupportFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        iniView()
        getMyCurrentLocation(false, true)
        startTimerThread()
        hitgetUser(
            true, App.instance!!.mPrefs!!.getString(
                StringUtils.userID,
                ""
            ).toString(), false
        )

        Animation.setAnimation(mActivity)
        container_map!!.visibility=View.GONE
        ReplaceFragments(SellerDashBoard.newInstance(myCurrentLocation))
    }


    private fun iniView() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        drawer_layout =  findViewById(R.id.drawer_layout);
        openNavigationDraw = findViewById(R.id.openNavigationDraw);
        container =  findViewById(R.id.container);
        container_map =  findViewById(R.id.container_map);
        side_nav_view = findViewById(R.id.side_nav_view);
        userProfileIV =  findViewById(R.id.userProfileIV);
        userNameTV =  findViewById(R.id.userNameTV);
        openMapView =  findViewById(R.id.openMapViewLocation);
        openMapView!!.setOnClickListener(this)
        userRollTV =  findViewById(R.id.userRollTV);
        searchView =  findViewById(R.id.searchView);
        userCountList =  findViewById(R.id.userCountList);
        Constants.setLayoutManager(userCountList!!, true, false)
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
        card_tag=  findViewById(R.id.card_tag);
        role_floatCard =  findViewById(R.id.userRoalrole_floatCard);
        role_floatCard!!.setOnClickListener(this)
        Addrrss_floatCard =  findViewById(R.id.Addrrss_floatCard);
        ChangeLocation =  findViewById(R.id.ChangeLocation);
        ChangeLocation!!.setOnClickListener(this)
        //Floating Card
        floatingActionButton =  findViewById(R.id.floatingActionButton);
        floatingActionButton!!.setOnClickListener(this)
        bottomNavigation =  findViewById(R.id.bottomNavigation);
        bottomNavigation!!.setOnNavigationItemSelectedListener(this)
        //MapCard
        card_profilePictureIV =  findViewById(R.id.card_profilePictureIV);
        card_userName =  findViewById(R.id.card_userName);
        card_addressTV =  findViewById(R.id.card_addressTV);
        card_UserRoal =  findViewById(R.id.card_UserRoal);
        card_callUser =  findViewById(R.id.card_callUser);
        markerInfoWindow =  findViewById(R.id.markerInfoWindow);
        card_closeDialog =  findViewById(R.id.card_closeDialog);
        card_closeDialog!!.setOnClickListener(this)
        val APIKEY: String = "AIzaSyAIbjK-LlK7ltu_ycyCYRY96kb2JspDMj8"
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, APIKEY)
        }
        val placesClient: PlacesClient = Places.createClient(this)
        autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
    }

    private fun userUIPermissions(userType: String) {
      /*  if (userType.equals(StringUtils.I_am_Seller)){
            menu_listingProduct!!.visibility = View.VISIBLE
            menu_BuyerList!!.visibility = View.VISIBLE
            floatingActionButton!!.visibility = View.VISIBLE
        }

        if (userType.equals(StringUtils.I_am_Buyer)){
            menu_listingProduct!!.visibility = View.GONE
            menu_BuyerList!!.visibility = View.GONE
            floatingActionButton!!.visibility = View.GONE

        }*/
    }


    fun openNavigationDraw(view: View) {drawer_layout!!.openDrawer(Gravity.LEFT);}
  /* */
    override fun onClick(v: View?) {
       when(v!!.id){
           R.id.menu_ProfileSettings->{
               drawer_layout!!.closeDrawer(GravityCompat.START);
               ReplaceFragments(ProfileFragment.newInstance())
           }
           R.id.menu_Transaction->{
               Utils.launchActivity(this@DashBoardActivity,TransactionDetails::class.java)
           }
           R.id.menu_BuyerList->{
               Utils.launchActivity(this@DashBoardActivity,MyRequestList::class.java)
           }
           R.id.ChangeLocation -> {
               val fields = Arrays.asList(
                   Place.Field.ID,
                   Place.Field.NAME,
                   Place.Field.ADDRESS,
                   Place.Field.LAT_LNG
               )
               val df = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                   .build(this@DashBoardActivity)
               startActivityForResult(df, AUTOCOMPLETE_REQUEST_CODE)
           }
           R.id.userRoalrole_floatCard -> {
               PopupMenu(v.context, v).apply {
                   menuInflater.inflate(R.menu.role_menu, menu)
                   setOnMenuItemClickListener { item ->
                       role_floatCard!!.setText(item.title)
                       if (item.title.equals("Buyer")) {
                           hitUpdateUserDetails(true, false, "1")
                       } else {
                           hitUpdateUserDetails(true, false, "2")
                       }


                       true
                   }
               }.show()

           }
           R.id.card_closeDialog -> {
               markerInfoWindow!!.visibility = View.GONE
           }
           R.id.openMapViewLocation -> {

               searchView!!.visibility = View.GONE
               container_map!!.visibility = View.VISIBLE
           }
           R.id.menu_Logout -> {

               Constants.showDialog(
                   mActivity,
                   "Are You Sure?" + "\n\n" + "Do you really want to log out?",
                   object :
                       AlertTask {
                       override fun doInPositiveClick() {
                           Utils.launchActivityWithFinish(mActivity, LoginActivity::class.java)
                           App.instance!!.mPrefs!!.setBoolean(StringUtils.loginStatus, false)
                       }

                       override fun doInNegativeClick() {
                       }
                   })


           }
           R.id.menu_listingProduct -> {
               Utils.launchActivity(mActivity, MyProductListing::class.java)
           }
           R.id.floatingActionButton -> {
               Utils.launchActivity(mActivity, MyProductListing::class.java)
           }

       }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode === RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                val location = Location("")
                location.latitude = place.latLng!!.latitude
                location.longitude = place.latLng!!.longitude
                hitUpdate(location)
                var details = Constants.getAllAddress(LatLng(location.latitude, location.longitude))
                var jsonObject = JSONObject()
                jsonObject.put("address_1", details.getString("address"))
                jsonObject.put("address_2", details.getString("knownName"))
                jsonObject.put("latitude", place.latLng!!.latitude)
                jsonObject.put("longitude", place.latLng!!.longitude)
                jsonObject.put("pincode", details.getString("postalCode"))
                jsonObject.put("city", details.getString("city"))
                jsonObject.put("state", details.getString("state"))
                hitUpdateUserDetails(false, true, jsonObject.toString());
            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
            } else if (resultCode === RESULT_CANCELED) {
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun hitUpdateUserDetails(userType: Boolean, location: Boolean, values: String) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart(
            "userID", App.instance!!.mPrefs!!.getString(
                StringUtils.userID,
                ""
            ).toString()
        )
        if (userType)
        builder.addFormDataPart("userType", values)
        if (location){
            builder.addFormDataPart("address_1", JSONObject(values).getString("address_1"))
            builder.addFormDataPart("address_2", JSONObject(values).getString("address_2"))
            builder.addFormDataPart("latitude", JSONObject(values).getString("latitude"))
            builder.addFormDataPart("longitude", JSONObject(values).getString("longitude"))
            builder.addFormDataPart("pincode", JSONObject(values).getString("pincode"))
            builder.addFormDataPart("city", JSONObject(values).getString("city"))
            builder.addFormDataPart("state", JSONObject(values).getString("state"))
        }

        LocalModel.instance!!.showProgressDialog(this@DashBoardActivity, "Loading..")
        val requestCall: Call<CommonModel> =
            App.instance!!.apiInterface!!.hitupdateuserApi(builder.build())
        requestCall.enqueue(object : Callback<CommonModel> {
            override fun onResponse(call: Call<CommonModel>, response: Response<CommonModel>) {
                if (response.body() != null) {
                    LocalModel.instance!!.cancelProgressDialog()
                    if (response.body()?.success.toString().trim().equals("1")) {
                        if (values.equals("1")) {
                            userRollTV!!.setText(StringUtils.I_am_Buyer)
                            App!!.instance!!.mPrefs!!.setString(
                                StringUtils.userRole,
                                StringUtils.I_am_Buyer
                            )
                            card_tag!!.setBackgroundResource(R.drawable.ic_blue_tag)
                            role_floatCard!!.text = StringUtils.I_am_Buyer
                            userUIPermissions(StringUtils.I_am_Buyer)
                        } else {
                            App!!.instance!!.mPrefs!!.setString(
                                StringUtils.userRole,
                                StringUtils.I_am_Seller
                            )
                            card_tag!!.setBackgroundResource(R.drawable.ic_yellow_tag)
                            userRollTV!!.setText(StringUtils.I_am_Seller)
                            role_floatCard!!.text = StringUtils.I_am_Seller
                            userUIPermissions(StringUtils.I_am_Seller)
                        }


                        startTimerThread()
                        Utils.launchActivityWithFinish(this@DashBoardActivity, DashBoardActivity::class.java)
                    }

                } else {
                    LocalModel.instance!!.cancelProgressDialog()
                }
            }

            override fun onFailure(call: Call<CommonModel>, t: Throwable) {
                LocalModel.instance!!.cancelProgressDialog()
            }
        })


    }

    @SuppressLint("MissingPermission")
    private fun getMyCurrentLocation(setMarker: Boolean, mapclear: Boolean) {
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
                            setMyLocation(location, setMarker, mapclear)
                            hitUpdate(location)
                        }
                    }).addOnFailureListener(OnFailureListener {
                    LocalModel.instance!!.cancelProgressDialog()
                })
            }
        }else{
            trunOnGPS()
        }

    }

    private fun setMyLocation(location: Location, setMarker: Boolean, mapclear: Boolean) {
        if (mMap!=null){
            try {
                 myCurrentLocation = LatLng(location.latitude, location.longitude)

                if (setMarker){
                    if (mapclear){
                        mMap.clear()
                    }

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
                    mMap.addCircle(circleOptionss)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition))
                    val cameraPosition = CameraPosition.Builder()
                        .target(myCurrentLocation)
                        .zoom(15f).build()
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
        getMyCurrentLocation(true, true)
        getAllBuyerLocation()
        mMap.setOnMarkerClickListener(this);
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
                                    getMyCurrentLocation(true, true)
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

    private fun hitUpdate(location: Location){
        val requestCall: Call<CommonModel> = App.instance!!.apiInterface!!.updateLocation(
            App.instance!!.mPrefs!!.getString(
                StringUtils.userID,
                ""
            ).toString(), location.latitude.toString(), location.longitude.toString()
        )
        requestCall.enqueue(object : Callback<CommonModel> {
            override fun onResponse(call: Call<CommonModel>, response: Response<CommonModel>) {
                if (response.body() != null) {
                }
            }

            override fun onFailure(call: Call<CommonModel>, t: Throwable) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        hitgetUser(
            false,
            App.instance!!.mPrefs!!.getString(StringUtils.userID, "").toString(),
            false
        )
        getAllBuyerLocation()
    }

    private fun hitgetUser(isLoderShow: Boolean, userid: String, mapOnClick: Boolean){
        if (isLoderShow){
            LocalModel.instance!!.showProgressDialog(mActivity, "Getting location....")
        }

        val requestCall: Call<UserDetails> = App.instance!!.apiInterface!!.userdetails(userid)
        requestCall.enqueue(object : Callback<UserDetails> {
            override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {
                if (response.body() != null) {
                    LocalModel.instance!!.cancelProgressDialog()
                    if (response.body()?.success.toString().trim().equals("1")) {
                        if (mapOnClick) {
                            showUserDetailsCard(response)
                        }

                        userNameTV!!.setText(response.body()?.userdetails!!.USERNAME)
                        Addrrss_floatCard!!.setText(response.body()?.userdetails!!.ADDRESS1)
                        if (response.body()?.userdetails!!.USERTYPE.equals("1")) {
                            userRollTV!!.setText(StringUtils.I_am_Buyer)
                            App!!.instance!!.mPrefs!!.setString(
                                StringUtils.userRole,
                                StringUtils.I_am_Buyer
                            )
                            card_tag!!.setImageDrawable(resources.getDrawable(R.drawable.ic_blue_tag))
                            role_floatCard!!.text = StringUtils.I_am_Buyer
                            userUIPermissions(StringUtils.I_am_Buyer)
                        } else {
                            App!!.instance!!.mPrefs!!.setString(
                                StringUtils.userRole,
                                StringUtils.I_am_Seller
                            )
                            card_tag!!.setImageDrawable(resources.getDrawable(R.drawable.ic_yellow_tag))
                            userRollTV!!.setText(StringUtils.I_am_Seller)
                            role_floatCard!!.text = StringUtils.I_am_Seller
                            userUIPermissions(StringUtils.I_am_Seller)
                        }
                        if (!response.body()?.userdetails!!.Profileverified.equals("")) {
                            if (response.body()?.userdetails!!.Profileverified.equals("true")) {
                                card_tag!!.visibility = View.VISIBLE
                            } else {
                                card_tag!!.visibility = View.GONE
                            }
                        } else {
                            card_tag!!.visibility = View.GONE
                        }


                        userName_floatCard!!.setText(response.body()?.userdetails!!.USERNAME)
                        Utils.setImageFromUrl(
                            userProfileIV!!,
                            response.body()?.userdetails!!.PROFILEPIC!!, this@DashBoardActivity
                        )

                    }
                }
            }

            override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                LocalModel.instance!!.cancelProgressDialog()
            }
        })
    }

    private fun showUserDetailsCard(response: Response<UserDetails>) {
        markerInfoWindow!!.visibility = View.VISIBLE
        Utils.setImageFromUrl(
            card_profilePictureIV!!,
            response.body()?.userdetails!!.PROFILEPIC!!, this@DashBoardActivity
        )
        card_userName!!.setText(response.body().userdetails.USERNAME)
        card_addressTV!!.setText(response.body().userdetails.ADDRESS1)
        card_callUser!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val callIntent = Intent(Intent.ACTION_CALL) //use ACTION_CALL class

                callIntent.data =
                    Uri.parse("tel:" + response.body().userdetails.PHONENO) //this is the phone number calling
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        applicationContext as Activity, arrayOf(
                            Manifest.permission.CALL_PHONE
                        ), 10
                    )
                    return
                } else {
                    try {
                        startActivity(callIntent) //call activity and make phone call
                    } catch (ex: ActivityNotFoundException) {
                    }
                }
            }

        })

        if (response.body()?.userdetails!!.USERTYPE.equals("1")) {
            card_UserRoal!!.setText(StringUtils.I_am_Buyer)
            App!!.instance!!.mPrefs!!.setString(
                StringUtils.userRole,
                StringUtils.I_am_Buyer
            )
        } else {
            App!!.instance!!.mPrefs!!.setString(
                StringUtils.userRole,
                StringUtils.I_am_Seller
            )
            card_UserRoal!!.setText(StringUtils.I_am_Seller)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                searchView!!.visibility = View.VISIBLE
                container_map!!.visibility = View.GONE
                ReplaceFragments(SellerDashBoard.newInstance(myCurrentLocation))
            }

            R.id.profile -> {
                searchView!!.visibility = View.GONE
                container_map!!.visibility = View.GONE
                ReplaceFragments(ProfileFragment.newInstance())

            }
            R.id.settings -> {

            }
        }
        return true;
    }

    private fun ReplaceFragments(newInstance: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, newInstance).commit();
    }

    override fun onBackPressed() {
        Constants.showDialog(
            mActivity,
            "Are You Sure?" + "\n\n" + "Do you really want to exit from Arka Vyaper?",
            object :
                AlertTask {
                override fun doInPositiveClick() {
                    finish()
                }

                override fun doInNegativeClick() {
                }
            })
    }

    private fun getAllBuyerLocation() {
        try {
            
            if (App.instance!!.mPrefs!!.getString(StringUtils.userRole, "").equals(StringUtils.I_am_Buyer)){

                requestCall = App.instance!!.apiInterface!!.getAllSellerLoca()
            }else if (App.instance!!.mPrefs!!.getString(StringUtils.userRole, "").equals(StringUtils.I_am_Seller)){
                requestCall = App.instance!!.apiInterface!!.getAllBuyerLocation()
            }
           
            requestCall!!.enqueue(object : Callback<ByerLocation> {
                override fun onResponse(
                    call: Call<ByerLocation>,
                    response: Response<ByerLocation>
                ) {
                    if (response.body() != null) {
                        LocalModel.instance!!.cancelProgressDialog()
                        if (response.body()?.success.toString().trim().equals("1")) {
                            try {
                                buyerListByLocation!!.clear()
                                for (i in 0 until response.body()?.Location_List!!.size) {
                                    var byerList = BuyerLocationInMapModel()
                                    byerList.Latitude =
                                        response.body()?.Location_List!!.get(i).Latitude
                                    byerList.Longitude =
                                        response.body()?.Location_List!!.get(i).Longitude
                                    byerList.UserId = response.body()?.Location_List!!.get(i).UserId
                                    byerList.Name = response.body()?.Location_List!!.get(i).UserName
                                    buyerListByLocation.add(byerList)
                                }
                                //Ploat All The Seller Or Byer Depanding ON user Role
                                buyerLocationPlotInMap(buyerListByLocation)

                            } catch (e: Exception) {
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ByerLocation>, t: Throwable) {}
            })
        } catch (e: Exception) {
        }
    }

    private fun buyerLocationPlotInMap(buyerListByLocation: ArrayList<BuyerLocationInMapModel>) {
        for (i in  0 until  buyerListByLocation.size) {
            var model = buyerListByLocation[i]
            var currentPosition = LatLng(model.Latitude!!.toDouble(), model.Longitude!!.toDouble())
             var marker: Int = R.drawable.ic_user_marker
            if (App.instance!!.mPrefs!!.getString(StringUtils.userRole, "").equals(StringUtils.I_am_Buyer)){
                marker =  R.drawable.ic_seller_marker
            }else if (App.instance!!.mPrefs!!.getString(StringUtils.userRole, "").equals(StringUtils.I_am_Seller)){
                   marker =   R.drawable.ic_user_marker
            }

         var marker_ =    mMap.addMarker(
             MarkerOptions()
                 .position(currentPosition)
                 .title(model.Name)
                 .icon(BitmapDescriptorFactory.fromResource(marker))
                 .draggable(false)
         )

            marker_.title=model.UserId
        }
        //Distance List
        val distanceFromMe: List<Int> = Arrays.asList(5, 15, 30, 45, 60, 75, 100)
        var filterList = ArrayList<MapUserFilter>()
        for (i in  0 until  distanceFromMe.size) {
            var listMapDetails = ArrayList<UserDetailsInfoMap>()
            for (j in  0 until  buyerListByLocation.size) {
                var userLatlang = LatLng(
                    buyerListByLocation.get(j).Latitude!!.toDouble(), buyerListByLocation.get(
                        j
                    ).Longitude!!.toDouble()
                )

                if (i!=0){
                    if (distanceFromMe.get(i)>=calculateDistanceInKM(userLatlang, myCurrentLocation) &&
                        distanceFromMe.get(i - 1)<calculateDistanceInKM(
                            userLatlang,
                            myCurrentLocation
                        )){
                        var model = UserDetailsInfoMap()
                        model.latitude = userLatlang.latitude.toString()
                        model.longitude = userLatlang.longitude.toString()
                        model.name = buyerListByLocation.get(j).Name
                        model.userID = buyerListByLocation.get(j).UserId
                        listMapDetails!!.add(model)
                    }
                }else{
                    if (distanceFromMe.get(i)>=calculateDistanceInKM(userLatlang, myCurrentLocation)){
                        var model = UserDetailsInfoMap()
                        model.latitude = userLatlang.latitude.toString()
                        model.longitude = userLatlang.longitude.toString()
                        model.name = buyerListByLocation.get(j).Name
                        model.userID = buyerListByLocation.get(j).UserId
                        listMapDetails!!.add(model)
                    }
                }



            }
            var modelMap = MapUserFilter()
            modelMap.distanceInKM = distanceFromMe.get(i).toString()
            modelMap.list = listMapDetails
            filterList.add(modelMap)
        }
        if (filterList.size>0){
            var adapter = SellBYELocation(
                filterList,
                this@DashBoardActivity,
                object : MapFilterCallback {
                    override fun showUserToMap(list: java.util.ArrayList<UserDetailsInfoMap>) {
                        buyerLocationPlotInMapFilter(list)
                    }

                })
            userCountList!!.adapter = adapter;
        }

    }

    private fun buyerLocationPlotInMapFilter(list: ArrayList<UserDetailsInfoMap>) {
        if (mMap!=null){
            mMap.clear()
            getMyCurrentLocation(true, false)
            for (i in  0 until  list.size) {
                var model = list[i]
                var currentPosition = LatLng(
                    model.latitude!!.toDouble(),
                    model.longitude!!.toDouble()
                )
                var marker: Int = R.drawable.ic_user_marker
                if (App.instance!!.mPrefs!!.getString(StringUtils.userRole, "").equals(StringUtils.I_am_Buyer)){
                    marker =  R.drawable.ic_seller_marker
                }else if (App.instance!!.mPrefs!!.getString(StringUtils.userRole, "").equals(
                        StringUtils.I_am_Seller
                    )){
                    marker =   R.drawable.ic_user_marker
                }

         var marker_ =   mMap.addMarker(
             MarkerOptions()
                 .position(currentPosition)
                 .title(model.name)
                 .icon(BitmapDescriptorFactory.fromResource(marker))
                 .draggable(false)
         )

                marker_.title=model.userID


            }
        }
    }


    fun calculateDistanceInKM(latlang_source: LatLng, latlang_distance: LatLng):Double{
        val loc1 = Location("")
        loc1.latitude = latlang_source.latitude
        loc1.longitude = latlang_source.longitude
        val loc2 = Location("")
        loc2.latitude = latlang_distance.latitude
        loc2.longitude = latlang_distance.longitude
        return loc1.distanceTo(loc2)*0.001
    }

    override fun onMarkerClick(marker: Marker?): Boolean {

        if (marker!!.title!=null){
            var UserID = marker.title.toString()

            hitgetUser(true, UserID, true)
        }


        return true
    }






}