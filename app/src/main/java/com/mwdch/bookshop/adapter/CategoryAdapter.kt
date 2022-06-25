package com.mwdch.bookshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mwdch.bookshop.databinding.ItemCategoryBinding
import com.mwdch.bookshop.model.Category

class CategoryAdapter(val context: Context, private val listener: OnCategoryListener) :
    RecyclerView.Adapter<CategoryAdapter.Holder?>() {

    var categories: List<Category> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindCategory(categories[position])
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun setCategoryList(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    inner class Holder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindCategory(category: Category) {
            binding.tvName.text = category.name
            Glide.with(context).load(category.icon).centerCrop().circleCrop()
                .into(binding.ivCategory)

            itemView.setOnClickListener {
                listener.onCategoryClick(category)
            }
        }
    }

    interface OnCategoryListener {
        fun onCategoryClick(category: Category)
    }
}