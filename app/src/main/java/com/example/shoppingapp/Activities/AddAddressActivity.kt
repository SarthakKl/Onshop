package com.example.shoppingapp.Activities


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.example.shoppingapp.FireStore.Address
import com.example.shoppingapp.FireStore.Constants
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.*

class AddAddressActivity : CommonMethodsClass() {
    private var addressDetails:Address?=null
    private var editAdd=false
    private var selectAddress=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_add_address)

        val name=findViewById<CustomEditText>(R.id.address_fullName)
        val mobNo=findViewById<CustomEditText>(R.id.address_mobileNo)
        val pc=findViewById<CustomEditText>(R.id.address_pincode)
        val city=findViewById<CustomEditText>(R.id.address_city)
        val state=findViewById<CustomEditText>(R.id.address_state)
        val location=findViewById<CustomEditText>(R.id.address_houseNo)
        val landmark=findViewById<CustomEditText>(R.id.address_area)
        val homeAdd=findViewById<CustomRadioButton>(R.id.address_homeAdd)
        val workAdd=findViewById<CustomRadioButton>(R.id.address_workAdd)

        if(intent.hasExtra(Constants.selectAddress)){
            selectAddress=intent.getBooleanExtra(Constants.EXTRA_ADDRESS_DETAILS,false)
        }
        if(intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)){
            editAdd=true
            findViewById<CustomTextView>(R.id.addAddress_toolbarTitle).text="EDIT ADDRESS"
            addressDetails=intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)

            name.setText(addressDetails!!.name)
            mobNo.setText(addressDetails!!.mobileNo)
            pc.setText(addressDetails!!.pin_code)
            city.setText(addressDetails!!.city)
            state.setText(addressDetails!!.state)
            location.setText(addressDetails!!.house_location)
            landmark.setText(addressDetails!!.landmark)

            if(addressDetails!!.address_type=="HOME"){
                homeAdd.isChecked=true
            }
            else{
                workAdd.isChecked=true
            }
        }
        findViewById<CustomButton>(R.id.address_saveAddress).setOnClickListener {
            if(validateAddressDetails()){
                if(!editAdd){
                    val addType=if(homeAdd.isChecked){
                        homeAdd.text.toString()
                    }else{
                        workAdd.text.toString()
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
                else{
                    val hash=HashMap<String,Any>()

                    hash["city"]=city.text.toString()
                    hash["house_location"]=location.text.toString()
                    hash["landmark"]=landmark.text.toString()
                    hash["mobileNo"]=mobNo.text.toString()
                    hash["name"]=name.text.toString()
                    hash["pin_code"]=pc.text.toString()
                    hash["state"]=state.text.toString()

                    FirestoreClass().editAddress(this,hash,addressDetails!!.id)
                }
            }
        }
    }
    fun addressAddedSuccessfully(){
        onBackPressed()
    }

    override fun onBackPressed() {
        val intent= Intent(this,AddressActivity::class.java)
        intent.putExtra(Constants.selectAddress,selectAddress)
        startActivity(intent)
        finish()
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