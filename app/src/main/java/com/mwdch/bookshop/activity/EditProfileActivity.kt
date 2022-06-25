package com.mwdch.bookshop.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.UserManager
import com.mwdch.bookshop.databinding.ActivityProfileEditBinding
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditBinding

    private val userManager: UserManager by inject()
    private val apiService: ApiService by inject()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = userManager.getUserInfo()
        binding.etFirstname.setText(user.firstname)
        binding.etLastname.setText(user.lastname)
        binding.etPhone.setText(user.phone)

        binding.btnSaveInfo.setOnClickListener {
            val firstname: String = binding.etFirstname.text.toString().trim()
            val lastname: String = binding.etLastname.text.toString().trim()
            val phone: String = binding.etPhone.text.toString().trim()

            if (firstname.isEmpty() || lastname.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "لطفا همه فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
            } else {
                apiService.updateUserInfo(userManager.getUserInfo().id, JsonObject().apply {
                    addProperty("firstname", firstname)
                    addProperty("lastname", lastname)
                    addProperty("phone", phone)
                })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CompletableObserver {
                        override fun onSubscribe(d: Disposable) {
                            compositeDisposable.add(d)
                        }

                        override fun onComplete() {
                            val user = userManager.getUserInfo()
                            user.firstname = firstname
                            user.lastname = lastname
                            user.phone = phone
                            userManager.saveCustomerInfo(user)
                            Toast.makeText(
                                this@EditProfileActivity,
                                "با موفقیت تغییر یافت.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }

                        override fun onError(e: Throwable) {

                        }
                    })
            }
        }

        binding.btnSavePassword.setOnClickListener {
            val oldPassword: String = binding.etOldPassword.text.toString().trim()
            val newPassword: String = binding.etNewPassword.text.toString().trim()

            if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "لطفا همه فیلدها را پر کنید.", Toast.LENGTH_SHORT).show()
            } else {
                apiService.updateUserPassword(JsonObject().apply {
                    addProperty("old_password", oldPassword)
                    addProperty("new_password", newPassword)
                    addProperty("username", userManager.getUserInfo().username)
                })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CompletableObserver {
                        override fun onSubscribe(d: Disposable) {
                            compositeDisposable.add(d)
                        }

                        override fun onComplete() {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "با موفقیت تغییر یافت.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }

                        override fun onError(e: Throwable) {

                        }
                    })
            }
        }
    }
}