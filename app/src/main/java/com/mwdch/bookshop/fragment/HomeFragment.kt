package com.mwdch.bookshop.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.activity.BookActivity
import com.mwdch.bookshop.activity.DetailCategoryActivity
import com.mwdch.bookshop.activity.SearchActivity
import com.mwdch.bookshop.adapter.BannerSliderAdapter
import com.mwdch.bookshop.adapter.BookHomeAdapter
import com.mwdch.bookshop.adapter.CategoryAdapter
import com.mwdch.bookshop.databinding.FragmentHomeBinding
import com.mwdch.bookshop.model.Banner
import com.mwdch.bookshop.model.Book
import com.mwdch.bookshop.model.Category
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class HomeFragment : Fragment(), CategoryAdapter.OnCategoryListener,
    BookHomeAdapter.OnBookListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val compositeDisposable = CompositeDisposable()
    private val apiService: ApiService by inject()

    private var sliderAdapter: BannerSliderAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var bookAdapter: BookHomeAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sliderAdapter = BannerSliderAdapter()
        binding.viewPager.adapter = sliderAdapter
        binding.viewPager.rotationY = 180f
        binding.tabs.setupWithViewPager(binding.viewPager, true)

        setAnimationForSlider()

        apiService.getAllBanners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Banner>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Banner>) {
                    sliderAdapter?.setBannerList(t)
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }

            })

        categoryAdapter = CategoryAdapter(requireContext(), this)
        binding.rvCategory.setHasFixedSize(true)
        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.adapter = categoryAdapter

        apiService.getAllCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Category>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Category>) {
                    categoryAdapter?.setCategoryList(t)
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }

            })

        bookAdapter = BookHomeAdapter(requireContext(), this)
        binding.rvBooks.setHasFixedSize(true)
        binding.rvBooks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvBooks.adapter = bookAdapter

        apiService.getAllBooks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Book>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Book>) {
                    bookAdapter?.setBookList(t)
                    binding.progressBar.visibility = View.GONE
                    binding.parent.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }

            })

        binding.ivSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

    }

    private fun setAnimationForSlider() {
        val paddingPx = 120
        val MIN_SCALE = 0.8f
        val MAX_SCALE = 1f
        binding.viewPager.clipToPadding = false
        binding.viewPager.setPadding(paddingPx, 0, paddingPx, 0)
        val transformer = ViewPager.PageTransformer { page: View, position: Float ->
            val pagerWidthPx = (page.parent as ViewPager).width.toFloat()
            val pageWidthPx = pagerWidthPx - 2 * paddingPx
            val maxVisiblePages = pagerWidthPx / pageWidthPx
            val center = maxVisiblePages / 2f
            val scale: Float
            if (position + 0.5f < center - 0.5f || position > center) {
                scale = MIN_SCALE
            } else {
                val coef: Float = if (position + 0.5f < center) {
                    (position + 1 - center) / 0.5f
                } else {
                    (center - position) / 0.5f
                }
                scale = coef * (MAX_SCALE - MIN_SCALE) + MIN_SCALE
            }
            page.scaleX = scale
            page.scaleY = scale
        }
        binding.viewPager.setPageTransformer(false, transformer)
//        setThreadForSlider()
    }

    private fun setThreadForSlider() {
        val sleepDuration = 4000L
        val thread: Thread = object : Thread() {
            override fun run() {
                while (true) {
                    try {
                        sleep(sleepDuration)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    requireActivity().runOnUiThread {
                        if (binding.viewPager.currentItem < sliderAdapter?.count!! - 1) binding.viewPager.currentItem =
                            binding.viewPager.currentItem + 1 else binding.viewPager.currentItem =
                            0

                    }
                }
            }
        }
        thread.start()
    }

    override fun onStop() {
        super.onStop()
        _binding = null
        compositeDisposable.clear()
    }

    override fun onBookClick(book: Book) {
        startActivity(Intent(requireContext(), BookActivity::class.java).apply {
            putExtra("data", book)
        })
    }

    override fun onCategoryClick(category: Category) {
        startActivity(Intent(context, DetailCategoryActivity::class.java).apply {
            putExtra("data", category)
        })
    }
}