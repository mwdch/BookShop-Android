package com.mwdch.bookshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mwdch.bookshop.databinding.ItemBookAllBinding
import com.mwdch.bookshop.model.Book
import java.text.DecimalFormat

class AllBookAdapter(val context: Context, private val listener: OnBookListener) :
    RecyclerView.Adapter<AllBookAdapter.Holder>() {

    var books: List<Book> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemBookAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class Holder(private val binding: ItemBookAllBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindBook(book: Book) {
            binding.tvName.text = book.name
            binding.tvAuthor.text = book.author
            val decimalFormat = DecimalFormat("###,###")
            binding.tvPrice.text = decimalFormat.format(book.price)

            Glide.with(context).load(book.image).into(binding.ivBookImage)

            itemView.setOnClickListener {
                listener.onBookClick(book)
            }
        }
    }

    interface OnBookListener {
        fun onBookClick(book: Book)
    }
}