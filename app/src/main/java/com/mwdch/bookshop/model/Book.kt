package com.mwdch.bookshop.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    @SerializedName("_id")
    val id: String,
    var name: String,
    val description: String,
    val image: String,
    val price: Int,
    val pages: Int,
    val volumes: Int,
    val author: String,
    val category: Int,
) : Parcelable