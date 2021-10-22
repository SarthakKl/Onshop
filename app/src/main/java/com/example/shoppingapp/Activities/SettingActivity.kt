package com.example.shoppingapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.example.shoppingapp.FireStore.Constants
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.FireStore.User
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CustomButton
import com.example.shoppingapp.Utils.CustomTextView
import com.example.shoppingapp.Utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

class SettingActivity : AppCompatActivity() {
    private var userDetails:User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setUpActionBar()

        findViewById<ImageView>(R.id.settings_goToProfile).setOnClickListener{
            val intent=Intent(this,CompleteProfile::class.java)
            intent.putExtra(Constants.userDetailsFromSettings,userDetails)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.settings_goToAddress).setOnClickListener {
            startActivity(Intent(this,AddressActivity::class.java))
        }
        findViewById<CustomButton>(R.id.settings_logoutBtn).setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent=Intent(this,LoginActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        FirestoreClass().getUserDetails(this)
        super.onStart()
    }
    fun getUserDetails(user:User){
        userDetails=user
        findViewById<CustomTextView>(R.id.settings_userName).text="${userDetails!!.First_Name} ${userDetails!!.Last_Name}"
        if(!TextUtils.isEmpty(user.Image))
            GlideLoader(this).loadPicture(user.Image,findViewById(R.id.settings_userImage))
    }
    private fun setUpActionBar(){
        setSupportActionBar(findViewById(R.id.settings_toolbar))
        val actionBar=supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        findViewById<Toolbar>(R.id.settings_toolbar).setNavigationOnClickListener { onBackPressed() }
    }
}