package com.mwdch.bookshop.model

import com.google.gson.annotations.SerializedName

data class Cart(
    @SerializedName("_id")
    val _id: String,
    val book: Book,
    val quantity: Int
)