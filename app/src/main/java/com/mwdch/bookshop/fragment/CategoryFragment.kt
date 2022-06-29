package com.mwdch.bookshop.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.R
import com.mwdch.bookshop.activity.DetailCategoryActivity
import com.mwdch.bookshop.adapter.AllCategoryAdapter
import com.mwdch.bookshop.databinding.FragmentCategoryBinding
import com.mwdch.bookshop.model.Category
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class CategoryFragment : Fragment(), AllCategoryAdapter.OnAllCategoryListener {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val compositeDisposable = CompositeDisposable()
    private val apiService: ApiService by inject()

    private var allCategoryAdapter: AllCategoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allCategoryAdapter = AllCategoryAdapter(requireContext(), this)
        binding.rvCategory.setHasFixedSize(true)
        binding.rvCategory.layoutManager = GridLayoutManager(context, 2)
        binding.rvCategory.adapter = allCategoryAdapter

        apiService.getAllCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Category>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Category>) {
                    allCategoryAdapter?.setCategoryList(t)
                    binding.progressBar.visibility = View.GONE
                    binding.parent.visibility = View.VISIBLE
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

    override fun onStop() {
        super.onStop()
        _binding = null
        compositeDisposable.clear()
    }

    override fun onAllCategoryClick(category: Category) {
        startActivity(Intent(context, DetailCategoryActivity::class.java).apply {
            putExtra("data", category)
        })
    }
}