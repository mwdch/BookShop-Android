package com.mwdch.bookshop.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.R
import com.mwdch.bookshop.UserManager
import com.mwdch.bookshop.adapter.OrderAdapter
import com.mwdch.bookshop.databinding.ActivityOrderBinding
import com.mwdch.bookshop.model.Order
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class OrderActivity : AppCompatActivity() {

    private val apiService: ApiService by inject()
    private val userManager: UserManager by inject()
    private val compositeDisposable = CompositeDisposable()

    private var orderAdapter: OrderAdapter? = null

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        orderAdapter = OrderAdapter(this)
        binding.rvOrders.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvOrders.adapter = orderAdapter

        apiService.getOrders(userManager.getUserInfo().id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Order>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Order>) {
                    binding.progressBar.visibility = View.GONE
                    binding.parent.visibility = View.VISIBLE
                    if (t.isNotEmpty()) {
                        orderAdapter?.setBookList(t)
                    } else {
                        binding.rvOrders.visibility = View.GONE
                        binding.tvEmptyState.visibility = View.VISIBLE
                    }
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(
                        this@OrderActivity,
                        getString(R.string.noConnection),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

}