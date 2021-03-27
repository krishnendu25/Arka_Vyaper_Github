package com.arkavyapar.ServiceMannager

import com.arkavyapar.BuildConfig


class URLConstants {
    companion object {
        const val BASE_URL_API = BuildConfig.BASE_URL
        const val registration = "Welcome/registration"
        const val Sendotp = "Welcome/Sendotp"
        const val validate_otp = "Welcome/validate_otp"
        const val userLogin = "Welcome/userLogin"
        const val updatelocation = "Welcome/updatelocation"
        const val userdetails = "Welcome/userdetails"

        const val banner_header = "Welcome/banner_header"
        const val productadd = "Welcome/productadd"
        const val productdetailsfetch = "Welcome/productdetailsfetch"
        const val getAllByerLocation = "Welcome/getAllByerLocation"
        const val getAllSellerLocation = "Welcome/getAllSellerLocation"
        const val getAllSellerList = "Welcome/getAllSellerList";
        const val getAllByerList = "Welcome/getAllByerList";
        const val getAllProduct = "Welcome/getAllProductMasterr";
        const val hitupdateuser = "Welcome/updateuser"

        const val requestproduct = "Welcome/Requestproduct"
        const val requestallproduct = "Welcome/Requestallproduct"
    }
}