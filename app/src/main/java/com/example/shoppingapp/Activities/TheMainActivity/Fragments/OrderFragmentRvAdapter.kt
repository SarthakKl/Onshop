package com.example.shoppingapp.Activities.TheMainActivity.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.Activities.OrderDetailsActivity
import com.example.shoppingapp.FireStore.Constants
import com.example.shoppingapp.FireStore.Orders
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CustomTextView
import com.example.shoppingapp.Utils.GlideLoader

class OrderFragmentRvAdapter(val activity: Activity, val items:ArrayList<Orders>):RecyclerView.Adapter<OrderFragmentRvAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.order_rv_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=items[position]

        holder.itemView.findViewById<CustomTextView>(R.id.orderFrag_orderTitle).text=item.title
        holder.itemView.findViewById<CustomTextView>(R.id.orderFrag_totalAmount).text=activity.getString(R.string.total,item.total)
        GlideLoader(activity).loadProductPicture(item.image,holder.itemView.findViewById(R.id.orderFrag_productImage))

        holder.itemView.findViewById<CardView>(R.id.orderFrag_cardView).setOnClickListener{
            val intent=Intent(activity,OrderDetailsActivity::class.java)
            intent.putExtra(Constants.orderDetails,item)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}