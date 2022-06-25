package com.mwdch.bookshop.adapter

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mwdch.bookshop.R
import com.mwdch.bookshop.databinding.ItemQuestionBinding
import com.mwdch.bookshop.model.Question

class QuestionAdapter : RecyclerView.Adapter<QuestionAdapter.Holder>() {

    private var questions: List<Question> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindQuestion(questions[position])
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    fun setQuestionList(questions: List<Question>) {
        this.questions = questions
        notifyDataSetChanged()
    }

    inner class Holder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindQuestion(question: Question) {
            binding.tvTitle.text = question.title
            binding.tvDescription.text = question.description

            binding.ivImage.setOnClickListener {
                if (binding.parentDescription.visibility == View.GONE) {
                    binding.parentDescription.visibility = View.VISIBLE
                    binding.ivImage.setImageResource(R.drawable.ic_baseline_close_24)
                    TransitionManager.beginDelayedTransition(
                        binding.parentTitle,
                        AutoTransition()
                    )
                } else {
                    binding.parentDescription.visibility = View.GONE
                    binding.ivImage.setImageResource(R.drawable.ic_baseline_add_24)
                    TransitionManager.beginDelayedTransition(
                        binding.parentTitle,
                        AutoTransition()
                    )
                }
            }

        }
    }
}