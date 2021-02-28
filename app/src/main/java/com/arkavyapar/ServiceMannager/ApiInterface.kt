package com.arkavyapar.hoori.controller

import com.arkavyapar.Model.LoginModel
import com.arkavyapar.Model.RegistationModel
import com.arkavyapar.Model.VideoUploadModel
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
}