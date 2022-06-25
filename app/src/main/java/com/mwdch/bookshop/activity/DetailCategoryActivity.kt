package com.mwdch.bookshop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.adapter.AllBookAdapter
import com.mwdch.bookshop.databinding.ActivityDetailCategoryBinding
import com.mwdch.bookshop.model.Book
import com.mwdch.bookshop.model.Category
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class DetailCategoryActivity : AppCompatActivity(), AllBookAdapter.OnBookListener {

    private lateinit var binding: ActivityDetailCategoryBinding

    private val apiService: ApiService by inject()
    private val compositeDisposable = CompositeDisposable()

    private var allBookAdapter: AllBookAdapter? = null

    private lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = intent.getParcelableExtra<Category>("data") as Category

        binding.tvCategory.text = category.name

        binding.ivBack.setOnClickListener {
            finish()
        }

        allBookAdapter = AllBookAdapter(this, this)
        binding.rvBooks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvBooks.adapter = allBookAdapter

        apiService.getBooksByCategory(category.id)
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
                    Toast.makeText(this@DetailCategoryActivity, e.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            })

    }

    override fun onBookClick(book: Book) {
        startActivity(Intent(this, BookActivity::class.java).apply {
            putExtra("data", book)
        })
    }
}