package com.arkavyapar.hoori.controller

import com.arkavyapar.ServiceMannager.URLConstants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {




    companion object Factory {
        private var retrofit: Retrofit? = null

        fun getRetrofit(): Retrofit? {
            if (retrofit == null) {

                retrofit = Retrofit.Builder()
                    .baseUrl(URLConstants.BASE_URL_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            }
            return retrofit
        }

    }


}