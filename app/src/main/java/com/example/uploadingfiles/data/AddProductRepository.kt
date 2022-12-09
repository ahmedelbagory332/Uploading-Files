package com.example.uploadingfiles.data


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import retrofit2.Callback
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

@SuppressLint("StaticFieldLeak")
class AddProductRepository constructor(private val ctx:Context) {



     val connectionError = MutableLiveData("")
     val serverResponse = MutableLiveData("")



    fun restAddProductVariables() {
        connectionError.value = ""
        serverResponse.value = ""

    }

    @SuppressLint("SuspiciousIndentation")
    fun uploadProduct(
        product_name: String,
        product_des: String,
        product_price: String,
        product_section: String,
        product_offer_price: String,
        product_offer_percentage: String,
        fileUri: Uri,
        fileRealPath: String
    ) {
        // new added

         val fileToSend = prepareFilePart("product_file", fileRealPath,fileUri)

        val productNameRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), product_name)
        val productDescriptionRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), product_des)
        val productPriceRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), product_price)
        val productSectionRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), product_section)
        val productOfferPriceRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), product_offer_price)
        val productOfferPercentageRequestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), product_offer_percentage)


              RetrofitService.getInstance().addProduct(
                   productNameRequestBody,
                   productDescriptionRequestBody,
                   productPriceRequestBody,
                   productSectionRequestBody,
                   productOfferPriceRequestBody,
                   productOfferPercentageRequestBody,
                   fileToSend).enqueue( object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if(response.body() != null && response.isSuccessful) {
                    try {

                        if (response.code() == 200) {
                            serverResponse.value = "uploaded"


                        } else {

                            connectionError.value = response.errorBody().toString()
                        }
                    } catch (e: Exception) {
                         connectionError.value = e.message.toString()
                    }
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                connectionError.value = t.message.toString()
            }
        })




    }





    // new added

    private fun prepareFilePart(partName: String,fileRealPath: String,fileUri: Uri): MultipartBody.Part {
        val file: File = File(fileRealPath)
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse(ctx.contentResolver.getType(fileUri)!!), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }


}