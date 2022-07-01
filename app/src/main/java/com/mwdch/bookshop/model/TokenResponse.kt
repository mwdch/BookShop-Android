package com.mwdch.bookshop.model

data class TokenResponse(
    val token: String,
    val id: String,
    val username: String
)