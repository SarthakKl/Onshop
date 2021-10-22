package com.example.shoppingapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.Activities.TheMainActivity.Fragments.ShopFragment
import com.example.shoppingapp.Activities.TheMainActivity.TheMainActivity
import com.example.shoppingapp.FireStore.*
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CommonMethodsClass
import com.example.shoppingapp.Utils.CustomButton
import com.example.shoppingapp.Utils.CustomTextView

class OrderActivity : CommonMethodsClass() {
    private lateinit var address: Address
    private lateinit var mProductList:ArrayList<Products>
    private lateinit var cartList:ArrayList<Cart>
    private var amount=0
    private var shippingCharge=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        setUpActionBar()
        if(intent.hasExtra(Constants.selectedAddress)){
            address= intent.getParcelableExtra(Constants.selectedAddress)!!
            findViewById<CustomTextView>(R.id.order_name).text=address.name
            findViewById<TextView>(R.id.order_addressType).text=address.address_type
            findViewById<CustomTextView>(R.id.order_address).text=getString(R.string.address,address.house_location,address.landmark,address.city,address.state,address.pin_code)
            findViewById<CustomTextView>(R.id.order_mobNo).text=address.mobileNo
        }
        findViewById<CustomButton>(R.id.order_placeOrder).setOnClickListener {
            showProgressBar()
            val order=Orders(
                "",
                cartList[0].userId,
                cartList,
                address,
                cartList[0].image,
                "Order No.#"+System.currentTimeMillis(),
                amount.toString(),
                shippingCharge.toString(),
                (amount+shippingCharge).toString()
            )
            FirestoreClass().orderProduct(this,order)
        }
        getCartDetails()
    }
    fun orderProductSuccess(){
        updateStock()
    }
    private fun updateStock(){
        FirestoreClass().updateStock(this,cartList)
    }
    fun updateStockSuccess(){
        hideProgressBar()

        val intent= Intent(this,TheMainActivity::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
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

            val rv=findViewById<RecyclerView>(R.id.order_rv)
            rv.visibility= View.VISIBLE

            val subtotalVal=findViewById<CustomTextView>(R.id.order_subtotalValue)
            val shippingVal=findViewById<CustomTextView>(R.id.order_shippingChargeValue)
            val totalAmount=findViewById<CustomTextView>(R.id.order_totalValue)



            for(i in cartList){
                if(i.stockQuantity>0){
                    amount+=i.cartQuantity*i.price.toInt()
                }
            }
            shippingCharge=(amount/100)*2
            subtotalVal.text=getString(R.string.total,amount.toString())
            shippingVal.text=getString(R.string.total,shippingCharge.toString())
            totalAmount.text=getString(R.string.total,(amount+shippingCharge).toString())

            if(amount==0){
                findViewById<CustomButton>(R.id.order_placeOrder).visibility=View.GONE
                findViewById<RelativeLayout>(R.id.order_payment_method).visibility=View.GONE
            }
            rv.adapter=CartActivityRvAdapter(cartList,this)
            rv.layoutManager= LinearLayoutManager(this)


    }
    private fun setUpActionBar() {
        setSupportActionBar(findViewById(R.id.order_toolbar))
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        findViewById<Toolbar>(R.id.order_toolbar).setNavigationOnClickListener { onBackPressed() }
    }
}