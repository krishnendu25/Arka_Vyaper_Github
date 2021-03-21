package com.arkavyapar.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.Html
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.arkavyapar.BuildConfig
import com.arkavyapar.Constant.Constants
import com.arkavyapar.R
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import org.json.JSONArray
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    companion object {
        fun runAnimation(view: View, tada: Techniques, durartion: Long){
            YoYo.with(tada)
                .duration(durartion)
                .repeat(1)
                .playOn(view);
        }
    /**
     * Calculate distance between 2 lat lng points
     */
    fun calculateDistanceUsingLatLng(point1: LatLng?, point2: LatLng?): Double? {
        return if (point1 == null || point2 == null) {
            null
        } else SphericalUtil.computeDistanceBetween(point1, point2)
    }// To be safe, you should check that the SDCard is mounted
    // using Environment.getExternalStorageState() before doing this.
    open fun toTitleCase(string: String?): String? {

        // Check if String is null
        if (string == null) {
            return null
        }
        var whiteSpace = true
        val builder = java.lang.StringBuilder(string) // String builder to store string
        val builderLength = builder.length

        // Loop through builder
        for (i in 0 until builderLength) {
            val c = builder[i] // Get character at builders position
            if (whiteSpace) {

                // Check if character is not white space
                if (!Character.isWhitespace(c)) {

                    // Convert to title case and leave whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c))
                    whiteSpace = false
                }
            } else if (Character.isWhitespace(c)) {
                whiteSpace = true // Set character is white space
            } else {
                builder.setCharAt(i, Character.toLowerCase(c)) // Set character to lowercase
            }
        }
        return builder.toString() // Return builders text
    }
    // This location works best if you want the created images to be shared
    // between applications and persist after your app has been uninstalled.

    // Create the storage directory if it does not exist
    // Create a media file name
    /** Create a File for saving an image or video  */
    private val outputMediaFile: File?
        private get() {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.
            val mediaStorageDir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + Constants.ROOT_DIRECTORY
            )

            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }
            // Create a media file name
            val timeStamp = SimpleDateFormat("ddMMyyyy_HHmm").format(Date())
            val mediaFile: File
            val mImageName = "MI_$timeStamp.jpg"
            mediaFile = File(mediaStorageDir.path + File.separator + mImageName)
            return mediaFile
        }


        /**
         * launch a new activity
         */
        fun launchActivity(context: Activity, destinationClass: Class<*>?) {
            val i = Intent(context, destinationClass)
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            if (Build.VERSION.SDK_INT > 20) {
                val options = ActivityOptions.makeSceneTransitionAnimation(context)
                context.startActivity(i, options.toBundle())
            } else {
                context.startActivity(i)
            }

        }

        /**
         * launch a new activity and finish current activity
         */
        fun launchActivityWithFinish(context: Activity, destinationClass: Class<*>?) {
            val intent = Intent(context, destinationClass)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            (context as Activity).finish()
        }

        /**
         * launch a new activity with start activity for result
         */
        fun launchActivityWithStartActivityForResult(
            context: Context,
            destinationClass: Class<*>?,
            requestCode: Int
        ) {
            val i = Intent(context, destinationClass)
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            (context as Activity).startActivityForResult(i, requestCode)
        }


        /**
         * Show toast with custom text and in different positions
         */
        fun showToast(context: Context?, string: String?, length: String?) {
            when (length) {
                Constants.SHORT -> Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
                Constants.LONG -> Toast.makeText(context, string, Toast.LENGTH_LONG).show()
                Constants.MIDDLE_SHORT -> {
                    val tMiddleShort = Toast.makeText(context, string, Toast.LENGTH_SHORT)
                    tMiddleShort.setGravity(Gravity.CENTER, 0, 0)
                    tMiddleShort.show()
                }
                Constants.MIDDLE_LONG -> {
                    val tMiddleLong = Toast.makeText(context, string, Toast.LENGTH_LONG)
                    tMiddleLong.setGravity(Gravity.CENTER, 0, 0)
                    tMiddleLong.show()
                }
                Constants.TOP_SHORT -> {
                    val tTopShort = Toast.makeText(context, string, Toast.LENGTH_SHORT)
                    tTopShort.setGravity(Gravity.TOP, 0, 0)
                    tTopShort.show()
                }
                Constants.TOP_LONG -> {
                    val tTopLong = Toast.makeText(context, string, Toast.LENGTH_LONG)
                    tTopLong.setGravity(Gravity.TOP, 0, 0)
                    tTopLong.show()
                }
            }
        }

        /**
         * Check internet connection available or not
         */
        fun isOnline(context: Context): Boolean {
            val cManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cManager != null) {
                val info = cManager.allNetworkInfo
                if (info != null) {
                    for (anInfo in info) {
                        if (anInfo.state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        /**
         * Get current date and time
         */
        fun getCurrentDateNTime(inputFormat: String?): String {
            val d = Date()
            val curr_datetime = DateFormat.format(inputFormat, d.time)
            return curr_datetime.toString()
        }

        /**
         * Change date and time format
         */
        fun changeDateNTimeFormat(
            inputDateTime: String?,
            inFormat: String?,
            outFormat: String?
        ): String {
            var output_datetime = ""
            try {
                val inputFormat = SimpleDateFormat(inFormat, Locale.getDefault())
                val outputFormat = SimpleDateFormat(outFormat, Locale.getDefault())
                output_datetime = outputFormat.format(inputFormat.parse(inputDateTime))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return output_datetime
        }

        // Get range of start date and end date of the week of selected date in "24th - 30th" format (input = Calendar.getInstance())
        fun getFirstAndLastDateRangeOfWeek(mCalendar: Calendar): String {
            // 1 = Sunday, 2 = Monday, etc.
            val day_of_week = mCalendar[Calendar.DAY_OF_WEEK]
            val monday_offset: Int
            monday_offset = if (day_of_week == 1) {
                -6
            } else 2 - day_of_week // need to minus back
            mCalendar.add(Calendar.DAY_OF_YEAR, monday_offset)
            val mDateMonday = mCalendar.time

            // Returns 6 the next days of current day (object cal save current day)
            mCalendar.add(Calendar.DAY_OF_YEAR, 6)
            val mDateSunday = mCalendar.time

            // Get format date
            val sdf = SimpleDateFormat(Constants.DATE_TIME_FORMAT_10, Locale.getDefault())
            val firstDate = sdf.format(mDateMonday).substring(0, 2)
            val lastDate = sdf.format(mDateSunday).substring(0, 2)
            return firstDate.toInt()
                .toString() + getSuffixOfDayOfMonth(firstDate.toInt()) + " - " + lastDate.toInt() + getSuffixOfDayOfMonth(
                lastDate.toInt()
            )
        }

        // Get date suffix from date (22 = 22th, 2 = 2nd)
        @SuppressLint("RestrictedApi")
        private fun getSuffixOfDayOfMonth(n: Int): String {
            return if (n >= 1 && n <= 31) {
                if (n >= 11 && n <= 13) {
                    "th"
                } else when (n % 10) {
                    1 -> "st"
                    2 -> "nd"
                    3 -> "rd"
                    else -> "th"
                }
            } else ""
        }

        fun decodeUriToBitmap(mContext: Context, sendUri: Uri?): Bitmap? {
            var getBitmap: Bitmap? = null
            try {
                val image_stream: InputStream?
                try {
                    image_stream = mContext.contentResolver.openInputStream(sendUri!!)
                    getBitmap = BitmapFactory.decodeStream(image_stream)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return getBitmap
        }

        /**
         * Check upper case, lower case, number & special character available or not
         */
        fun isValidPassword(newPassword: String): Boolean {
            var upper = 0
            var lower = 0
            var number = 0
            var special = 0
            for (i in 0 until newPassword.length) {
                val ch = newPassword[i]
                if (ch >= 'A' && ch <= 'Z') upper++ else if (ch >= 'a' && ch <= 'z') lower++ else if (ch >= '0' && ch <= '9') number++ else special++
            }
            return upper > 0 && lower > 0 && number > 0 && special > 0
        }

        fun getAppVersion(context: Context): String {
            var version = "0.0"
            try {
                val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                version = pInfo.versionName
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return version
        }

        /**
         * get android device id
         */
        @SuppressLint("HardwareIds")
        fun getMobileDeviceId(context: Context): String {
            try {
                return Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            } catch (e: Exception) {
                Toast.makeText(context, "Device ID not available.", Toast.LENGTH_LONG).show()
            }
            return ""
        }

        /**
         * get all sim id list
         */
        @SuppressLint("MissingPermission")
        fun getMobileSimIdArr(context: Context): JSONArray {
            val simIdArr = JSONArray()
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    val manager =
                        context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
                    val infoList = manager.activeSubscriptionInfoList
                    if (infoList != null && !infoList.isEmpty()) {
                        for (i in infoList.indices) {
                            val info = infoList[i]
                            simIdArr.put(info.iccId)
                        }
                    }
                } else {
                    val telephonyManager =
                        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    simIdArr.put(telephonyManager.simSerialNumber)
                }
            } catch (ex: Exception) {
                Toast.makeText(context, "Invalid SIM or SIM not available.", Toast.LENGTH_LONG)
                    .show()
            }
            //simIdArr.put(1111); //TODO for testing
            return simIdArr
        }

        fun showSoftKeyboard(context: Context, view: View?) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        }

        fun hideSoftKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        /**
         * Convert DP to pixel value as per screen density.
         */
        fun convertDipToPixels(context: Context, dip: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip.toFloat(),
                context.resources.displayMetrics
            )
                .toInt()
        }

        fun convertDpToPixel(dp: Int, context: Context): Int {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
        }

        /**
         * Convert font size DP to pixel value as per screen density.
         */
        fun fontSizeDpToPixel(context: Context, `val`: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                `val`.toFloat(),
                context.resources.displayMetrics
            )
                .toInt()
        }

        fun convertPixelToDp(pixel: Int, context: Context): Float {
            return pixel / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun makeTextViewDifferentColor(firstLetter: String, restText: String, textView: TextView) {
            val text =
                "<font color=#212121>$firstLetter</font> <font color=#757575>$restText</font>"
            textView.text = Html.fromHtml(text)
        }

        fun makeTextViewDifferentColorPrimay(
            firstLetter: String,
            restText: String,
            textView: TextView
        ) {
            val text =
                "<font color=#00574B>$firstLetter</font> <font color=#008577>$restText</font>"
            textView.text = Html.fromHtml(text)
        }

        /*String mendatoryMark = "<font color='#EE0000'>" + "*" + "</font>";
     labelTv.setText(Html.fromHtml(label + mendatoryMark));*/
        val starMarkInRedColor: String
            get() = "<font color='#EE0000'>" + "*" + "</font>"

        /*String mendatoryMark = "<font color='#EE0000'>" + "*" + "</font>";
        labelTv.setText(Html.fromHtml(label + mendatoryMark));*/

        fun setMendatoryMarks(textView: TextView, text: String) {
            //return "<font color='#EE0000'>" + "*" + "</font>";

            /*String mendatoryMark = "<font color='#EE0000'>" + "*" + "</font>";
        labelTv.setText(Html.fromHtml(label + mendatoryMark));*/
            val mendatoryText = "$text<font color='#EE0000'>*</font>"
            textView.text = Html.fromHtml(mendatoryText)
        }

        // Generate a 5 digit random number
        fun generateFiveDigitRandomNumber(): String {
            val r = Random(System.currentTimeMillis())
            val randNumber = (1 + r.nextInt(2)) * 10000 + r.nextInt(10000)
            return randNumber.toString()
        }

        public fun setImageFromUrl(view: ImageView, url: String, context: Context){
            Glide
                .with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(view);
        }



        /**
         * Returns the consumer friendly device name
         */
        val deviceName: String
            get() {
                val manufacturer = Build.MANUFACTURER
                val model = Build.MODEL
                return if (model.startsWith(manufacturer)) {
                    capitalize(model)
                } else capitalize(manufacturer) + " " + model
            }

        private fun capitalize(str: String): String {
            if (TextUtils.isEmpty(str)) return str
            val arr = str.toCharArray()
            var capitalizeNext = true
            val phrase = StringBuilder()
            for (c in arr) {
                if (capitalizeNext && Character.isLetter(c)) {
                    phrase.append(Character.toUpperCase(c))
                    capitalizeNext = false
                    continue
                } else if (Character.isWhitespace(c)) capitalizeNext = true
                phrase.append(c)
            }
            return phrase.toString()
        }

        // calculate percentage
        fun calculatePercentage(value: String, totalValue: Float): Float {
            val singleValue = value.toFloat()
            return singleValue * 100 / totalValue
        }

        fun getUriFromFile(context: Context?, file: File?): Uri? {
            var fileUri: Uri? = null
            fileUri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Uri.fromFile(file)
            } else FileProvider.getUriForFile(
                context!!, BuildConfig.APPLICATION_ID.toString() + ".provider",
                file!!
            )
            return fileUri
        }

        fun deleteDirectory(path: File): Boolean {
            if (path.exists()) {
                val files = path.listFiles() ?: return true
                for (i in files.indices) {
                    if (files[i].isDirectory) {
                        deleteDirectory(files[i])
                    } else {
                        files[i].delete()
                    }
                }
            }
            return path.delete()
        }
        /**
         * Convert custom array list to byte array
         * To store in local database
         */
        /*public static byte[] convertArrayListToByteArrayBlobFormat(ArrayList<ImageVideoListModel> fileList) {
        byte[] byteArr = new byte[0];
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(fileList);
            byteArr = bos.toByteArray();
        } catch (Exception e) {
            byteArr = null;
            e.getStackTrace();
        }
        return byteArr;
    }*/
        /**
         * Convert byte array to custom array list
         * Fetch from local database
         */
        /*public static ArrayList<ImageVideoListModel> getArrayListFromByteArray(byte[] byteArr) {
        ObjectInputStream ois = null;
        ArrayList<ImageVideoListModel> imageList = new ArrayList<>();
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(byteArr));
            imageList.addAll((ArrayList<ImageVideoListModel>) ois.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageList;
    }*/
        fun SaveImagetoSDcard(imagename: String, img: Bitmap, mActivity: Activity?): String {
            val dir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + Constants.ROOT_DIRECTORY + File.separator
            )
            if (!dir.exists()) {
                dir.mkdirs()
                Log.d("@@@ ", "Dir Created==> $dir")
            }

            /* File mydir = new File(mActivity.getFilesDir() + "/" + "_HMC/");
        if (!mydir.exists()) {
            mydir.mkdir();
        }*/
            val image = File(dir, "$imagename.jpg")
            var success = false
            val outStream: FileOutputStream
            try {
                outStream = FileOutputStream(image)
                img.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                outStream.flush()
                outStream.close()
                success = true
            } catch (e: IOException) {
            } catch (e: Exception) {
            }
            return if (success) {
                "$dir/$imagename.jpg"
            } else {
                ""
            }
        }

        fun saveFile(str: String?) {
            val dir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + Constants.ROOT_DIRECTORY + File.separator
            )
            if (!dir.exists()) {
                dir.mkdirs()
                Log.d("@@@ ", "Dir Created==> $dir")
                return
            }
            val file = File(dir, "Layer_" + System.currentTimeMillis() + ".json")
            try {
                val fos = FileOutputStream(file)
                //image.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close()
            } catch (e: FileNotFoundException) {
                Log.d("TAG", "File not found: " + e.message)
            } catch (e: IOException) {
                Log.d("TAG", "Error accessing file: " + e.message)
            }
        }

        fun SaveImage(finalBitmap: Bitmap) {
            val root = Environment.getExternalStorageDirectory().toString()
            val myDir = File("$root/saved_images")
            myDir.mkdirs()
            val generator = Random()
            var n = 10000
            n = generator.nextInt(n)
            val fname = "Image-$n.jpg"
            val file = File(myDir, fname)
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}
