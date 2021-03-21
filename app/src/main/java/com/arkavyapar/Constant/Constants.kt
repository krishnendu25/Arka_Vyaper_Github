package com.arkavyapar.Constant

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.text.ClipboardManager
import android.text.TextUtils
import android.util.Base64
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arkavyapar.App
import com.arkavyapar.Model.AddressModel
import com.arkavyapar.R
import com.arkavyapar.View.Interface.AlertTask
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


class Constants {
    companion object obj {
        fun getCurrentUnixTimeStamps(): String? {
            return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
                .toString()
        }
        fun setLayoutManager(
            recyclerView: RecyclerView,
            HORIZONTAL: Boolean,
            VERTICAL: Boolean
        ): LinearLayoutManager? {
            var linearLayoutManager: LinearLayoutManager? = null
            if (HORIZONTAL) {
                linearLayoutManager =
                    LinearLayoutManager(App.instance, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.layoutManager = linearLayoutManager
            } else if (VERTICAL) linearLayoutManager =
                LinearLayoutManager(App.instance, LinearLayoutManager.VERTICAL, false)
            run { recyclerView.layoutManager = linearLayoutManager }
            return linearLayoutManager
        }

        fun getTimeStampTODateString(timeStamp: String?, Sign: String?): String? {
            return try {
                val unixSeconds = java.lang.Long.valueOf(timeStamp!!)
                val date =
                    Date(unixSeconds * 1000L) // *1000 is to convert seconds to milliseconds
                val sdf =
                    SimpleDateFormat(Sign) // the format of your date
                sdf.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        fun getCityNameFromGeo(
            c: Context?,
            latitide: Double,
            longitude: Double
        ): ArrayList<AddressModel>? {
            var tempList: ArrayList<AddressModel> = arrayListOf()
            var strAdd = ""
            val geocoder = Geocoder(c, Locale.getDefault())
            try {
                val addresses =
                    geocoder.getFromLocation(latitide, longitude, 1)
                if (addresses != null) {
                    var addressModel = AddressModel()
                    addressModel.city = addresses[0].locality
                    addressModel.address = addresses[0].getAddressLine(0)
                    tempList.add(addressModel)
                }
            } catch (e: java.lang.Exception) {
            }
            return tempList
        }

        fun getDateToMilliSecound(dateString: String, dateFormat: String): Long {

            var millisecound = 0
            var sdf = SimpleDateFormat(dateFormat)
            try {
                var mDate = sdf.parse(dateString)
                millisecound = mDate.time.toInt()
                return millisecound.toLong()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return millisecound.toLong()
        }

        //Check Device Time Automatic oR Not
        fun isTimeAutomaticOnDevice(c: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Settings.Global.getInt(c.contentResolver, Settings.Global.AUTO_TIME, 0) != 1
            } else {
                Settings.System.getInt(c.contentResolver, Settings.System.AUTO_TIME, 0) != 1
            }
        }

        fun showDialog(activity: Activity?, msg: String?, alertTask: AlertTask) {
            val dialog = Dialog(activity!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.custom_dialog_view)
          var  ic_closePopup: ImageView = dialog.findViewById(R.id.ic_closePopup);
            var  dialogText:TextView =  dialog.findViewById(R.id.dialogText);
            var  closePopup :TextView =  dialog.findViewById(R.id.closePopup);
            dialogText!!.text=msg
            closePopup.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    dialog.dismiss()
                    alertTask.doInPositiveClick()
                }
            })

            ic_closePopup.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    alertTask.doInNegativeClick()
                    dialog.dismiss()
                }
            })
            dialog.show()
        }
        fun showAlertDialog(c: Context?, body: String?, title: String?, alertTask: AlertTask) {
            val alertDialogBuilder: AlertDialog.Builder
            alertDialogBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder(c)
            } else {
                AlertDialog.Builder(c)
            }
            alertDialogBuilder.setTitle(title)
            alertDialogBuilder.setMessage(body)
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                dialog.dismiss()
                alertTask.doInPositiveClick()
            }
            alertDialogBuilder.setNegativeButton("CANCLE") { dialog, which ->
                dialog.dismiss()
                alertTask.doInNegativeClick()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        }
        fun encodeImage(bm: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }
        fun showImageTakeDialog(mActivity: Activity) {


            val alertDialogBuilder: AlertDialog.Builder
            alertDialogBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder(mActivity)
            } else {
                AlertDialog.Builder(mActivity)
            }
            alertDialogBuilder.setTitle("SELECT A PHOTO")
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setPositiveButton(
                "Take Photo"
            ) { dialog, which ->
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                mActivity.startActivityForResult(cameraIntent, AppConstants.CAMERA_REQUEST)
            }
            alertDialogBuilder.setNegativeButton("Gallery") { dialog, which ->
                dialog.dismiss()
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                mActivity.startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    AppConstants.GALLERY_REQUEST
                )
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
        }


        fun getRandomValue(isString: Boolean): String? {
            var values: String? = null

            if (isString) {
                val generator = Random()
                val randomStringBuilder = StringBuilder()
                val randomLength = generator.nextInt(4)
                var tempChar: Char
                for (i in 0 until randomLength) {
                    tempChar = (generator.nextInt(96) + 32).toChar()
                    randomStringBuilder.append(tempChar)
                }
                values = randomStringBuilder.toString()
            } else {
                val range = 9 // to generate a single number with this range, by default its 0..9
                val length = 3 // by default length is 4
                val secureRandom = SecureRandom()
                var s = ""
                run {
                    var i = 0
                    while (i < length) {
                        val number = secureRandom.nextInt(range)
                        if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                            i = -1
                            i++
                            continue
                        }
                        s = s + number
                        i++
                    }
                }
                values = s.toString()
            }
            return values
        }

        fun saveImagetoSDcard(img: Bitmap, mActivity: Activity): String? {
            val imagename: String? = getRandomValue(true)
            val mydir = File(
                mActivity.filesDir
                    .toString() + "/" + "DataBind/"
            )
            if (!mydir.exists()) {
                mydir.mkdir()
            }
            val image = File(mydir, "$imagename.jpg")
            var success = false
            val outStream: FileOutputStream
            try {
                outStream = FileOutputStream(image)
                img.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                outStream.flush()
                outStream.close()
                success = true
            } catch (e: java.lang.Exception) {
            }
            return if (success) {
                "$mydir/$imagename.jpg"
            } else {
                ""
            }
        }

        fun getBitmapFromURL(src: String?): Bitmap? {
            return try {
                val url = URL(src)
                val connection =
                    url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: java.lang.Exception) {
                // Log exception
                null
            }
        }

        @Throws(IOException::class)
        fun getCorrectlyOrientedImage(
            context: Context,
            photoUri: Uri?
        ): Bitmap? {
            val MAX_IMAGE_DIMENSION = 900
            var `is` = context.contentResolver.openInputStream(photoUri!!)
            val dbo = BitmapFactory.Options()
            dbo.inJustDecodeBounds = true
            BitmapFactory.decodeStream(`is`, null, dbo)
            `is`!!.close()
            val rotatedWidth: Int
            val rotatedHeight: Int
            val orientation: Int =
                getOrientation(context, photoUri)
            if (orientation == 90 || orientation == 270) {
                rotatedWidth = dbo.outHeight
                rotatedHeight = dbo.outWidth
            } else {
                rotatedWidth = dbo.outWidth
                rotatedHeight = dbo.outHeight
            }
            var srcBitmap: Bitmap?
            `is` = context.contentResolver.openInputStream(photoUri)
            if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
                val widthRatio =
                    rotatedWidth.toFloat() / MAX_IMAGE_DIMENSION.toFloat()
                val heightRatio =
                    rotatedHeight.toFloat() / MAX_IMAGE_DIMENSION.toFloat()
                val maxRatio = Math.max(widthRatio, heightRatio)

                // Create the bitmap from file
                val options = BitmapFactory.Options()
                options.inSampleSize = maxRatio.toInt()
                srcBitmap = BitmapFactory.decodeStream(`is`, null, options)
            } else {
                srcBitmap = BitmapFactory.decodeStream(`is`)
            }
            `is`!!.close()

            /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */if (orientation > 0) {
                val matrix = Matrix()
                matrix.postRotate(orientation.toFloat())
                srcBitmap = Bitmap.createBitmap(
                    srcBitmap!!, 0, 0, srcBitmap.width,
                    srcBitmap.height, matrix, true
                )
            }
            return srcBitmap
        }

        fun getOrientation(context: Context, photoUri: Uri?): Int {
            // it's on the external media.
            return try {
                val cursor = context.contentResolver.query(
                    photoUri!!,
                    arrayOf(MediaStore.Images.ImageColumns.ORIENTATION),
                    null,
                    null,
                    null
                )
                if (cursor!!.count != 1) {
                    return -1
                }
                cursor.moveToFirst()
                cursor.getInt(0)
            } catch (e: java.lang.Exception) {
                0
            }
        }

        fun setClipboard(context: Context, text: String?) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.text = text
            } else {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", text)
                clipboard.setPrimaryClip(clip)
            }
        }

        fun convertMillisToDateString(
            timeInMillis: Long,
            DATE_TIME_FORMAT: String?
        ): String? {
            val d = java.util.Date(timeInMillis)
            val sdf = SimpleDateFormat(DATE_TIME_FORMAT)
            return sdf.format(d)
        }


        fun getResizedBitmap(bm: Bitmap): Bitmap? {
            var resizedBitmap: Bitmap? = null
            return try {
                val width = bm.width
                val height = bm.height
                val scaleWidth = 480.toFloat() / width
                val scaleHeight = 640.toFloat() / height
                // create a matrix for the manipulation
                val matrix = Matrix()
                // resize the bit map
                matrix.postScale(scaleWidth, scaleHeight)
                // recreate the new Bitmap
                resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
                resizedBitmap
            } catch (e: java.lang.Exception) {
                resizedBitmap
            }
        }


        /**
         * Toast related constants
         */
        var LONG = "long"
        var SHORT = "short"
        var MIDDLE_SHORT = "middleShort"
        var MIDDLE_LONG = "middleLong"
        var TOP_SHORT = "topShort"
        var TOP_LONG = "topLong"


        /**
         * Date time format
         */
        var DATE_TIME_FORMAT_1 = "yyyy-MM-dd"
        var DATE_TIME_FORMAT_2 = "dd MMM yyyy" // 27 Nov 2018

        var DATE_TIME_FORMAT_3 = "yyyy-MM-dd HH:mm:ss" // 2018-09-10 10:30:12

        var DATE_TIME_FORMAT_4 = "yyyy-MM-dd KK:mm a" // 2018-09-10 10:30 AM

        var DATE_TIME_FORMAT_5 = "EEEE" // Sunday

        var DATE_TIME_FORMAT_6 = "dd MMM yyyy KK:mm a" // 27 Nov 2018 10:30 AM

        var DATE_TIME_FORMAT_7 = "yyyyMMdd_HHmmss" // 27 Nov 2018 10:30 AM

        var DATE_TIME_FORMAT_8 = "HH:mm:ss" //

        var DATE_TIME_FORMAT_9 = "KK:mm a" //

        var DATE_TIME_FORMAT_10 = "dd MMM" // 24 Sep

        var REQ_CODE_MULTIPLE_PERMISSIONS = 1001
        var REQ_CODE_SELECT_IMAGE = 1002
        var REQ_CODE_CAPTURE_IMAGE = 1003
        var REQ_CODE_CAPTURE_IMAGE_1 = 1004
        var REQ_CODE_CAPTURE_IMAGE_2 = 1005
        var REQ_CODE_CAPTURE_VIDEO = 1006
        var REQ_CODE_LOCATION_PERMISSION = 1007
        var REQ_CODE_LOCATION_PERMISSION_DYNAMICFORM = 1008
        var REQ_CODE_MAP_DATA = 1009
        var REQ_CODE_CAPTURE_IMAGE_3 = 1010
        var REQ_CODE_PHONE_INFO = 1011
        var REQ_CODE_MAP2_DATA = 1012


        var ROOT_DIRECTORY = "ARKA_VAYPER"
        fun isMobileValid(msgUserMobile: String): Boolean {
            // 11 digit number start with 011 or 010 or 015 or 012
            // then [0-9]{8} any numbers from 0 to 9 with length 8 numbers
            try {
                if(!Pattern.matches("(011|012|010|015)[0-9]{8}", msgUserMobile.trim())) {
                    return false
                }
                return true
            } catch (e: Exception) { return true
            }
        }
        fun isValidEmail(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }

        fun getAllAddress(latlang: LatLng):JSONObject{
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(App.instance, Locale.getDefault())

            addresses = geocoder.getFromLocation(
                latlang.latitude,
                latlang.longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            val address: String =
                addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            val city: String = addresses[0].getLocality()
            val state: String = addresses[0].getAdminArea()
            val country: String = addresses[0].getCountryName()
            val postalCode: String = addresses[0].getPostalCode()
            val knownName: String = addresses[0].subLocality

            var json = JSONObject()
            json.put("address", address)
            json.put("knownName", knownName)
            json.put("city", city)
            json.put("state", state)
            json.put("country", country)
            json.put("postalCode", postalCode)
return json
        }

    }


    /*fun showDialog(activity: Activity?, msg: String?) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_view)
        val text = dialog.findViewById(R.id.dialogText) as TextView
        val closePopup: TextView = dialog.findViewById(R.id.closePopup) as TextView
        val ic_closePopup = dialog.findViewById(R.id.ic_closePopup) as ImageView
        text.text = msg
        ic_closePopup.setOnClickListener { dialog.dismiss() }
        closePopup.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
*/
}