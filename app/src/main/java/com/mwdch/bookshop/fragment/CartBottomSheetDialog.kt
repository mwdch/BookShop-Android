package com.mwdch.bookshop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mwdch.bookshop.databinding.LayoutBottomSheetCartBinding
import java.text.DecimalFormat

class CartBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: LayoutBottomSheetCartBinding? = null
    private val binding get() = _binding!!

    private var bottomSheetListener: BottomSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = LayoutBottomSheetCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val decimalFormat = DecimalFormat("###,###")
        binding.tvTotalPrice.text = decimalFormat.format(requireArguments().getString("totalPrice")?.toInt())
        binding.tvBookCount.text = requireArguments().getString("bookCount")

        binding.btnGoToNext.setOnClickListener {
            bottomSheetListener?.onBtnPurchaseClicked()
            dismiss()
        }
    }

    companion object {
        fun newInstance(totalPrice: String, bookCount: String): CartBottomSheetDialog {
            val args = Bundle()
            args.putString("totalPrice", totalPrice)
            args.putString("bookCount", bookCount)
            val fragment = CartBottomSheetDialog()
            fragment.arguments = args
            return fragment
        }
    }

    fun setListener(listener: BottomSheetListener) {
        bottomSheetListener = listener
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

    interface BottomSheetListener {
        fun onBtnPurchaseClicked()
    }

}