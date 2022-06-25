package com.mwdch.bookshop

import android.content.SharedPreferences
import com.mwdch.bookshop.model.User

class UserManager(private val sharedPreferences: SharedPreferences) {

    fun loadToken() = TokenContainer.update(sharedPreferences.getString("token", null))

    fun saveToken(token: String) {
        sharedPreferences.edit().apply {
            putString("token", token)
        }.apply()
    }

    fun saveCustomerInfo(user: User) {
        sharedPreferences.edit().apply {
            putString("id", user.id)
            putString("username", user.username)
            putString("firstname", user.firstname)
            putString("lastname", user.lastname)
            putString("phone", user.phone)
        }.apply()
    }

    fun saveCustomerInfo(id: String, username: String) {
        sharedPreferences.edit().apply {
            putString("id", id)
            putString("username", username)

        }.apply()
    }

    fun getUserInfo(): User {
        return User(
            sharedPreferences.getString("id", "") ?: "",
            sharedPreferences.getString("firstname", "") ?: "",
            sharedPreferences.getString("lastname", "") ?: "",
            sharedPreferences.getString("username", "") ?: "",
            "",
            sharedPreferences.getString("phone", "") ?: ""
        )
    }

    fun logout() = sharedPreferences.edit().apply {
        clear()
    }.apply()

}