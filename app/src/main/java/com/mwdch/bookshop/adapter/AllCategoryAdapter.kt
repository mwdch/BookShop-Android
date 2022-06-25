package com.mwdch.bookshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mwdch.bookshop.databinding.ItemAllCategoryBinding
import com.mwdch.bookshop.model.Category

class AllCategoryAdapter(
    var context: Context,
    private val listener: OnAllCategoryListener
) : RecyclerView.Adapter<AllCategoryAdapter.Holder>() {

    private var categories: List<Category> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemAllCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindCategory(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    fun setCategoryList(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }


    inner class Holder(private val binding: ItemAllCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindCategory(category: Category) {
            binding.tvName.text = category.name
            Glide.with(context).load(category.icon).into(binding.ivImage)

            itemView.setOnClickListener {
                listener.onAllCategoryClick(category)
            }
        }
    }

    interface OnAllCategoryListener {
        fun onAllCategoryClick(category: Category)
    }
}