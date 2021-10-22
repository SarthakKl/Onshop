package com.example.shoppingapp.Activities

import android.os.Bundle
import android.text.TextUtils
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CustomButton
import com.example.shoppingapp.Utils.CustomEditText
import com.example.shoppingapp.Utils.CommonMethodsClass
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : CommonMethodsClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        findViewById<CustomButton>(R.id.send_email).setOnClickListener{
            send_email()
        }
    }
    private fun send_email(){
        val email=findViewById<CustomEditText>(R.id.reset_EmailId).text.toString().trim{it<=' '}
        when{
           TextUtils.isEmpty(email)->{
                showSnackBar("Please enter the Email id: ",false)
            }
            else ->{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            finish()
                        }
                        else{
                            showSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
            }
        }
    }
}