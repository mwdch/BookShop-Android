package com.mwdch.bookshop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.mcdev.quantitizerlibrary.AnimationStyle
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.R
import com.mwdch.bookshop.UserManager
import com.mwdch.bookshop.adapter.BookHomeAdapter
import com.mwdch.bookshop.adapter.LimitedCommentAdapter
import com.mwdch.bookshop.databinding.ActivityBookBinding
import com.mwdch.bookshop.model.AddToCartResponse
import com.mwdch.bookshop.model.Book
import com.mwdch.bookshop.model.Comment
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import java.text.DecimalFormat

class BookActivity : AppCompatActivity(), BookHomeAdapter.OnBookListener {

    private lateinit var binding: ActivityBookBinding

    private val apiService: ApiService by inject()
    private val userManager: UserManager by inject()
    private val compositeDisposable = CompositeDisposable()

    private var relatedBookAdapter: BookHomeAdapter? = null

    private lateinit var book: Book

    var limitedCommentAdapter: LimitedCommentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        book = intent.getParcelableExtra<Book>("data") as Book

        setFields()

        binding.quantitizer.textAnimationStyle = AnimationStyle.FALL_IN
        binding.quantitizer.maxValue = book.volumes
        binding.quantitizer.isReadOnly = true

        relatedBookAdapter = BookHomeAdapter(this, this)
        binding.rvRelatedBooks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRelatedBooks.adapter = relatedBookAdapter

        apiService.getBooksByCategory(book.category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Book>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Book>) {
                    relatedBookAdapter?.setBookList(t)
                    binding.progressBar.visibility = View.GONE
                    binding.parent.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(
                        this@BookActivity,
                        getString(R.string.noConnection),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

        limitedCommentAdapter = LimitedCommentAdapter()
        binding.rvComments.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvComments.adapter = limitedCommentAdapter

        apiService.getComments(book.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Comment>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Comment>) {
                    limitedCommentAdapter?.setCommentList(t.toMutableList())
                }

                override fun onError(e: Throwable) {

                }

            })

        binding.tvShowAllComments.setOnClickListener {
            startActivity(Intent(this@BookActivity, CommentActivity::class.java).apply {
                putExtra("data", book)
            })
        }

        binding.btnAddToCart.setOnClickListener {
            apiService.addToCart(JsonObject().apply {
                addProperty("customer_id", userManager.getUserInfo().id)
                addProperty("book_id", book.id)
                addProperty("quantity", binding.quantitizer.value)
            })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<AddToCartResponse> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onSuccess(t: AddToCartResponse) {
                        Toast.makeText(
                            this@BookActivity,
                            t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(e: Throwable) {

                    }
                })
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun setFields() {
        Glide.with(this).load(book.image).into(binding.ivBook)
        binding.tvBookName.text = book.name
        binding.tvAuthorName.text = book.author
        binding.tvDescription.text = book.description
        binding.tvSecondBookName.text = book.name
        binding.tvSecondBookAuthor.text = book.author
        binding.tvBookCategory.text = when (book.category) {
            0 -> "فلسفه"
            1 -> "دین و مذهب"
            2 -> "علمی"
            3 -> "اقتصاد"
            4 -> "روانشناسی"
            5 -> "هنر"
            6 -> "رمان و داستان"
            7 -> "درسی و دانشگاهی"
            8 -> "فیزیک"
            9 -> "تاریخ"
            10 -> "شیمی"
            11 -> "زبان"
            12 -> "ریاضیات"
            13 -> "لوازم التحریر"
            else -> ""
        }
        binding.tvBookPageCount.text = book.pages.toString()
        val decimalFormat = DecimalFormat("###,###")
        binding.tvPrice.text = decimalFormat.format(book.price)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onBookClick(book: Book) {
        startActivity(Intent(this, BookActivity::class.java).apply {
            putExtra("data", book)
        })
    }
}