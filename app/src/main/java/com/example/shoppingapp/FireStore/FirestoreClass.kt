package com.example.shoppingapp.FireStore

import android.app.Activity
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.webkit.MimeTypeMap
import android.content.ContentResolver
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.shoppingapp.Activities.*
import com.example.shoppingapp.Activities.TheMainActivity.Fragments.OrdersFragment
import com.example.shoppingapp.Activities.TheMainActivity.Fragments.ProductsFragment
import com.example.shoppingapp.Activities.TheMainActivity.Fragments.ShopFragment
import com.google.firebase.firestore.DocumentSnapshot


class FirestoreClass:AppCompatActivity(){

    /**
    * Used in RegisterActivity
    * For adding the user details to the User Collection in the Firestore*/
    fun registerUser(activity: RegisterActivity, userinfo:User){
        val db= FirebaseFirestore.getInstance()

        db.collection(Constants.users)
            .document(userinfo.id)
            .set(userinfo, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(activity, "Registration Successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Error registering User", Toast.LENGTH_SHORT).show()
            }
    }
    /**
    * Used in AddProductActivity
    * For creating a new document in the product collection and adding the details of the product
    * in the Firestore.*/
    fun addProductToDatabase(activity:Activity,product:Products){
        val db=FirebaseFirestore.getInstance()

        db.collection(Constants.products).document()
            .set(product, SetOptions.merge())
            .addOnSuccessListener {
                when(activity){
                    is AddProductActivity->{
                        activity.productDetailsUploadSuccess()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
            }
    }
    /**
    * Used by various methods
    * For getting the userId of the Logged in user that was given while registering the user
    * (FirebaseAuth)*/
    fun getCurrentUserId():String{
        val currentUser=FirebaseAuth.getInstance().currentUser
        var currentUserId=""
        if(currentUser!=null)
            currentUserId=currentUser.uid

        return currentUserId
    }
    /**Used By RegisterActivity and LoginActivity
    * For getting the details of the logged in user stored in the user collection,
    * FirebaseFirestore */
    fun getUserDetails(activity: Activity){
        val db=FirebaseFirestore.getInstance()
        val docRef = db.collection(Constants.users).document(getCurrentUserId())
        docRef.get()
            .addOnSuccessListener { document ->
                successListenerGetUserDetails(activity,document)
            }
            .addOnFailureListener { exception ->
                if(activity is RegisterActivity )
                    activity.hideProgressBar()
                else if(activity is LoginActivity )
                    activity.hideProgressBar()
                Toast.makeText(activity, "Error retrieving the document with exception $exception", Toast.LENGTH_SHORT).show()
            }

    }
    /**
    Used by getUserDetails
    FirestoreClass Method
    */
    private fun successListenerGetUserDetails(activity: Activity,document:DocumentSnapshot?){
        if (document != null) {

            val user=document.toObject(User::class.java)        //converting the document to user type
            val sharedPreference:SharedPreferences= PreferenceManager
                .getDefaultSharedPreferences(activity)

            val editor:SharedPreferences.Editor=sharedPreference.edit()

            if (user != null) {
                editor.putString(Constants.sharedPreferenceUserName,"${user.First_Name} ${user.Last_Name}")
                editor.apply()
            }
            when(activity){
                is LoginActivity->{
                    activity.hideProgressBar()
                    activity.logInSuccessful(user!!)
                }
                is RegisterActivity->{
                    activity.hideProgressBar()
                    activity.logInSuccessful()
                }
                is SettingActivity->{
                    activity.getUserDetails(user!!)
                }
            }

        } else {
            if(activity is RegisterActivity )
                activity.hideProgressBar()
            else if(activity is LoginActivity )
                activity.hideProgressBar()
            Toast.makeText(activity, "No such User found", Toast.LENGTH_SHORT).show()
        }
    }
    /**
    * Used by CompleteProfileActivity and AddProductActivity
    * used for uploading the image of the user and the product
    * FirebaseStorage */
    fun uploadImageToFirebaseStorage(activity:Activity,imageUri: Uri,header:String){
        val cR: ContentResolver = activity.contentResolver
        val type = MimeTypeMap.getSingleton().getExtensionFromMimeType(cR.getType(imageUri))


        // A reference can be thought of as a pointer to a file in the storage cloud
        val sref:StorageReference=FirebaseStorage.getInstance().reference.child(
            header+System.currentTimeMillis()+"."+type
        )
        sref.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot->
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { url->
                        Log.e("Image Upload","Image Successfully Uploaded")
                        when (activity) {
                            is CompleteProfile -> {
                                activity.profilePictureUploaded(url)
                            }
                            is AddProductActivity->{
                                activity.productImageUploadSuccess(url)
                            }
                        }
                    }
            }
            .addOnFailureListener{exception->
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT).show()
            }
    }
    /**
    * Used By CompleteProfileActivity
    * For updating the details of user in the Firestore collection(user collection)
    * FirebaseFirestore*/
    fun updateUserDetails(activity:Activity,hash:HashMap<String,Any>){
        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.users).document(getCurrentUserId())
            .update(hash)
            .addOnFailureListener {
                Toast.makeText(activity, "Update failed", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                when(activity){
                    is CompleteProfile->{
                        activity.updateSuccess()
                    }
                }
            }
    }
    /**
     * Used by getProductsSoldByUser(this class) method
     * FirebaseFirestore*/
    private fun updateTheProductId(productId:String){
        val hash=HashMap<String,Any>()
        hash[Constants.productId]=productId

        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.products)
            .document(productId)
            .update(hash)
            .addOnSuccessListener {
                Log.e("updating product id","Product id updated in the Firestore")
            }
            .addOnFailureListener {
                Log.e("updating product id",it.message.toString())
            }
    }
    /**
    * Used by ProductsFragment
    * For retrieving the products added by the user to the shop or the products collection in Firestore
    * FirebaseFirestore*/
    fun getProductsSoldByUser(fragment:Fragment){
        val db=FirebaseFirestore.getInstance()

        db.collection(Constants.products)
            .whereEqualTo(Constants.userId,getCurrentUserId())
            .get()
            .addOnSuccessListener {

                val products = ArrayList<Products>()
                val document = it.documents

                for (i in document) {
                    val product = i.toObject(Products::class.java)!!
                    product.id=i.id
                    updateTheProductId(i.id)
                    products.add(product)
                }
                when (fragment) {
                    is ProductsFragment -> {
                        fragment.productDetailsFetchSuccess(products)
                    }
                }

            }
            .addOnFailureListener {
                when(fragment){
                    is ProductsFragment->{
                        fragment.hideProgressDialog()
                        Log.e("Error getting Document",it.message.toString())
                    }
                }
            }
    }
    /**Used by ShopFragment
    * For retrieving all the products details in the products collection of Firestore
    * FirebaseFirestore*/
    fun getAllProducts(fragment:Fragment){
        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.products)
            .get()
            .addOnSuccessListener {
                val docs=it.documents
                val products=ArrayList<Products>()

                for(i in docs){
                    val x=i.toObject(Products::class.java)
                    products.add(x!!)
                }
                when(fragment){
                    is ShopFragment ->{
                        fragment.productDetailsFetchSuccess(products)
                    }
                }
            }
            .addOnFailureListener {
                when(fragment){
                    is ShopFragment->{
                        fragment.hideProgressDialog()
                    }
                }
                Log.e("Fragment $fragment",it.message.toString())
            }
    }
    /**
     * Used by ProductsFragment(Delete Button)
     * For deleting the document(product) in the products collection that is added by the logged in user
     * FirebaseFirestore
     */
    fun deleteProduct(fragment:ProductsFragment,productId:String){
        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.products)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSuccess()
            }
            .addOnFailureListener {
                fragment.hideProgressDialog()
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
    /**
     * Used by ProductDetailsActivity
     * For creating a new document with the provided fields(Cart class in the Firestore in the the
     * Cart collection
     * FirebaseFirestore*/
    fun addProductsToCart(activity:ProductDetailsActivity,item:Cart,directOrder:Boolean){
        val db=FirebaseFirestore.getInstance()

        db.collection(Constants.cart).document()
            .set(item, SetOptions.merge())
            .addOnSuccessListener {
                activity.addProductToCartSuccess(directOrder)
            }
            .addOnFailureListener {
                activity.hideProgressBar()
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
    }
    /**
     * Used by
     * For updating the stock of the product if the product is added in cart by the user
     * FirebaseFirestore
     * */
    fun updateStockOfProduct(activity: Activity,productId:String,newQuantity:Int){
        val hash=HashMap<String,Any>()
        hash[Constants.productQuantity]=newQuantity

        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.products)
            .document(productId)
            .update(hash)
            .addOnSuccessListener {
                Log.e("updateStockOfProduct","Product Stock Updated")
            }
            .addOnFailureListener {
                Log.e("updateStockOfProduct",it.message.toString())
            }

    }
    /**
     * Used by ProductDetailsActivity
     * For checking if the product that we are looking is already in cart or not
     * FirebaseFirestore*/
    fun checkIfAlreadyInCart(activity:ProductDetailsActivity,productId: String,userId:String){
        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.cart)
            .whereEqualTo(Constants.cartUserId,userId)
            .whereEqualTo(Constants.cartProductId,productId)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty){
                    Log.e("CheckInCart","true")
                    activity.itemCheckInCartSuccess(true)
                }
                else{
                    Log.e("CheckInCart","false")
                    activity.itemCheckInCartSuccess(false)
                }
            }
            .addOnFailureListener {
                activity.itemCheckInCartSuccess(false)
                Log.e("CheckIfAlreadyInCart",it.message.toString())
            }

    }
    /**
     * Used by CartRvAdapter
     * For deleting item(document) from cart collection
     * FirebaseFirestore
     */
    fun removeItemFromCart(context: Context, cartId:String){
        val db=FirebaseFirestore.getInstance()

        db.collection(Constants.cart)
            .document(cartId)
            .delete()
            .addOnSuccessListener {

                when(context) {
                   is CartActivity->{
                       context.removeItemFromCartSuccess()
                   }
                }
            }
            .addOnFailureListener {
                Log.e("RemovingItemFromCart",it.message.toString())
            }
    }
    /**
     * Used by CartActivity
     * For getting all the products in the shop for comparing the stock of the product that is in cart
     * FirebaseFirestore*/
    fun getAllProductsForStockCheck(activity:Activity){
        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.products)
            .get()
            .addOnSuccessListener {
                val docs=it.documents
                val products=ArrayList<Products>()

                for(i in docs){
                    val x=i.toObject(Products::class.java)
                    products.add(x!!)
                }
                when(activity){
                    is CartActivity ->{
                        activity.productDetailsFetchSuccess(products)
                    }
                    is OrderActivity->{
                        activity.productDetailsFetchSuccess(products)
                    }
                }
            }
            .addOnFailureListener {
                when(activity){
                    is CartActivity->{
                        activity.hideProgressBar()
                    }
                }
                Log.e("Fragment $activity",it.message.toString())
            }
    }
    /**
     * Used by CartRvAdapter
     * For updating(increasing and decreasing) the cartQuantity
     * FirebaseFirestore*/
    fun updateCartQuantity(activity:Context,pid:String,hash: HashMap<String, Any>){
        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.cart)
            .document(pid)
            .update(hash)
            .addOnSuccessListener {
                if(activity is CartActivity)
                    activity.changeCartQuantitySuccess()
            }
            .addOnFailureListener {
                Log.e("Error updateQuantity",it.message.toString())
            }
    }

    private fun updateCartId(cartId:String){
        val hash=HashMap<String,Any>()

        hash[Constants.cartId]=cartId
        FirebaseFirestore.getInstance().collection(Constants.cart).document(cartId)
            .update(hash)
            .addOnSuccessListener {
                Log.e("updateCartId","Id of the cart updated")
            }
            .addOnFailureListener {
                Log.e("updateCartId",it.message.toString())
            }
    }
    /**
     * Used by CartActivity
     * For getting the documents from Cart collection for a specific user
     * FirebaseFirestore
     * */
    fun getCartDetails(activity:Activity){
        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.cart)
            .whereEqualTo(Constants.cartUserId,getCurrentUserId())
            .get()
            .addOnSuccessListener {
                val document=it.documents
                val itemsInCart=ArrayList<Cart>()

                for(i in document){
                    val item=i.toObject(Cart::class.java)
                    updateCartId(i.id)
                    item!!.id=i.id
                    itemsInCart.add(item)
                }
                when(activity) {

                    is CartActivity-> {
                        activity.cartDetailsFetchSuccess(itemsInCart)
                    }
                    is OrderActivity->{
                        activity.cartDetailsFetchSuccess(itemsInCart)
                    }
                }
            }
            .addOnFailureListener {
                Log.e("Cart Details",it.message.toString())
            }
    }
    /**
     * Used by AddAddressActivity
     * For creating a new document in the Addresses collection
     * FirebaseFirestore*/
    fun addAddress(activity:Activity,add:Address){
        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.address)
            .document()
            .set(add, SetOptions.merge())
            .addOnSuccessListener{
                when(activity){
                    is AddAddressActivity->{
                        activity.addressAddedSuccessfully()
                    }
                }
            }
            .addOnFailureListener {
                Log.e("addAddress",it.message.toString())
            }
    }
    private fun updateAddressId(id:String){
        val hash=HashMap<String,Any>()
        hash[Constants.id]=id

        FirebaseFirestore.getInstance().collection(Constants.address)
            .document(id)
            .update(hash)
            .addOnSuccessListener {
                Log.e("updateAddressId","Success")
            }
            .addOnFailureListener {
                Log.e("updateAddressId","Failure")
            }
    }
    /**
     * Used by AddressActivity
     * For getting the addres of the user
     * FirebaseFirestore*/
    fun getUserAddress(activity:Activity,userId:String){
        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.address)
            .whereEqualTo(Constants.userId,userId)
            .get()
            .addOnSuccessListener {
                    val documents=it.documents
                    val addresses=ArrayList<Address>()

                    for(i in documents){
                        val address=i.toObject(Address::class.java)
                        address!!.id=i.id
                        updateAddressId(i.id)
                        addresses.add(address)
                    }
                    when(activity){
                        is AddressActivity->{
                            activity.retrievedUserAddressSuccessfully(addresses)
                        }
                    }

            }
            .addOnFailureListener {
                Log.e("getUserAddress",it.message.toString())
            }

    }
    fun editAddress(activity:AddAddressActivity,editedAdd:HashMap<String,Any>,id:String){
        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.address)
            .document(id)
            .update(editedAdd)
            .addOnSuccessListener {
                activity.addressAddedSuccessfully()
            }
            .addOnFailureListener {
                Log.e("editAddress","edit Failed")
            }
    }
    fun updateStock(activity: OrderActivity,cartList:ArrayList<Cart>){
        val db=FirebaseFirestore.getInstance()
        val writeBatch=db.batch()

        for(cartItem in cartList) {
            val productHashMap=HashMap<String,Any>()
            productHashMap[Constants.productQuantity]=cartItem.stockQuantity-cartItem.cartQuantity

            val docReference=db.collection(Constants.products)
                .document(cartItem.productId)

            writeBatch.update(docReference,productHashMap)
        }
        for(cartItem in cartList){
            val docReference=db.collection(Constants.cart)
                .document(cartItem.id)

            writeBatch.delete(docReference)
        }
        writeBatch.commit().addOnSuccessListener {
            activity.updateStockSuccess()
        }
            .addOnFailureListener {
                Log.e("updateStock",it.message.toString())
            }
    }
    fun getOrderedProducts(fragment:Fragment){
        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.Order)
            .whereEqualTo(Constants.userId,getCurrentUserId())
            .get()
            .addOnSuccessListener {
                val docs=it.documents
                val orders=ArrayList<Orders>()

                for(i in docs){
                    val order=i.toObject(Orders::class.java)
                    order!!.id=i.id
                    orders.add(order)
                }
                when(fragment){
                    is OrdersFragment->{
                        fragment.orderedProductFetchSuccess(orders)
                    }
                }
            }
            .addOnFailureListener {
                Log.e("getOrderedProducts",it.message.toString())
            }
    }
    fun orderProduct(activity:Activity,order:Orders){
        val db=FirebaseFirestore.getInstance()
        db.collection(Constants.Order)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                when(activity){
                    is OrderActivity->{
                        activity.orderProductSuccess()
                    }
                }
            }
            .addOnFailureListener {
                Log.e("orderProduct ",it.message.toString())
            }
    }

    fun getSellerName(activity: AddProductActivity,id:String){
    val db=FirebaseFirestore.getInstance()
    db.collection(Constants.users)
        .document(id)
        .get()
        .addOnSuccessListener { document->
            val user=document.toObject(User::class.java)
            if(user!=null)
                activity.sellerNameFetchSuccess(user.First_Name+" "+user.Last_Name)
        }
        .addOnFailureListener {
            Log.e("Get Seller Name",it.message.toString())
        }

    }/*
    fun getSellerId(activity:CartActivity, productId:String){
    val db=FirebaseFirestore.getInstance()
    db.collection(Constants.products)
        .document(productId)
        .get()
        .addOnSuccessListener { document->

                val product=document.toObject(Products::class.java)

                activity.sellerIdFetchSuccess(product!!.user_id)

        }
        .addOnFailureListener {
            Log.e("User Details For Cart",it.message.toString())
        }
    }*/
}