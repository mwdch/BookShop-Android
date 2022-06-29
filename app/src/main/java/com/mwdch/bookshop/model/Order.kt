package com.mwdch.bookshop.model

data class Order(
    val _id: String,
    val customer: String,
    val status: String,
    val books: List<Cart>
)