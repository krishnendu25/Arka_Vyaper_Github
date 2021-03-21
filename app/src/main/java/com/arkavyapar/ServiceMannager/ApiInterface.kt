package com.arkavyapar.hoori.controller

import com.arkavyapar.Model.ReponseModel.CommonModel
import com.arkavyapar.Model.ReponseModel.*
import com.arkavyapar.ServiceMannager.URLConstants.Companion.Sendotp
import com.arkavyapar.ServiceMannager.URLConstants.Companion.banner_header
import com.arkavyapar.ServiceMannager.URLConstants.Companion.getAllByerList
import com.arkavyapar.ServiceMannager.URLConstants.Companion.getAllByerLocation
import com.arkavyapar.ServiceMannager.URLConstants.Companion.getAllProduct
import com.arkavyapar.ServiceMannager.URLConstants.Companion.getAllSellerList
import com.arkavyapar.ServiceMannager.URLConstants.Companion.getAllSellerLocation
import com.arkavyapar.ServiceMannager.URLConstants.Companion.hitupdateuser
import com.arkavyapar.ServiceMannager.URLConstants.Companion.productadd
import com.arkavyapar.ServiceMannager.URLConstants.Companion.productdetailsfetch
import com.arkavyapar.ServiceMannager.URLConstants.Companion.registration
import com.arkavyapar.ServiceMannager.URLConstants.Companion.updatelocation
import com.arkavyapar.ServiceMannager.URLConstants.Companion.userLogin
import com.arkavyapar.ServiceMannager.URLConstants.Companion.userdetails
import com.arkavyapar.ServiceMannager.URLConstants.Companion.validate_otp
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    /*  @FormUrlEncoded
      @POST(LOGIN)
      fun getGarageOwnerLogin(
          @Field("userName") username: String?,
          @Field("passWord") password: String?,
          @Field("deviceID") deviceID: String?
      ): Call<LoginModel>

      @GET(ALL_SERVICES)
      fun getAllServices(): Call<ResponseBody>

      @GET(ALL_CITY)
      fun GetAllCity(): Call<ResponseBody>

      @POST(salestoreregistration)
      fun Registation(@Body file: RequestBody?): Call<RegistationModel>

      @POST(salesvideoupload)
      fun UploadVideo(@Body file: RequestBody?): Call<VideoUploadModel>*/


    @FormUrlEncoded
    @POST(userLogin)
    fun Login(
        @Field("phoneNo") phoneNo: String?,
        @Field("Password") Password: String?,
        @Field("fcmToken") fcmToken: String?
    ): Call<LoginModel>


    @POST(registration)
    fun Registration(@Body file: RequestBody?): Call<CommonModel>


    @FormUrlEncoded
    @POST(Sendotp)
    fun Sendotp(@Field("phoneNo") phoneNo: String?
    ): Call<SendOTP>

    @FormUrlEncoded
    @POST(validate_otp)
    fun Verifyotp(@Field("phoneNo") phoneNo: String?,
                @Field("OTP") OTP: String?
    ): Call<VerifyOTP>



    @FormUrlEncoded
    @POST(updatelocation)
    fun updateLocation(@Field("userID") userID: String, @Field("latitude") latitude: String?, @Field("longitude") longitude: String?
    ): Call<CommonModel>

    @FormUrlEncoded
    @POST(userdetails)
    fun userdetails(@Field("userID") userID: String): Call<UserDetails>

    @GET(banner_header)
    fun getBannerHeader(): Call<BannerApiModel>
    @GET(getAllByerLocation)
    fun getAllBuyerLocation(): Call<ByerLocation>

    @GET(getAllSellerLocation)
    fun getAllSellerLoca(): Call<ByerLocation>

    @POST(productadd)
    fun productAddToMyProfile(@Body file: RequestBody?): Call<CommonModel>

    @FormUrlEncoded
    @POST(productdetailsfetch)
    fun productfetchToMyProfile(@Field("userID") userID: String): Call<MyProductListApi>

    @FormUrlEncoded
    @POST(getAllSellerList)
    fun getAllSellerListApi(@Field("latitude") latitude: String,
                            @Field("longitude") longitude: String,
                            @Field("userId") userID: String): Call<AllSellerListApiModel>

    @FormUrlEncoded
    @POST(getAllByerList)
    fun getAllByerListApi(@Field("latitude") latitude: String,
                            @Field("longitude") longitude: String,
                            @Field("userId") userID: String): Call<AllSellerListApiModel>

    @GET(getAllProduct)
    fun getproductDropDownModel(): Call<ResponseBody>

    @POST(hitupdateuser)
    fun hitupdateuserApi(@Body file: RequestBody?): Call<CommonModel>

}