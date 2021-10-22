package com.example.shoppingapp.FireStore

import android.os.Parcel
import android.os.Parcelable

class Cart(
    var userId:String="",
    var productId:String="",
    var productName:String="",
    var price:String="",
    var image:String="",
    var cartQuantity:Int=0,
    var stockQuantity:Int=0,
    var id:String=""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeString(price)
        parcel.writeString(image)
        parcel.writeInt(cartQuantity)
        parcel.writeInt(stockQuantity)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cart> {
        override fun createFromParcel(parcel: Parcel): Cart {
            return Cart(parcel)
        }

        override fun newArray(size: Int): Array<Cart?> {
            return arrayOfNulls(size)
        }
    }

}