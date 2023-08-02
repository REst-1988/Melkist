package com.example.melkist.views.login.forgetPass

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.melkist.LoginActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragForgetPassP1EnterNcodePhoneBinding
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.showDialogWithMessage
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
            binding.txtTitle.text = resources.getString(R.string.change_password)
            listenToSendVerificationCode()
        } catch (e: Exception) {
            handleSystemException(lifecycleScope, "ForgetPassP1EnterNcodePhoneFrag, onViewCreated, ", e)
            showToast(requireContext(), requireContext().resources.getString(R.string.global_error))
        }
    }

    fun onSend() {
        if (isAllFieldIsOkay())
            requestVerificationCodeForNcodePhone()
    }

    fun back() {
        findNavController().popBackStack()
    }

    private fun requestVerificationCodeForNcodePhone() {
        viewModel.setMobileNo(binding.etPhoneNo.editText!!.text.toString())
        viewModel.setNationalCode(binding.etNationalCode.editText!!.text.toString().toLong())
        viewModel.getNcodeMobileVerificationCode(
            requireActivity(),
            viewModel.getNationalCode().toString(),
            viewModel.getMobileNo()
        )
    }

    private fun listenToSendVerificationCode() {
        viewModel.verificationCodeResponse.observe(viewLifecycleOwner) {
            if (viewModel.isResponseOk(viewModel.verificationCodeResponse)) {
                showToast(requireContext(), viewModel.verificationCodeResponse.value!!.message!!)
                startNextFragAndResetResponse()
            } else if (viewModel.isResponseNotOk(viewModel.verificationCodeResponse))
                if (viewModel.verificationCodeResponse.value!!.errors.isNotEmpty())
                    showDialogWithMessage(
                        requireContext(),
                        concatenateText(viewModel.verificationCodeResponse.value!!.errors)
                    ) { d, _ ->
                        d.dismiss()
                    }
                else
                    Log.e("TAG", "listenToSendVerificationCode: ${resources.getString(R.string.somthing_goes_wrong)} ", )
                    /*showToast(
                        requireContext(),
                        resources.getString(R.string.somthing_goes_wrong)
                    )*/
        }
    }

    private fun startNextFragAndResetResponse() {
        Log.e("TAG", "startNextFragAndResetResponse 1: ${activity is LoginActivity}", )
        Log.e("TAG", "startNextFragAndResetResponse 2: ${activity == LoginActivity::class}", )
        Log.e("TAG", "startNextFragAndResetResponse 3: ${activity == LoginActivity::class.java}", )
        if (activity is LoginActivity)
            findNavController()
                .navigate(
                    R.id.action_forgetPassP1EnterNcodePhoneFrag_to_forgetPassP2ReceiveVerificationSmsFrag
                )
        else
            findNavController()
                .navigate(
                    R.id.action_forgetPassP1EnterNcodePhoneFrag2_to_forgetPassP2ReceiveVerificationSmsFrag2
                )
        viewModel.restVerificationResponse(viewModel.verificationCodeResponse)
        setViewModelFieldsIfVerificationCodeSent()
    }

    private fun setViewModelFieldsIfVerificationCodeSent(){
        viewModel.setMobileNo(binding.etPhoneNo.editText!!.text.toString())
        viewModel.setNationalCode(binding.etNationalCode.editText!!.text.toString().toLong())
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