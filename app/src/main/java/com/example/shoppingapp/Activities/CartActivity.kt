package com.example.shoppingapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.FireStore.*
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CommonMethodsClass
import com.example.shoppingapp.Utils.CustomButton
import com.example.shoppingapp.Utils.CustomTextView

class CartActivity : CommonMethodsClass() {

    //private var seller:String?=null
    private lateinit var mProductList:ArrayList<Products>
    private lateinit var cartList:ArrayList<Cart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setUpActionBar()
        findViewById<CustomButton>(R.id.cart_placeOrderBtn).setOnClickListener {
            if(checkForOutOfStockItem()){
                val intent=Intent(this,AddressActivity::class.java)
                intent.putExtra(Constants.selectAddress,true)
                startActivity(intent)
            }
        }
    }
    fun checkForOutOfStockItem():Boolean{
        for(i in cartList){
            if(i.stockQuantity==0) {
                showSnackBar(
                    "Please remove the Out Of Stock Item from cart before placing the order",
                    true
                )
                return false
            }
        }
        return true
    }
    override fun onResume() {
        super.onResume()
        getCartDetails()
    }
    private fun getCartDetails(){
        showProgressBar()
        FirestoreClass().getAllProductsForStockCheck(this)
    }
    fun productDetailsFetchSuccess(productList:ArrayList<Products>){
        mProductList=productList
        FirestoreClass().getCartDetails(this)
    }

    fun cartDetailsFetchSuccess(items:ArrayList<Cart>){
        hideProgressBar()
        cartList=items

        val ifDeleted= Array(cartList.size){0}
        var flag=0

        for(i in mProductList){
            for(j in cartList){
                if(i.id==j.productId){
                    j.stockQuantity=i.quantity
                    if(i.quantity==0){
                        j.cartQuantity=0
                    }
                    ifDeleted[flag]=1
                }
                ++flag
            }
            flag=0
        }
        while(flag<ifDeleted.size){
            if(ifDeleted[flag]!=1){
                cartList[flag].cartQuantity=0
                cartList[flag].stockQuantity=0
            }
            ++flag
        }
        if(cartList.size>0){
            val rv=findViewById<RecyclerView>(R.id.cart_rv)
            rv.visibility= View.VISIBLE
            val cartMsg=findViewById<TextView>(R.id.cart_msg)
            cartMsg.visibility=View.GONE

            val subtotalVal=findViewById<CustomTextView>(R.id.cart_subtotalValue)
            val shippingVal=findViewById<CustomTextView>(R.id.cart_shippingChargeValue)
            val totalAmount=findViewById<CustomTextView>(R.id.cart_totalValue)

            var amount=0

            for(i in cartList){
                if(i.stockQuantity>0){
                    amount+=i.cartQuantity*i.price.toInt()
                }
            }
            val shippingCharge=(amount/100)*2
            subtotalVal.text=getString(R.string.total,amount.toString())
            shippingVal.text=getString(R.string.total,shippingCharge.toString())
            totalAmount.text=getString(R.string.total,(amount+shippingCharge).toString())

            if(amount==0){
                findViewById<LinearLayout>(R.id.checkout_details).visibility=View.GONE
                findViewById<CustomButton>(R.id.cart_placeOrderBtn).visibility=View.GONE
                findViewById<View>(R.id.diversion).visibility=View.GONE
            }
            rv.adapter=CartActivityRvAdapter(cartList,this)
            rv.layoutManager=LinearLayoutManager(this)
        }
        else{
            findViewById<TextView>(R.id.cart_msg).visibility= View.VISIBLE
            findViewById<RecyclerView>(R.id.cart_rv).visibility=View.GONE
            findViewById<LinearLayout>(R.id.checkout_details).visibility=View.GONE
            findViewById<CustomButton>(R.id.cart_placeOrderBtn).visibility=View.GONE
            findViewById<View>(R.id.diversion).visibility=View.GONE
        }
    }
    fun removeItemFromCartSuccess(){
        hideProgressBar()
        Toast.makeText(this, "Item removed successfully", Toast.LENGTH_SHORT).show()
        getCartDetails()
    }
    fun changeCartQuantitySuccess(){
        getCartDetails()
    }
    private fun setUpActionBar() {
        setSupportActionBar(findViewById(R.id.cart_toolbar))
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        findViewById<Toolbar>(R.id.cart_toolbar).setNavigationOnClickListener { onBackPressed() }
    }
/*
    fun getSellerDetails(item: Cart):String?{
        FirestoreClass().getSellerId(this,item.productId)
        Log.e("seller",seller?:"")
        return seller
    }
    fun sellerIdFetchSuccess(sellerId:String){
        FirestoreClass().getSellerName(this,sellerId)
    }
    fun sellerNameFetchSuccess(sellerName:String){
        seller=sellerName
        Log.e("Seller Name",seller?:"")
    }*/
}