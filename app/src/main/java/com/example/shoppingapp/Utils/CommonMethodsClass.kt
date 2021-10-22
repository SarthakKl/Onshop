package com.example.shoppingapp.Utils

import android.app.Dialog
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.shoppingapp.R
import com.google.android.material.snackbar.Snackbar


open class CommonMethodsClass:AppCompatActivity(){
    private lateinit var progressDialog:Dialog
    private var doubleBackPressed=false
    fun showProgressBar(){
        progressDialog= Dialog(this)
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.setTitle("Progress Dialog")

        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }
    fun hideProgressBar(){
        progressDialog.dismiss()
    }
    fun showSnackBar(message:String,error:Boolean){
        val sb=Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)

        val snackBarView=sb.view

        if(error){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@CommonMethodsClass,
                    R.color.SnackBar_error_red
                )
            )
        }
        else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@CommonMethodsClass,
                    R.color.SnackBar_error_green
                )
            )
        }

        snackBarView.setPadding(0, 0, 0, 0) //set padding to 0
        sb.show()
    }
    fun doubleBackPressToExit(){
        if(doubleBackPressed){
            super.onBackPressed()
            doubleBackPressed=false
        }
        doubleBackPressed=true

        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackPressed=false
        },2000)
    }
}