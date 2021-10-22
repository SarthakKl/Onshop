package com.example.shoppingapp.Activities.TheMainActivity.Fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.FireStore.FirestoreClass
import com.example.shoppingapp.FireStore.Products
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.GlideLoader

class ProductFragmentAdapter(val context: Context,val items:ArrayList<Products>,val fragment: ProductsFragment)
    : RecyclerView.Adapter<ProductFragmentAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val productName: TextView =view.findViewById(R.id.pf_recyclerView_productName)
        val productPrice: TextView =view.findViewById(R.id.pf_recyclerView_productPrice)
        val productImage: ImageView =view.findViewById(R.id.pf_recyclerView_productImage)
        val delete:ImageView=view.findViewById(R.id.pf_recyclerView_deleteItem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.product_fragment_recyclerview_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item=items[position]
        holder.productName.text=item.productName
        GlideLoader(context).loadProductPicture(item.productImage,holder.productImage)
        holder.productPrice.text="₹ "+item.price.toString()
        holder.delete.setOnClickListener {
            fragment.showAlertDialog(item.id)
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
}