package com.mwdch.bookshop.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.TokenContainer
import com.mwdch.bookshop.model.TokenResponse
import com.mwdch.bookshop.UserManager
import com.mwdch.bookshop.databinding.ActivityLoginBinding
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val compositeDisposable = CompositeDisposable()
    private val apiService: ApiService by inject()
    private val userManager: UserManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (TokenContainer.token != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val username: String = binding.etUsername.text.toString().trim()
            val password: String = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "لطفا همه فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
            } else {
                apiService.login(JsonObject().apply {
                    addProperty("username", username)
                    addProperty("password", password)
                })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<TokenResponse> {
                        override fun onSubscribe(d: Disposable) {
                            compositeDisposable.add(d)
                        }

                        override fun onSuccess(tokenResponse: TokenResponse) {
                            Toast.makeText(
                                this@LoginActivity,
                                "با موفقیت وارد شدید.",
                                Toast.LENGTH_SHORT
                            ).show()
                            TokenContainer.update(tokenResponse.token)
                            userManager.saveToken(tokenResponse.token)
                            userManager.saveCustomerInfo(tokenResponse.id, tokenResponse.username)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }

                        override fun onError(e: Throwable) {
                            Toast.makeText(
                                this@LoginActivity,
                                "نام کاربری یا رمزعبور اشتباه است.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}