package com.example.shoppingapp.Activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.example.shoppingapp.FireStore.*
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CommonMethodsClass
import com.example.shoppingapp.Utils.CustomButton
import com.example.shoppingapp.Utils.CustomTextView
import com.example.shoppingapp.Utils.GlideLoader

class ProductDetailsActivity : CommonMethodsClass() {
    private var productDetail: Products? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setUpActionBar()

        settingProductDetailsActivity()
        checkIfAlreadyInCart()

        findViewById<CustomButton>(R.id.productDetails_addToCart).setOnClickListener {
            showProgressBar()
            val itemInCart= Cart(FirestoreClass().getCurrentUserId(),productDetail!!.id,productDetail!!.productName,productDetail!!.price.toString(),productDetail!!.productImage,1,productDetail!!.quantity)
            FirestoreClass().addProductsToCart(this,itemInCart,false)
        }
        findViewById<CustomButton>(R.id.productDetails_goToCart).setOnClickListener {
            val intent= Intent(this,CartActivity::class.java)
            startActivity(intent)
        }
        findViewById<CustomButton>(R.id.productDetails_BuyNow).setOnClickListener {
            showProgressBar()
            val itemInCart= Cart(FirestoreClass().getCurrentUserId(),productDetail!!.id,productDetail!!.productName,productDetail!!.price.toString(),productDetail!!.productImage,1,productDetail!!.quantity)
            FirestoreClass().addProductsToCart(this,itemInCart,true)
        }
    }
    private fun checkIfAlreadyInCart(){
        showProgressBar()
        Log.e("productDetail", productDetail!!.id)
        FirestoreClass().checkIfAlreadyInCart(this,productDetail!!.id,productDetail!!.user_id)
    }
    fun itemCheckInCartSuccess(present:Boolean){
        hideProgressBar()
        if(present)
        {
            findViewById<CustomButton>(R.id.productDetails_BuyNow).visibility=View.GONE
            findViewById<CustomButton>(R.id.productDetails_addToCart).visibility=View.GONE
        }
    }
    fun addProductToCartSuccess(directOrder:Boolean){
        hideProgressBar()
        if(!directOrder)
            findViewById<CustomButton>(R.id.productDetails_addToCart).visibility=View.GONE
        else if(directOrder){
            val intent=Intent(this,AddressActivity::class.java)
            intent.putExtra(Constants.selectAddress,true)
            startActivity(intent)
        }
    }
    private fun settingProductDetailsActivity(){
        productDetail=intent.getParcelableExtra(Constants.productDetails)

        GlideLoader(this).loadPicture(productDetail!!.productImage,findViewById(R.id.productDetails_productImage))

        findViewById<CustomTextView>(R.id.productDetails_productName).text=productDetail!!.productName

        findViewById<CustomTextView>(R.id.productDetails_Price).text=getString(R.string.total,productDetail!!.price.toString())

        findViewById<CustomTextView>(R.id.productDetails_quantity).text="Quantity: "+productDetail!!.quantity.toString()

        findViewById<CustomTextView>(R.id.productDetails_sellerName).text="SOLD BY: "+productDetail!!.sellerName

        if(productDetail!!.quantity>0) {
            val stockTv = findViewById<CustomTextView>(R.id.productDetails_StockMsg)
            stockTv.text = Constants.inStockMsg
            stockTv.setTextColor(Color.parseColor("#00D100"))
        }

        else{
            findViewById<CustomButton>(R.id.productDetails_goToCart).visibility=View.GONE
            findViewById<CustomButton>(R.id.productDetails_addToCart).visibility=View.GONE
            findViewById<CustomButton>(R.id.productDetails_BuyNow).visibility=View.GONE

            val stockTv = findViewById<CustomTextView>(R.id.productDetails_StockMsg)
            stockTv.text = Constants.outOfStockMsg
            stockTv.setTextColor(Color.parseColor("#FF0000"))
        }
        findViewById<TextView>(R.id.productDetails_productDescription).text=productDetail!!.description

        if(TextUtils.isEmpty(productDetail!!.offers)){
            findViewById<TextView>(R.id.productDetails_offers).visibility= View.GONE
            findViewById<TextView>(R.id.productDetails_productOffers).visibility= View.GONE
            findViewById<CardView>(R.id.OffersCardView).visibility=View.GONE
        }
        else{
            findViewById<TextView>(R.id.productDetails_productOffers).text=productDetail!!.offers
        }
    }
    private fun setUpActionBar(){
        setSupportActionBar(findViewById(R.id.productDetails_toolbar))
        val actionBar=supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        findViewById<Toolbar>(R.id.productDetails_toolbar).setNavigationOnClickListener { onBackPressed() }
    }
}