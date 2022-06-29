package com.mwdch.bookshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mwdch.bookshop.databinding.ItemOrderBookBinding
import com.mwdch.bookshop.model.Book

class OrderBookAdapter(val context: Context) :
    RecyclerView.Adapter<OrderBookAdapter.Holder>() {

    var books: List<Book> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemOrderBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindBook(books[position])
    }

    override fun getItemCount(): Int {
        return books.size
    }

    fun setBookList(books: List<Book>) {
        this.books = books
        notifyDataSetChanged()
    }

    inner class Holder(private val binding: ItemOrderBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindBook(book: Book) {
            Glide.with(context).load(book.image).into(binding.ivBook)
        }
    }

}