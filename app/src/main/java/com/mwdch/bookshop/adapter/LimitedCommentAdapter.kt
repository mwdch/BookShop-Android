package com.mwdch.bookshop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mwdch.bookshop.databinding.ItemLimitedCommentBinding
import com.mwdch.bookshop.model.Category
import com.mwdch.bookshop.model.Comment

class LimitedCommentAdapter :
    RecyclerView.Adapter<LimitedCommentAdapter.Holder?>() {

    var comments = mutableListOf<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemLimitedCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.comment(comments[position])
    }

    override fun getItemCount(): Int {
        return if (comments.size > 4) 4 else comments.size
    }

    fun setCommentList(comments: MutableList<Comment>) {
        this.comments = comments
        notifyDataSetChanged()
    }

    fun addComment(comment: Comment) {
        this.comments.add(0, comment)
        notifyItemInserted(0)
    }

    inner class Holder(private val binding: ItemLimitedCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun comment(comment: Comment) {
            binding.tvText.text = comment.text
        }
    }
}