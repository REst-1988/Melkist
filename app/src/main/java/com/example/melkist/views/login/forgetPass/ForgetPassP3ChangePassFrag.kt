package com.example.melkist.views.login.forgetPass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragForgetPassP3ChangePassBinding
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.ForgetPassViewModel


class ForgetPassP3ChangePassFrag : Fragment() {

    private lateinit var binding: FragForgetPassP3ChangePassBinding
    private val viewModel: ForgetPassViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragForgetPassP3ChangePassBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ForgetPassP3ChangePassFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToChangePassResponse()
    }

    private fun listenToChangePassResponse() {
        viewModel.changePassResponse.observe(viewLifecycleOwner) {
            if (viewModel.isResponseOk(viewModel.changePassResponse)) {
                showToast(requireContext(), viewModel.changePassResponse.value!!.message!!)
                showDialogWithMessage(
                    requireContext(),
                    "p3 listenToChangePassResponse: " + viewModel.changePassResponse.value!!.message!!
                ) { d, _ ->
                    d.dismiss()
                    readyForStartNextSetion()
                }
            } else if (viewModel.isResponseNotOk(viewModel.changePassResponse))
                if (viewModel.changePassResponse.value!!.errors.isNotEmpty())
                    showDialogWithMessage(
                        requireContext(),
                        "p3 listenToChangePassResponse: " + viewModel.changePassResponse.value!!.errors[0]
                    ) { d, _ -> d.dismiss() }
                else
                    showToast(
                        requireContext(),
                        "p3 listenToChangePassResponse: " + viewModel.changePassResponse.value!!.result.toString()
                    )
        }
    }

    private fun readyForStartNextSetion() {
        findNavController()
            .navigate(
                R.id.action_forgetPassP3ChangePassFrag_to_forgetPassP1EnterNcodePhoneFrag
            )
    }

    fun back() {
        findNavController().navigate(
            R.id.action_forgetPassP3ChangePassFrag_to_forgetPassP1EnterNcodePhoneFrag
        )
    }

    fun onConfirm() {
        if (isPassword()) {
            viewModel.password = binding.etPassword.editText!!.text.toString()
            viewModel.requestChangePasswordByMobile()
        }
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