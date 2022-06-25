package com.mwdch.bookshop.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mwdch.bookshop.R
import com.mwdch.bookshop.databinding.ActivityHomeBinding
import com.mwdch.bookshop.fragment.CartFragment
import com.mwdch.bookshop.fragment.CategoryFragment
import com.mwdch.bookshop.fragment.HomeFragment
import com.mwdch.bookshop.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.containerLayout, HomeFragment())
            .commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containerLayout, HomeFragment())
                        .commit()
                    true
                }
                R.id.nav_categories -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containerLayout, CategoryFragment())
                        .commit()
                    true
                }
                R.id.nav_cart -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containerLayout, CartFragment())
                        .commit()
                    true
                }
                R.id.nav_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containerLayout, ProfileFragment())
                        .commit()
                    true
                }
                else -> false
            }

        }

    }

}