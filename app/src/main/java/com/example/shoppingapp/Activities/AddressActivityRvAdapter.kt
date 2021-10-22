package com.example.shoppingapp.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.FireStore.Address
import com.example.shoppingapp.FireStore.Cart
import com.example.shoppingapp.FireStore.Constants
import com.example.shoppingapp.R
import com.example.shoppingapp.Utils.CustomTextView

class AddressActivityRvAdapter(private val context: Context,
                               private val addresses:ArrayList<Address>,
                                private val selectAddress:Boolean):RecyclerView.Adapter<AddressActivityRvAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.address_rv_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address=addresses[position]

        holder.itemView.findViewById<CustomTextView>(R.id.addressRv_name).text=address.name
        holder.itemView.findViewById<TextView>(R.id.addressRv_addressType).text=address.address_type
        holder.itemView.findViewById<TextView>(R.id.addressRv_mobNo).text=address.mobileNo
        holder.itemView.findViewById<TextView>(R.id.addressRv_address).text=
            context.getString(R.string.address,
                address.house_location,address.landmark,
                address.city,address.state,address.pin_code
            )
        if(selectAddress){
            holder.itemView.setOnClickListener {
                val intent= Intent(context,OrderActivity::class.java)
                intent.putExtra(Constants.selectedAddress,address)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return addresses.size
    }
}