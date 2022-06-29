package com.mwdch.bookshop.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.Response
import com.mwdch.bookshop.databinding.ActivitySignUpBinding
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val compositeDisposable = CompositeDisposable()
    private val apiService: ApiService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {
            onBackPressed()
        }

        binding.btnSignUp.setOnClickListener {
            val username: String = binding.etUsername.text.toString().trim()
            val name: String = binding.etFirstname.text.toString().trim()
            val lastname: String = binding.etLastname.text.toString().trim()
            val phone: String = binding.etPhone.text.toString().trim()
            val password: String = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || name.isEmpty() || lastname.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "لطفا همه فیلدها را پر کنید", Toast.LENGTH_SHORT).show()
            } else {
                apiService.signUp(JsonObject().apply {
                    addProperty("username", username)
                    addProperty("firstname", name)
                    addProperty("lastname", lastname)
                    addProperty("phone", phone)
                    addProperty("password", password)
                })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<Response> {
                        override fun onSubscribe(d: Disposable) {
                            compositeDisposable.add(d)
                        }

                        override fun onSuccess(t: Response) {
                            Toast.makeText(
                                this@SignUpActivity,
                                "ثبت نام با موفقیت انجام شد.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }

                        override fun onError(e: Throwable) {
                            Toast.makeText(
                                this@SignUpActivity,
                                "ثبت نام انجام نشد",
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