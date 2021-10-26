package com.example.shoppingapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.FireStore.Address
import com.example.shoppingapp.FireStore.Constants
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CommonMethodsClass
import com.example.shoppingapp.Utils.CustomTextView
import com.example.shoppingapp.Utils.SwipeToEditCallback

class AddressActivity : CommonMethodsClass(),View.OnClickListener {
    private var selectAddress:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)


        if(intent.hasExtra(Constants.selectAddress)){
            findViewById<CustomTextView>(R.id.address_toolbarTv).text="SELECT ADDRESS"
            selectAddress=true
        }
        findViewById<CardView>(R.id.address_AddAddress).setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        getUserAddress()
    }
    private fun getUserAddress(){
        FirestoreClass().getUserAddress(this,FirestoreClass().getCurrentUserId())
    }
    fun retrievedUserAddressSuccessfully(addresses:ArrayList<Address>){
        if(addresses.size>0){
            val addressRv=findViewById<RecyclerView>(R.id.address_rv)
            findViewById<TextView>(R.id.address_noAddressMsg).visibility= View.GONE

            addressRv.adapter=AddressActivityRvAdapter(this,addresses,selectAddress)
            addressRv.layoutManager=LinearLayoutManager(this)

            val editSwipeHandler=object: SwipeToEditCallback(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = addressRv.adapter as AddressActivityRvAdapter
                    adapter.notifyEditItem(
                        this@AddressActivity,
                        viewHolder.absoluteAdapterPosition
                    )
                }
            }

            val editItemTouchHelper= ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(addressRv)
        }
        else{
            findViewById<RecyclerView>(R.id.address_rv).visibility=View.GONE

        }
    }

    override fun onClick(view: View?) {
        if(view!!.id==R.id.address_AddAddress) {
            val intent=Intent(this, AddAddressActivity::class.java)
            intent.putExtra(Constants.selectAddress,selectAddress)
            startActivity(intent)
            finish()
        }
    }

}