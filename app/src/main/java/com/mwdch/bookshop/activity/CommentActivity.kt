package com.mwdch.bookshop.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.UserManager
import com.mwdch.bookshop.adapter.LimitedCommentAdapter
import com.mwdch.bookshop.databinding.ActivityCommentBinding
import com.mwdch.bookshop.model.Book
import com.mwdch.bookshop.model.Comment
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject


class CommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding

    private val apiService: ApiService by inject()
    private val userManager: UserManager by inject()
    private val compositeDisposable = CompositeDisposable()

    private var limitedCommentAdapter: LimitedCommentAdapter? = null

    private lateinit var book: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        book = intent.getParcelableExtra<Book>("data") as Book

        binding.ivBack.setOnClickListener {
            finish()
        }

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
                    binding.progressBar.visibility = View.GONE
                    binding.parent.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@CommentActivity, e.toString(), Toast.LENGTH_SHORT).show()
                }

            })

        binding.btnSendComment.setOnClickListener {
            val commentText = binding.etComment.text.toString().trim()
            if (commentText.isEmpty()) {
                Toast.makeText(this, "لطفا نظر خود را وارد نمایید.", Toast.LENGTH_SHORT).show()
            } else {
                apiService.sendComment(JsonObject().apply {
                    addProperty("customer", userManager.getUserInfo().id)
                    addProperty("book", book.id)
                    addProperty("text", commentText)
                })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<Comment> {
                        override fun onSubscribe(d: Disposable) {
                            compositeDisposable.add(d)
                        }

                        override fun onSuccess(t: Comment) {
                            limitedCommentAdapter?.addComment(t)
                        }

                        override fun onError(e: Throwable) {
                            Toast.makeText(this@CommentActivity, e.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
            }
        }
    }
}