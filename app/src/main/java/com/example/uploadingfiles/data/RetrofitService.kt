package com.example.uploadingfiles.data

import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface RetrofitService {


    @Multipart
    @POST("")
     fun addProduct(
        @Part("product_name") product_name: RequestBody?,
        @Part("product_des") product_des: RequestBody?,
        @Part("product_price") product_price: RequestBody?,
        @Part("product_section") product_section: RequestBody?,
        @Part("product_offer_price") product_offer_price: RequestBody?,
        @Part("product_offer_percentage") product_offer_percentage: RequestBody?,
        @Part file: MultipartBody.Part?) : Call<ResponseBody>




    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val client = OkHttpClient()

                val retrofit = Retrofit.Builder()
                    .baseUrl("")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }

}