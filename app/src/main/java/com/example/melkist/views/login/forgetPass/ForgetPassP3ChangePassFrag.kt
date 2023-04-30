package com.example.melkist.views.login.forgetPass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.melkist.R
import com.example.melkist.databinding.FragForgetPassP3ChangePassBinding
import com.example.melkist.viewmodels.SignupViewModel


class ForgetPassP3ChangePassFrag : Fragment() {

    private lateinit var binding: FragForgetPassP3ChangePassBinding
    private val viewModel: SignupViewModel by activityViewModels()
    private var isShowPass = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragForgetPassP3ChangePassBinding.inflate(inflater)
        return binding.root
    }

    fun cancel() {
        TODO("CMPL")
    }

    fun onConfirm() {
        if (isPassword())
            TODO("CMPL")
    }

    private fun isPassword(): Boolean {
        if (binding.etPassword.editText == null || binding.etPassword.editText!!.text.isEmpty()) {
            binding.etPassword.error =
                requireContext().resources.getString(R.string.error_on_empty_password)
            return false
        } else if (binding.etPassword.editText!!.text.length < 8) {
            binding.etPassword.error =
                requireContext().resources.getString(R.string.error_on_wrong_password)
            return false
        }
        binding.etPassword.error = null
        return true
    }


}