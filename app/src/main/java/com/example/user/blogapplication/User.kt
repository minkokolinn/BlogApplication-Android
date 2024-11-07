package com.example.user.blogapplication

import android.os.Parcel
import android.os.Parcelable

class User() : Parcelable{
    var name:String?=null
    var userimg:String?=null
    var userid:String?=null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        userimg = parcel.readString()
        userid = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(userimg)
        parcel.writeString(userid)
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