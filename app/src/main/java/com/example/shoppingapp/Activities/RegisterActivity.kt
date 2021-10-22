package com.example.shoppingapp.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.shoppingapp.Activities.TheMainActivity.TheMainActivity
import com.example.shoppingapp.FireStore.Constants
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CustomEditText
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.Utils.CommonMethodsClass
import com.example.shoppingapp.FireStore.User
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : CommonMethodsClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setUpActionBar()
        findViewById<TextView>(R.id.register_activity_login).setOnClickListener {
           onBackPressed()
        }
        findViewById<Button>(R.id.register_activity_registerButton).setOnClickListener {
            registerUser()

        }
    }
    private fun registerUser(){
        if(validateUserDetails()){
            showProgressBar()
            val email=findViewById<CustomEditText>(R.id.Register_Activity_EmailAddress).text.toString().trim { it<=' ' }
            val password=findViewById<CustomEditText>(R.id.Register_Activity_Password).text.toString().trim { it<=' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        val firebaseUser=task.result!!.user

                        val user=User(firebaseUser!!.uid,
                            findViewById<CustomEditText>(R.id.first_name).text.toString(),
                            findViewById<CustomEditText>(R.id.last_name).text.toString(),
                            email)

                        FirestoreClass().registerUser(this@RegisterActivity,user)
                        FirestoreClass().getUserDetails(this@RegisterActivity)
                    }
                    else{
                        hideProgressBar()
                        showSnackBar(task.exception!!.message.toString(),true)
                    }
                }
        }
    }
    private fun validateUserDetails():Boolean{
        return when{
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.first_name).text.toString().trim{it <= ' '})-> {
                showSnackBar(resources.getString(R.string.error_msg_firstName),true)
                false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.last_name).text.toString().trim{it <= ' '})-> {
                showSnackBar(resources.getString(R.string.error_msg_lastName),true)
                false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.Register_Activity_EmailAddress).text.toString().trim{it <= ' '})-> {
                showSnackBar(resources.getString(R.string.error_msg_emailId), true)
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(findViewById<CustomEditText>(R.id.Register_Activity_EmailAddress).text.toString()).matches()->{
                showSnackBar(resources.getString(R.string.error_msg_incorrect_email),true)
                false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.Register_Activity_Password).text.toString().trim{it <= ' '})-> {
                showSnackBar(resources.getString(R.string.error_msg_Password),true)
                false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.confirm_pass).text.toString().trim{it <= ' '})-> {
                showSnackBar(resources.getString(R.string.error_msg_ConfirmPassword),true)
                false
            }

                findViewById<CustomEditText>(R.id.confirm_pass).text.toString()!=findViewById<CustomEditText>(R.id.Register_Activity_Password).text.toString() ->{
                showSnackBar(resources.getString(R.string.error_msg_password_notMatch),true)
                false
            }
            !findViewById<CheckBox>(R.id.checkbox).isChecked ->{
                showSnackBar(resources.getString(R.string.error_msg_termsAnd_Condtitions_not_checked),true)
                false
            }
            else -> {
                //showSnackBar(resources.getString(R.string.successfull_registration),false)
                true
            }
        }
    }
    private fun setUpActionBar(){
        setSupportActionBar(findViewById(R.id.register_activity_toolbar))
        val actionBar=supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        findViewById<Toolbar>(R.id.register_activity_toolbar).setNavigationOnClickListener { onBackPressed() }
    }
    fun logInSuccessful(){
        startActivity(Intent(this, TheMainActivity::class.java))
        finish()
    }

}