package com.mwdch.bookshop.model

import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("index")
    val id: String,
    val image: String
)