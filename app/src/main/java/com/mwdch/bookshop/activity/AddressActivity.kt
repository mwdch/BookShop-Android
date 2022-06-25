package com.mwdch.bookshop.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.UserManager
import com.mwdch.bookshop.databinding.ActivityAddressBinding
import com.mwdch.bookshop.model.Book
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class AddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressBinding

    private val userManager: UserManager by inject()
    private val apiService: ApiService by inject()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = userManager.getUserInfo()
        binding.etFirstname.setText(user.firstname)
        binding.etLastname.setText(user.lastname)
        binding.etPhone.setText(user.phone)

        binding.btnGoToPay.setOnClickListener {
            apiService.purchase(user.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        startActivity(Intent(this@AddressActivity, SuccessPayActivity::class.java))
                        finish()
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(
                            this@AddressActivity,
                            e.toString(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                })
        }
    }
}