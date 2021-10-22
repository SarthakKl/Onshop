package com.example.shoppingapp.Activities.TheMainActivity.Fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.Activities.ProductDetailsActivity
import com.example.shoppingapp.FireStore.Constants
import com.example.shoppingapp.FireStore.Products
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.GlideLoader

class ShopFragmentRvAdapter(val context: Context, val products:ArrayList<Products>)
    : RecyclerView.Adapter<ShopFragmentRvAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.shop_fragment_rvitem,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=products[position]
        GlideLoader(context).loadPicture(item.productImage,holder.itemView.findViewById<ImageView>(R.id.shopRv_ProductImage))
        holder.itemView.findViewById<TextView>(R.id.shopRv_productName).text=item.productName
        holder.itemView.findViewById<TextView>(R.id.shopRv_productPrice).text="₹ "+item.price
        holder.itemView.findViewById<View>(R.id.shopRv_view).setOnClickListener{
            val intent= Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra(Constants.productDetails,item)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)
}