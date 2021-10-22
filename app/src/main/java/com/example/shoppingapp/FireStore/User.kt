package com.example.shoppingapp.FireStore

import android.os.Parcel
import android.os.Parcelable

class User (
    var id:String="",
    var First_Name:String="",
    var Last_Name: String="",
    var email: String="",
    var mobile:Long=0,
    var gender:String="",
    var Image: String="",
    var profileComplete:Int=0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(First_Name)
        parcel.writeString(Last_Name)
        parcel.writeString(email)
        parcel.writeLong(mobile)
        parcel.writeString(gender)
        parcel.writeString(Image)
        parcel.writeInt(profileComplete)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}
