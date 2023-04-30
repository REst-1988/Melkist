package com.example.melkist.views.login.forgetPass

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragForgetPassP1EnterNcodePhoneBinding
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.ForgetPassViewModel


class ForgetPassP1EnterNcodePhoneFrag : Fragment() {

    private lateinit var binding: FragForgetPassP1EnterNcodePhoneBinding
    private val viewModel: ForgetPassViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragForgetPassP1EnterNcodePhoneBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            binding.apply {
                lifecycleOwner = viewLifecycleOwner
                viewmodel = viewModel
                fragment = this@ForgetPassP1EnterNcodePhoneFrag
            }
            listenToSendVerificationCode()
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(requireContext(), requireContext().resources.getString(R.string.global_error))
        }
    }

    fun onSend() {
        if (isAllFieldIsOkay())
            registerNcodePhoneAndRequestApi()
    }

    fun back() {
        findNavController().navigate(R.id.action_forgetPassP1EnterNcodePhoneFrag_to_loginForm)
    }

    private fun registerNcodePhoneAndRequestApi() {
        viewModel.setMobileNo(binding.etPhoneNo.editText!!.text.toString())
        viewModel.setNationalCode(binding.etNationalCode.editText!!.text.toString().toLong())
        viewModel.getNcodeMobileVerificationCode(
            viewModel.getNationalCode().toString(),
            viewModel.getMobileNo()
        )
    }

    private fun listenToSendVerificationCode() {
        viewModel.verificationCodeResponse.observe(viewLifecycleOwner) {
            if (viewModel.isResponseOk(viewModel.verificationCodeResponse)) {
                showToast(requireContext(), viewModel.verificationCodeResponse.value!!.message!!)
                startNextFrag()
            } else if (viewModel.isResponseNotOk(viewModel.verificationCodeResponse))
                if (viewModel.verificationCodeResponse.value!!.errors.isNotEmpty())
                    showToast(
                        requireContext(),
                        viewModel.verificationCodeResponse.value!!.errors[0]
                    )
                else
                    Log.e(
                        "TAG",
                        "3listenToSendVerificationCode: ${viewModel.verificationCodeResponse.value!!.result}"
                    )
        }
    }

    private fun startNextFrag() {
/*        findNavController() // TODO("Do navigation")
            .navigate(
                R.id.action_page1EnterNcodePhoneFragment_to_fragmentPage2ReceiveVerificationSms
            )*/
        viewModel.restVerificationResponse(viewModel.verificationCodeResponse)
    }

    private fun isAllFieldIsOkay(): Boolean {
        val isPhoneNo = isPhoneNo()
        val isNationalCode = isNationalCode()
        return isPhoneNo && isNationalCode
    }

    private fun isPhoneNo(): Boolean {
        // not empty field
        if (binding.etPhoneNo.editText == null || binding.etPhoneNo.editText!!.text.isEmpty()) {
            binding.etPhoneNo.error =
                requireContext().resources.getString(R.string.error_on_empty_mobile_no)
            return false
        }
        // not wrong field
        else if (binding.etPhoneNo.editText!!.text.substring(
                0, 2
            ) != "09" || binding.etPhoneNo.editText!!.text.length != 11
        ) {
            binding.etPhoneNo.error =
                requireContext().resources.getString(R.string.error_on_wrong_mobile_no)
            return false
        }
        binding.etPhoneNo.error = null
        return true
    }

    private fun isNationalCode(): Boolean {
        // not empty field
        if (binding.etNationalCode.editText == null || binding.etNationalCode.editText!!.text.isEmpty()) {
            binding.etNationalCode.error =
                requireContext().resources.getString(R.string.error_on_empty_national_code)
            return false
        }
        // not wrong field
        else if (binding.etNationalCode.editText!!.text.length != 10) {
            binding.etNationalCode.error =
                requireContext().resources.getString(R.string.error_on_wrong_national_code)
            return false
        }
        binding.etNationalCode.error = null
        return true
    }
}