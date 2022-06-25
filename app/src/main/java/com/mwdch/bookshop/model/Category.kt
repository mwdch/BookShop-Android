package com.mwdch.bookshop.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("index")
    val id: Int,
    val name: String,
    val icon: String
) : Parcelable