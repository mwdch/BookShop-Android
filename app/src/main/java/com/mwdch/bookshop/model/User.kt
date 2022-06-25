package com.mwdch.bookshop.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String,
    var firstname: String,
    var lastname: String,
    val username: String,
    val password: String,
    var phone: String,
)