package com.example.shoppingapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.FireStore.Cart
import com.example.shoppingapp.FireStore.Constants
import com.example.shoppingapp.FireStore.Orders
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CustomTextView

class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var orderDetail: Orders
    private lateinit var cart:ArrayList<Cart>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        if(intent.hasExtra(Constants.orderDetails)){
            orderDetail=intent.getParcelableExtra(Constants.orderDetails)!!
            cart=orderDetail.products

            findViewById<CustomTextView>(R.id.orderDetails_title).text=orderDetail.title
            val rv=findViewById<RecyclerView>(R.id.orderDetails_rv)
            rv.adapter=CartActivityRvAdapter(cart,this)
            rv.layoutManager=LinearLayoutManager(this)

            findViewById<CustomTextView>(R.id.orderDetails_name).text=
                orderDetail.addressId?.name ?: ""
            findViewById<TextView>(R.id.orderDetails_addressType).text=orderDetail.addressId!!.address_type

            findViewById<CustomTextView>(R.id.orderDetails_address).text=getString(R.string.address,orderDetail.addressId!!.house_location,orderDetail.addressId!!.landmark,orderDetail.addressId!!.city,orderDetail.addressId!!.state,orderDetail.addressId!!.pin_code)

            findViewById<CustomTextView>(R.id.orderDetails_mobNo).text=orderDetail.addressId!!.mobileNo

            findViewById<CustomTextView>(R.id.orderDetails_subtotalValue).text=getString(R.string.total,orderDetail.subTotal)

            findViewById<CustomTextView>(R.id.orderDetails_shippingChargeValue).text=getString(R.string.total,orderDetail.shippingCharge)

            findViewById<CustomTextView>(R.id.orderDetails_totalValue).text=getString(R.string.total,orderDetail.total)
        }
    }
}