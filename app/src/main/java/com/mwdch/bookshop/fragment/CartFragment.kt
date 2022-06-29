package com.mwdch.bookshop.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.R
import com.mwdch.bookshop.UserManager
import com.mwdch.bookshop.activity.AddressActivity
import com.mwdch.bookshop.activity.BookActivity
import com.mwdch.bookshop.adapter.CartAdapter
import com.mwdch.bookshop.databinding.FragmentCartBinding
import com.mwdch.bookshop.model.Book
import com.mwdch.bookshop.model.Cart
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class CartFragment : Fragment(), CartAdapter.OnBookListener,
    CartBottomSheetDialog.BottomSheetListener {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val compositeDisposable = CompositeDisposable()
    private val apiService: ApiService by inject()
    private val userManager: UserManager by inject()

    private var cartAdapter: CartAdapter? = null

    private var totalPrice = 0
    private var bookCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartAdapter = CartAdapter(requireContext(), this)
        binding.rvBooks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvBooks.adapter = cartAdapter

        binding.btnGoNext.setOnClickListener {
            val dialog =
                CartBottomSheetDialog.newInstance(totalPrice.toString(), bookCount.toString())
            dialog.setListener(this)
            dialog.show(parentFragmentManager, null)
        }

    }

    private fun getCartBooks() {
        totalPrice = 0
        bookCount = 0
        apiService.getCartBooks(userManager.getUserInfo().id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Cart>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Cart>) {
                    binding.progressBar.visibility = View.GONE
                    binding.parent.visibility = View.VISIBLE

                    if (t.isNotEmpty()) {
                        cartAdapter?.setBookList(t)
                        t.forEach { cart ->
                            totalPrice += cart.book.price * cart.quantity
                            bookCount += cart.quantity
                        }
                    } else {
                        binding.rvBooks.visibility = View.GONE
                        binding.btnGoNext.visibility = View.GONE
                        binding.tvEmptyState.visibility = View.VISIBLE
                    }
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.noConnection),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    override fun onResume() {
        super.onResume()

        getCartBooks()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        compositeDisposable.clear()
    }

    override fun onCartClick(book: Book) {
        startActivity(Intent(requireContext(), BookActivity::class.java).apply {
            putExtra("data", book)
        })
    }

    override fun onBtnPurchaseClicked() {
        requireActivity().startActivity(Intent(requireContext(), AddressActivity::class.java))
    }
}