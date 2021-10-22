package com.example.shoppingapp.Activities

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import com.example.shoppingapp.FireStore.Constants
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.FireStore.Products
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CommonMethodsClass
import com.example.shoppingapp.Utils.CustomButton
import com.example.shoppingapp.Utils.CustomEditText
import com.example.shoppingapp.Utils.GlideLoader

class AddProductActivity : CommonMethodsClass() {
    private var productImageUri:Uri?=null
    private var productImageUrl:String?=null
    private var currentUserId:String?=null
    private var sellerName:String?=null

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        if(uri!=null){
            productImageUri=uri
            GlideLoader(this).loadPicture(uri,findViewById(R.id.addProduct_productImage))
        }
        else{
            Toast.makeText(this, "Error fetching image", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setUpActionBar()

        findViewById<ImageView>(R.id.addProduct_AddImage).setOnClickListener {
            getContent.launch("image/*")
        }
        findViewById<CustomButton>(R.id.addProduct_AddBtn).setOnClickListener{
            if(validateProductDetails()){
                showProgressBar()

                FirestoreClass().uploadImageToFirebaseStorage(this,productImageUri!!, Constants.productImageHeader)
            }
        }
    }
    fun productImageUploadSuccess(url:Uri){
        productImageUrl=url.toString()
        currentUserId=FirestoreClass().getCurrentUserId()
        FirestoreClass().getSellerName(this,currentUserId!!)
    }
    fun sellerNameFetchSuccess(name:String){
        sellerName=name
        uploadProductDetails()
    }
    private fun uploadProductDetails(){
        val productName=findViewById<CustomEditText>(R.id.addProduct_ProductName).text.toString()
        val productPrice=findViewById<CustomEditText>(R.id.addProduct_Price).text.toString().toInt()
        val quantity=findViewById<CustomEditText>(R.id.addProduct_Quantity).text.toString().toInt()
        val description=findViewById<CustomEditText>(R.id.addProduct_ProductDescription).text.toString()
        val offers=if(TextUtils.isEmpty(findViewById<CustomEditText>(R.id.addProduct_offers).text.toString())){
            ""
        }
        else{
            findViewById<CustomEditText>(R.id.addProduct_offers).text.toString()
        }

        val product=Products("",productName,productPrice,quantity,currentUserId!!,sellerName!!,offers,description,productImageUrl!!)

        FirestoreClass().addProductToDatabase(this,product)
    }
    fun productDetailsUploadSuccess(){
        hideProgressBar()
        Toast.makeText(this, "Thanks for adding your product to our shop", Toast.LENGTH_SHORT).show()
        onBackPressed()
    }
    private fun validateProductDetails():Boolean{
        return when{
            productImageUri==null->{
                showSnackBar("Please upload the image of the product",true)
                false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.addProduct_ProductName).text.toString().trim{it<=' '})->{
               showSnackBar("Please Enter the product name",true)
               false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.addProduct_Price).text.toString().trim{it<=' '})->{
                showSnackBar("Please enter the price of the product",true)
                false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.addProduct_Quantity).text.toString().trim{it<=' '})->{
                showSnackBar("Please enter the quantity of the product",true)
                false
            }
            TextUtils.isEmpty(findViewById<CustomEditText>(R.id.addProduct_ProductDescription).text.toString().trim{it<=' '})->{
                showSnackBar("Please enter the product description",true)
                false
            }
            else->{
                true
            }
        }
    }
    private fun setUpActionBar(){
        setSupportActionBar(findViewById(R.id.addProduct_toolbar))
        val actionBar=supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        findViewById<Toolbar>(R.id.addProduct_toolbar).setNavigationOnClickListener { onBackPressed() }
    }
}