package com.example.shoppingapp.FireStore

import android.os.Parcel
import android.os.Parcelable

class Address (
    val user_id:String="",
    val name:String="",
    val mobileNo:String="",
    val pin_code:String="",
    val state:String="",
    val city:String="",
    val house_location:String="",
    val landmark:String="",
    val address_type:String="",
    var id:String=""
        ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user_id)
        parcel.writeString(name)
        parcel.writeString(mobileNo)
        parcel.writeString(pin_code)
        parcel.writeString(state)
        parcel.writeString(city)
        parcel.writeString(house_location)
        parcel.writeString(landmark)
        parcel.writeString(address_type)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Address> {
        override fun createFromParcel(parcel: Parcel): Address {
            return Address(parcel)
        }

        override fun newArray(size: Int): Array<Address?> {
            return arrayOfNulls(size)
        }
    }

}