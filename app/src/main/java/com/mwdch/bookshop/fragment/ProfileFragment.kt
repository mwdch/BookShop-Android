package com.mwdch.bookshop.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mwdch.bookshop.ApiService
import com.mwdch.bookshop.R
import com.mwdch.bookshop.TokenContainer
import com.mwdch.bookshop.UserManager
import com.mwdch.bookshop.activity.EditProfileActivity
import com.mwdch.bookshop.activity.LoginActivity
import com.mwdch.bookshop.activity.OrderActivity
import com.mwdch.bookshop.activity.QuestionActivity
import com.mwdch.bookshop.databinding.FragmentProfileBinding
import com.mwdch.bookshop.model.User
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val compositeDisposable = CompositeDisposable()
    private val apiService: ApiService by inject()
    private val userManager: UserManager by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService.getCustomer(userManager.getUserInfo().username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<User> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: User) {
                    binding.tvName.text = "${t.firstname} ${t.lastname}"
                    binding.tvPhone.text = t.phone
                    userManager.saveCustomerInfo(t)
                    binding.progressBar.visibility = View.GONE
                    binding.parent.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.noConnection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        binding.layoutLogout.setOnClickListener {
            userManager.logout()
            TokenContainer.update(null)
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        binding.layoutQuestion.setOnClickListener {
            startActivity(Intent(requireContext(), QuestionActivity::class.java))
        }

        binding.layoutOrders.setOnClickListener {
            startActivity(
                Intent(requireContext(), OrderActivity::class.java)
            )
        }

        binding.layoutInfo.setOnClickListener {
            startActivity(
                Intent(requireContext(), EditProfileActivity::class.java)
            )
        }

    }

    override fun onStop() {
        super.onStop()
        _binding = null
        compositeDisposable.clear()
    }
}