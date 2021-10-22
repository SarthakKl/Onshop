package com.example.shoppingapp.Activities


import android.os.Bundle
import android.text.TextUtils
import com.example.shoppingapp.FireStore.Address
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CommonMethodsClass
import com.example.shoppingapp.Utils.CustomButton
import com.example.shoppingapp.Utils.CustomEditText
import com.example.shoppingapp.Utils.CustomRadioButton

class AddAddressActivity : CommonMethodsClass() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_add_address)

        findViewById<CustomButton>(R.id.address_saveAddress).setOnClickListener {
            if(validateAddressDetails()){
                val name=findViewById<CustomEditText>(R.id.address_fullName)
                val mobNo=findViewById<CustomEditText>(R.id.address_mobileNo)
                val pc=findViewById<CustomEditText>(R.id.address_pincode)
                val city=findViewById<CustomEditText>(R.id.address_city)
                val state=findViewById<CustomEditText>(R.id.address_state)
                val location=findViewById<CustomEditText>(R.id.address_houseNo)
                val landmark=findViewById<CustomEditText>(R.id.address_area)
                val addType=if(findViewById<CustomRadioButton>(R.id.address_homeAdd).isChecked){
                    findViewById<CustomRadioButton>(R.id.address_homeAdd).text.toString()
                }else{
                    findViewById<CustomRadioButton>(R.id.address_workAdd).text.toString()
                }

                val address=Address(FirestoreClass().getCurrentUserId(),
                    name.text.toString(),
                    mobNo.text.toString(),
                    pc.text.toString(),state.text.toString(),
                    city.text.toString(),
                    location.text.toString(),
                    landmark.text.toString(),
                    addType,"")

                FirestoreClass().addAddress(this,address)
            }
        }
    }
    fun addressAddedSuccessfully(){
        onBackPressed()
    }
    private fun validateAddressDetails():Boolean{
        return when{
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.address_fullName).text.toString().trim{it<=' '})->{
                showSnackBar("Please enter you full name",true)
                false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.address_mobileNo).text.toString().trim{it<=' '})->{
                showSnackBar("Please enter your mobile number",true)
                false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.address_pincode).text.toString().trim{it<=' '})->{
                showSnackBar("Please enter the Pincode",true)
                false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.address_city).text.toString().trim{it<=' '})->{
                showSnackBar("Please enter the city name",true)
                false
            }TextUtils.isEmpty(findViewById<CustomEditText>(R.id.address_state).text.toString().trim{it<=' '})->{
                showSnackBar("Please enter the state name",true)
                false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.address_houseNo).text.toString().trim{it<=' '})->{
                showSnackBar("Please enter the location details",true)
                false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.address_area).text.toString().trim{it<=' '})->{
                showSnackBar("Please enter the landmark details",true)
                false
            }
            else->{
                true
            }
        }
    }
}