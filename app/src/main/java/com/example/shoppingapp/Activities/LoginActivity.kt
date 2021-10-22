package com.example.shoppingapp.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import com.example.shoppingapp.Activities.TheMainActivity.TheMainActivity
import com.example.shoppingapp.FireStore.Constants
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CustomButton
import com.example.shoppingapp.Utils.CustomEditText
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.FireStore.User
import com.example.shoppingapp.Utils.CommonMethodsClass
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : CommonMethodsClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<TextView>(R.id.register_user).setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }
        findViewById<CustomButton>(R.id.Login_btn).setOnClickListener{
            signIn()
        }
        findViewById<TextView>(R.id.forgot_password).setOnClickListener{
            startActivity(Intent(this,ResetPasswordActivity::class.java))

        }
    }
    private fun signIn(){
        when{
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.EmailAddress).text.toString().trim{it<=' '})->{
                showSnackBar("Please enter the Email Id",true)
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.Password).text.toString().trim{it<=' '})-> {
                 showSnackBar("Please enter the Password", true)
            }
            else->{
                showProgressBar()
                val email=findViewById<CustomEditText>(R.id.EmailAddress).text.toString().trim{it<=' '}
                val password=findViewById<CustomEditText>(R.id.Password).text.toString().trim{it<=' '}

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener{task->
                        if(task.isSuccessful){
                            showSnackBar("Login Successfully as $email",false)

                            FirestoreClass().getUserDetails(this@LoginActivity)
                        }
                        else{
                            hideProgressBar()
                            showSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
            }
        }
    }
    fun logInSuccessful( user: User){
        if(user.profileComplete==0){
            val intent= Intent(this, CompleteProfile::class.java)
            intent.putExtra(Constants.AllUserDetails,user)
            startActivity(intent)
        }
        else{
            startActivity(Intent(this, TheMainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        doubleBackPressToExit()
    }

}