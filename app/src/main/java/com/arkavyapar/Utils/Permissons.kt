package com.arkavyapar.Utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arkavyapar.Constant.Constants


class Permissons {

    companion object{


        /**
         * check multiple permission given or not (single time)
         */
        fun isAllPermissionsGranted(context: Context?): Boolean {
            val locationPermission =
                ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            val storagePermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val cameraPermission =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            val listPermissionsNeeded: MutableList<String> = ArrayList()
            if (locationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA)
            }
            return listPermissionsNeeded.isEmpty()
        }


        /**
         * request multiple permission at a time
         */
        fun requestAllPermissions(context: Context?) {
            val locationPermission =
                ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            val storagePermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val cameraPermission =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            val listPermissionsNeeded: MutableList<String> = ArrayList()
            if (locationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA)
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(
                    (context as Activity?)!!,
                    listPermissionsNeeded.toTypedArray(),
                    Constants.REQ_CODE_MULTIPLE_PERMISSIONS
                )
            }
        }


        // Read write Permission
        fun checkStoragePermission(context: Context?): Boolean {
            val result = ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val resultRestore = ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            return result == PackageManager.PERMISSION_GRANTED && resultRestore == PackageManager.PERMISSION_GRANTED
        }


        fun requestStoragePermission(activity: Activity?, requestCodeStorage: Int) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                requestCodeStorage
            )
        }


        // Check Read & write Permission
        fun isLocationPermissionGranted(context: Context?): Boolean {
            val resultFine =
                ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            val resultCourse = ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            return resultFine == PackageManager.PERMISSION_GRANTED && resultCourse == PackageManager.PERMISSION_GRANTED
        }


        // Request for Read & write Permission
        fun requestLocationPermission(context: Context?, requestCodeLocation: Int) {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestCodeLocation
            )
        }


        /**
         * check multiple permission given or not (single time)
         */
        fun isPhoneInfoPermissionsGranted(context: Context?): Boolean {
            val smsPermission =
                ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_SMS)
            val phNoPermission =
                ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_PHONE_NUMBERS)
            val phStatePermission =
                ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_PHONE_STATE)
            val listPermissionsNeeded: MutableList<String> = ArrayList()
            if (smsPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_SMS)
            }
            if (phNoPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_NUMBERS)
            }
            if (phStatePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE)
            }
            return listPermissionsNeeded.isEmpty()
        }


        /**
         * request multiple permission at a time
         */
        fun requestPhoneInfoPermissions(context: Context?) {
            val smsPermission =
                ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_SMS)
            val phNoPermission =
                ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_PHONE_NUMBERS)
            val phStatePermission =
                ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_PHONE_STATE)
            val listPermissionsNeeded: MutableList<String> = ArrayList()
            if (smsPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_SMS)
            }
            if (phNoPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_NUMBERS)
            }
            if (phStatePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE)
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(
                    (context as Activity?)!!,
                    listPermissionsNeeded.toTypedArray(),
                    Constants.REQ_CODE_PHONE_INFO
                )
            }
        }


    }
    //Request Permisson

}