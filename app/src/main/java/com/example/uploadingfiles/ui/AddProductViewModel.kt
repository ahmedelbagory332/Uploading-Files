package com.example.uploadingfiles.ui


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.example.uploadingfiles.data.AddProductRepository

@SuppressLint("StaticFieldLeak")
class AddProductViewModel constructor(ctx:Context) : ViewModel() {


   private val addProductRepository = AddProductRepository(ctx)


    val connectionError: LiveData<String>
        get() = addProductRepository.connectionError


    val response: LiveData<String>
        get() = addProductRepository.serverResponse


    fun rest() {
        addProductRepository.restAddProductVariables()

    }

    fun upload(
        product_name: String,
        product_des: String,
        product_price: String,
        product_section: String,
        product_offer_price: String,
        product_offer_percentage: String,
        fileUri: Uri) {
        addProductRepository.uploadProduct(
            product_name,
            product_des,
            product_price,
            product_section,
            product_offer_price,
            product_offer_percentage,
            fileUri)


    }




}