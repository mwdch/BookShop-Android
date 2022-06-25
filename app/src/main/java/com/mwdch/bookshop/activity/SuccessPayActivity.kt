package com.mwdch.bookshop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mwdch.bookshop.R

class SuccessPayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_pay)
        Handler().postDelayed({
            finish()
        }, 2000)
    }
}