package com.example.shoppingapp.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import com.example.shoppingapp.Activities.TheMainActivity.TheMainActivity
import com.example.shoppingapp.FireStore.Constants
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.FireStore.User
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.*

class CompleteProfile : CommonMethodsClass() {
    private var userDetails = User()
    private var flag=0
    private var profilePictureUri:Uri?=null
    private var savedImageUrl:Uri?=null
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri:Uri? ->
            if(uri!=null) {
                profilePictureUri=uri
                GlideLoader(this).loadPicture(profilePictureUri!!,findViewById(R.id.cprofile_profilePicture))
            }
            else{
                Toast.makeText(this, "Error fetching image", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)
        setUpActionBar()

        val firstName = findViewById<CustomEditText>(R.id.cprofile_firstName)
        val lastName = findViewById<CustomEditText>(R.id.cprofile_lastName)
        val emailId = findViewById<CustomEditText>(R.id.cprofile_emailId)

        if (intent.hasExtra(Constants.AllUserDetails)) {
            userDetails = intent.getParcelableExtra(Constants.AllUserDetails)!!
            firstName.isEnabled = false
            lastName.isEnabled = false
            emailId.isEnabled = false
        }
        else if(intent.hasExtra(Constants.userDetailsFromSettings)){
            userDetails = intent.getParcelableExtra(Constants.userDetailsFromSettings)!!
            flag=1
            findViewById<CustomTextView>(R.id.cprofile_toolbarText).text=resources.getString(R.string.toolbarText)
            emailId.isEnabled = false
            findViewById<CustomEditText>(R.id.cprofile_mobileNo).setText(userDetails.mobile.toString())
            GlideLoader(this).loadPicture(userDetails.Image,findViewById<ImageView>(R.id.cprofile_profilePicture))
        }

        firstName.setText(userDetails.First_Name)
        lastName.setText(userDetails.Last_Name)
        emailId.setText(userDetails.email)

        findViewById<ImageView>(R.id.cprofile_profilePicture).setOnClickListener {
            getContent.launch("image/*")
        }
        findViewById<Button>(R.id.cprofile_saveBtn).setOnClickListener{
            if(validateUserDetails()){
                showProgressBar()

                if(profilePictureUri!=null) {
                    FirestoreClass().uploadImageToFirebaseStorage(this, profilePictureUri!!,Constants.profileImageHeader)
                }
                else {
                    updateUserDetails()
                }
            }
            else{
                showSnackBar("Please enter your mobile number",true)
            }
        }
    }
    private fun updateUserDetails(){
        val hash=hashMapOf<String,Any>()
        hash[resources.getString(R.string.GENDER)]=if(findViewById<CustomRadioButton>(R.id.cprofile_male).isChecked){
            resources.getString(R.string.male)
        }else{
            resources.getString(R.string.female)
        }
        if(flag==1 && userDetails.First_Name!=findViewById<CustomEditText>(R.id.cprofile_firstName).text.toString()){
            hash[Constants.userFirstName]=findViewById<CustomEditText>(R.id.cprofile_firstName).text.toString()
        }
        if(flag==1 && userDetails.Last_Name!=findViewById<CustomEditText>(R.id.cprofile_lastName).text.toString()){
            hash[Constants.userLastName]=findViewById<CustomEditText>(R.id.cprofile_lastName).text.toString()
        }
        hash[Constants.profileComplete]=1
        hash[resources.getString(R.string.mobile)]=findViewById<CustomEditText>(R.id.cprofile_mobileNo).text.toString().trim{it<=' '}.toLong()

        if(savedImageUrl!=null){
            hash[resources.getString(R.string.image)]=savedImageUrl.toString()
        }
        FirestoreClass().updateUserDetails(this@CompleteProfile, hash)
    }
    fun profilePictureUploaded(url:Uri){
        savedImageUrl=url
        updateUserDetails()
    }
    fun updateSuccess(){
        hideProgressBar()
        Toast.makeText(this, "User profile updated", Toast.LENGTH_SHORT).show()
        if(flag!=1) {
            val intent = Intent(this, TheMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
    private fun validateUserDetails():Boolean{
        return !TextUtils.isEmpty(findViewById<CustomEditText>(R.id.cprofile_mobileNo).text.toString().trim{it<=' '})
    }
        private fun setUpActionBar() {
            setSupportActionBar(findViewById(R.id.cprofile_toolbar))
            val actionBar = supportActionBar
            actionBar?.setDisplayShowTitleEnabled(false)
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
            }
            findViewById<Toolbar>(R.id.cprofile_toolbar).setNavigationOnClickListener { onBackPressed() }
        }
}
