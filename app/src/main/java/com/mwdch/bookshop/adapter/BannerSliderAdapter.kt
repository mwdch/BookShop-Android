package com.mwdch.bookshop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.mwdch.bookshop.databinding.ItemSliderBinding
import com.mwdch.bookshop.model.Banner

class BannerSliderAdapter : PagerAdapter() {

    private var banners: List<Banner> = ArrayList()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding =
            ItemSliderBinding.inflate(LayoutInflater.from(container.context), container, false)
        Glide.with(container.context).load(banners[position].image).into(binding.ivSlider)
        container.addView(binding.root)

        binding.root.rotationY = 180f

        return binding.root
    }

    fun setBannerList(banners: List<Banner>) {
        this.banners = banners
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return banners.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}