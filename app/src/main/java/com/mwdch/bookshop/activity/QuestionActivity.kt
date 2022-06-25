package com.mwdch.bookshop.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mwdch.bookshop.adapter.QuestionAdapter
import com.mwdch.bookshop.databinding.ActivityQuestionBinding
import com.mwdch.bookshop.model.Question

class QuestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionBinding
    var questionAdapter: QuestionAdapter = QuestionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        val questions = mutableListOf<Question>()

        questions.add(
            Question(
                "چگونه سفارش خود را در فروشگاه ثبت کنم؟",
                "بعد از ورود به پروفایل ، کالای مدنظر خود را به سبد خرید اضافه کنید ، پس از انتخاب آدرس میتوانید نوع پرداخت خود را انتخاب کنید.بعد از اتمام ثبت سفارش ، سیستم کد سفارش شما را نمایش خواهد داد."
            )
        )
        questions.add(
            Question(
                "چگونه میتوانم سفارشم را پیگیری کنم؟",
                "وارد نرم افزار شوید\r\nروی گزینه سفارش های من کلیک کنید\r\nدر این قسمت با کلیک روی جزییات میتوانید سفارش خود را ببینید\r\nمیتوانید در این قسمت روند آماده سازی و مراحل ارسال سفارش خود را پیگیری کنید"
            )
        )
        questions.add(
            Question(
                "آیا میتوانم مبلغ سفارش را بصورت اقساطی پرداخت کنم؟",
                "در حال حاضر امکان تسویه حساب بصورت اقساط فراهم نشده"
            )
        )
        questions.add(
            Question(
                "چطور میتوانم سفارشم را لغو کنم؟",
                "شما میتوانید با مراجعه به پروفایل خود سفارش یا مرسوله ایی که از ارسال آن منصرف شدید را لغو نمایید.\r\nمیتوانید برای مشاهده روند لغو سفارش به توضیحات تکمیلی مراجعه کنید."
            )
        )
        questions.add(
            Question(
                "چطور میتوانم سفارشم را پیگیری کنم؟",
                "روی گزینه سفارش های من کلیک کنید.\r\nدر این قسمت با کلیک روی جزییات میتوانید سفارش خود را ببینید.\r\nمیتوانید در این قسمت روند آماده سازی و مراحل ارسال سفارش خود را پیگیری کنید."
            )
        )

        binding.rvQuestions.setHasFixedSize(true)
        binding.rvQuestions.layoutManager = LinearLayoutManager(this)
        questionAdapter.setQuestionList(questions)
        binding.rvQuestions.adapter = questionAdapter
    }
}