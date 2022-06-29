package com.mwdch.bookshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mwdch.bookshop.databinding.ItemOrderBinding
import com.mwdch.bookshop.model.Book
import com.mwdch.bookshop.model.Order

class OrderAdapter(val context: Context) :
    RecyclerView.Adapter<OrderAdapter.Holder>() {

    private var orders: List<Order> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindOrder(orders[position])
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    fun setBookList(orders: List<Order>) {
        this.orders = orders
        notifyDataSetChanged()
    }

    inner class Holder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindOrder(order: Order) {
            binding.tvTrackingCode.text = "کد سفارش: " + order._id

            val orderBookAdapter = OrderBookAdapter(context)
            binding.rvBooks.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvBooks.adapter = orderBookAdapter
            val books = mutableListOf<Book>()
            order.books.forEach {
                books.add(it.book)
            }
            orderBookAdapter.setBookList(books)

        }
    }


}