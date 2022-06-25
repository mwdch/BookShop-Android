package com.mwdch.bookshop.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.adapter.AllBookAdapter
import com.mwdch.bookshop.databinding.ActivitySearchBinding
import com.mwdch.bookshop.model.Book
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class SearchActivity : AppCompatActivity(), AllBookAdapter.OnBookListener {

    private lateinit var binding: ActivitySearchBinding

    private val apiService: ApiService by inject()
    private val compositeDisposable = CompositeDisposable()

    private var allBookAdapter: AllBookAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        allBookAdapter = AllBookAdapter(this, this)
        binding.rvBooks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvBooks.adapter = allBookAdapter

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(p0)) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.parent.visibility = View.INVISIBLE
                    apiService.search(p0.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : SingleObserver<List<Book>> {
                            override fun onSubscribe(d: Disposable) {
                                compositeDisposable.add(d)
                            }

                            override fun onSuccess(t: List<Book>) {
                                allBookAdapter?.setBookList(t)
                                binding.progressBar.visibility = View.GONE
                                binding.parent.visibility = View.VISIBLE
                            }

                            override fun onError(e: Throwable) {
                                Toast.makeText(
                                    this@SearchActivity,
                                    e.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }

    override fun onBookClick(book: Book) {
        startActivity(Intent(this, BookActivity::class.java).apply {
            putExtra("data", book)
        })
    }
}