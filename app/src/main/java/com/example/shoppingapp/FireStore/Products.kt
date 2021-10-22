package com.example.shoppingapp.FireStore

import android.os.Parcel
import android.os.Parcelable

class Products(
    var id:String="",
    var productName:String="",
    var price:Int=0,
    var quantity:Int=0,
    var user_id:String="",
    var sellerName:String="",
    var offers:String="",
    var description:String="",
    var productImage:String=""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(productName)
        parcel.writeInt(price)
        parcel.writeInt(quantity)
        parcel.writeString(user_id)
        parcel.writeString(sellerName)
        parcel.writeString(offers)
        parcel.writeString(description)
        parcel.writeString(productImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Products> {
        override fun createFromParcel(parcel: Parcel): Products {
            return Products(parcel)
        }

        override fun newArray(size: Int): Array<Products?> {
            return arrayOfNulls(size)
        }
    }

}


