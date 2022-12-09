package com.example.uploadingfiles.ui

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.uploadingfiles.R
import com.example.uploadingfiles.utils.getAvailableInternalMemorySize
import com.example.uploadingfiles.utils.getFilePathFromUri
import com.example.uploadingfiles.utils.getFileSize
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var permReqLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var addProductViewModel: AddProductViewModel
    private lateinit var selectedFile: Uri
    private lateinit var tvFile: TextView

    private var Permissions   =  arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvFile = findViewById(R.id.textView)

        val viewModelFactory = ViewModelFactory(this)

        addProductViewModel =
            ViewModelProvider(this, viewModelFactory).get(AddProductViewModel::class.java)


        addProductViewModel.response.observe(this, Observer {
            if (it.isNotEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                // new added
              File(this.cacheDir, addProductViewModel.fileName.value.toString()).delete()
                addProductViewModel.rest()
            }

        })


        addProductViewModel.connectionError.observe(this, Observer {
            if (it.isNotEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                Log.d("TAG", "Bego Uploading Files: $it")
                addProductViewModel.rest()
            }

        })


        permReqLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all { it.value }
                if (granted) {
                    selectImage()
                } else {
                    Toast.makeText(this@MainActivity, "No Permission Granted", Toast.LENGTH_SHORT)
                        .show()

                }
            }


    }

    fun upload(view: View) {


          if(getFileSize(this@MainActivity,selectedFile)<getAvailableInternalMemorySize()){
              Toast.makeText(this, "uploading...", Toast.LENGTH_SHORT).show()

                  addProductViewModel.upload(
                      "iphone 13",
                      "good product",
                      "13000",
                      "phones",
                      "0",
                      "0",
                      selectedFile,
                      getFilePathFromUri(this@MainActivity,selectedFile,addProductViewModel)
                  )

          }else{
              Toast.makeText(this, "no enough space", Toast.LENGTH_SHORT).show()

          }



    }

    fun pick(view: View) {
        if (hasPermissions(this@MainActivity, Permissions)) {

            selectImage()

        } else {
            permReqLauncher.launch(Permissions)
        }


    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == RESULT_OK) {
                // Get the Image from data
                selectedFile = result.data!!.data!!


                tvFile.text =  selectedFile.path
                Log.d("TAG", "Bego Uploading Files: ${selectedFile.scheme}")
                Log.d("TAG", "Bego Uploading Files: ${ContentResolver.SCHEME_CONTENT}")
                selectedFile.path?.let {
                    Log.d("TAG", "Bego Uploading Files: $it")
                }
                Log.d("TAG", "Bego Uploading Files: ${this.contentResolver.getType(selectedFile)}")


            }
        }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        resultLauncher.launch(intent)

    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

}