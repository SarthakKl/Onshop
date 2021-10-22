package com.example.shoppingapp.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.FireStore.Cart
import com.example.shoppingapp.FireStore.Constants
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CustomTextView
import com.example.shoppingapp.Utils.GlideLoader
import org.w3c.dom.Text

class CartActivityRvAdapter( private val items:ArrayList<Cart>, private val context: Context):RecyclerView.Adapter<CartActivityRvAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.cart_rv_item_layout,parent,false)
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=items[position]
        val id=item.id
        //val seller=activity.getSellerDetails(item)?:""

        val quantity=holder.itemView.findViewById<CustomTextView>(R.id.cartRv_quantity)

        GlideLoader(context).loadProductPicture(item.image,holder.itemView.findViewById(R.id.cartRv_productImage))
        holder.itemView.findViewById<CustomTextView>(R.id.cartRv_productName).text=item.productName
        holder.itemView.findViewById<CustomTextView>(R.id.cartRv_productPrice).text=context.getString(R.string.total,item.price)

        if(context is CartActivity)
            quantity.text=item.cartQuantity.toString()
        else{
            quantity.text="Quantitiy: "+item.cartQuantity.toString()
        }
        //holder.itemView.findViewById<CustomTextView>(R.id.cart_product_seller).text=seller

        holder.itemView.findViewById<ImageView>(R.id.cartRv_removeFromCart).setOnClickListener {
            when(context ) {
                is CartActivity -> {
                    context.showProgressBar()
                }
                is OrderActivity -> {
                    context.showProgressBar()
                }
            }
            FirestoreClass().removeItemFromCart(context,item.id)
        }

        if(item.cartQuantity==0){
            holder.itemView.findViewById<ImageButton>(R.id.cartRv_increaseQuantity).visibility=View.GONE
            holder.itemView.findViewById<ImageButton>(R.id.cartRv_decrease_quantity).visibility=View.GONE

            quantity.text=context.getString(R.string.outOfStock)

            quantity.setTextColor(ContextCompat.getColor(context,R.color.SnackBar_error_red))
        }
        else if(context is CartActivity){
            holder.itemView.findViewById<ImageButton>(R.id.cartRv_increaseQuantity).visibility=View.VISIBLE
            holder.itemView.findViewById<ImageButton>(R.id.cartRv_decrease_quantity).visibility=View.VISIBLE
        }
        else if (context is OrderActivity){
            holder.itemView.findViewById<ImageButton>(R.id.cartRv_increaseQuantity).visibility=View.GONE
            holder.itemView.findViewById<ImageButton>(R.id.cartRv_decrease_quantity).visibility=View.GONE
            holder.itemView.findViewById<ImageView>(R.id.cartRv_removeFromCart).visibility=View.GONE
        }
        else if (context is OrderDetailsActivity){
            holder.itemView.findViewById<ImageButton>(R.id.cartRv_increaseQuantity).visibility=View.GONE
            holder.itemView.findViewById<ImageButton>(R.id.cartRv_decrease_quantity).visibility=View.GONE
            holder.itemView.findViewById<ImageView>(R.id.cartRv_removeFromCart).visibility=View.GONE
        }
        holder.itemView.findViewById<ImageButton>(R.id.cartRv_increaseQuantity).setOnClickListener {
            val cartQuantity=item.cartQuantity
            if(item.cartQuantity<item.stockQuantity){
                val hash=HashMap<String,Any>()
                hash[Constants.cartQuantity]=(cartQuantity+1)
                FirestoreClass().updateCartQuantity(context,id,hash)
            }
            else{
                if(context is CartActivity)
                    context.showSnackBar("Stock Limit Reached",true)
            }
        }
        holder.itemView.findViewById<ImageButton>(R.id.cartRv_decrease_quantity).setOnClickListener {

            val cartQuantity=item.cartQuantity
            if(cartQuantity==1){
                FirestoreClass().removeItemFromCart(context,id)
            }
            else{
                val hash=HashMap<String,Any>()
                hash[Constants.cartQuantity]=(cartQuantity-1)
                FirestoreClass().updateCartQuantity(context,item.id,hash)
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}