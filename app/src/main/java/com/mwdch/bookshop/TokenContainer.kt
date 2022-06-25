package com.mwdch.bookshop

import timber.log.Timber

object TokenContainer {
    var token: String? = null
        private set

    fun update(token: String?) {
        Timber.i("Access Token-> ${token?.substring(0, 10)}")
        TokenContainer.token = token
    }
}