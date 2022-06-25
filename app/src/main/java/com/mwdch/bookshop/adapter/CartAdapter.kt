package com.mwdch.bookshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mwdch.bookshop.databinding.ItemCartBinding
import com.mwdch.bookshop.model.Book
import com.mwdch.bookshop.model.Cart
import java.text.DecimalFormat

class CartAdapter(val context: Context, private val listener: OnBookListener) :
    RecyclerView.Adapter<CartAdapter.Holder>() {

    var carts: List<Cart> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindCart(carts[position])
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    fun setBookList(carts: List<Cart>) {
        this.carts = carts
        notifyDataSetChanged()
    }

    inner class Holder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindCart(cart: Cart) {
            binding.tvName.text = cart.book.name
            binding.tvAuthor.text = cart.book.author
            val decimalFormat = DecimalFormat("###,###")
            binding.tvPrice.text = decimalFormat.format(cart.book.price)

            Glide.with(context).load(cart.book.image).into(binding.ivBookImage)

            binding.tvQuantity.text = "تعداد: " + cart.quantity.toString()

            itemView.setOnClickListener {
                listener.onCartClick(cart.book)
            }
        }
    }

    interface OnBookListener {
        fun onCartClick(book: Book)
    }
}