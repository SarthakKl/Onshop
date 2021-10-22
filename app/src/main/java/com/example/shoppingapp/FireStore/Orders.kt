package com.example.shoppingapp.FireStore

import android.os.Parcel
import android.os.Parcelable

class Orders(
    var id:String="",
    var user_id:String="",
    var products: ArrayList<Cart> =ArrayList(),
    var addressId: Address? =Address(),
    val image:String="",
    val title:String="",
    val subTotal:String="",
    val shippingCharge:String="",
    val total:String="",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(Cart.CREATOR) as ArrayList<Cart>,
        parcel.readParcelable(Address::class.java.classLoader),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(user_id)
        parcel.writeTypedList(products)
        parcel.writeParcelable(addressId, flags)
        parcel.writeString(image)
        parcel.writeString(title)
        parcel.writeString(subTotal)
        parcel.writeString(shippingCharge)
        parcel.writeString(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Orders> {
        override fun createFromParcel(parcel: Parcel): Orders {
            return Orders(parcel)
        }

        override fun newArray(size: Int): Array<Orders?> {
            return arrayOfNulls(size)
        }
    }

}