package com.example.melkist.views.login.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.LoginActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragSignupP5RecieveVerificationSmsBinding
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.SignupViewModel


class SignupP5ReceiveVerificationSmsFrag : Fragment() {

    private lateinit var binding: FragSignupP5RecieveVerificationSmsBinding
    private val viewModel: SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragSignupP5RecieveVerificationSmsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@SignupP5ReceiveVerificationSmsFrag
        }
        timerHandling()
        watchEtVerificationCodeField()
        listenToVerifyPhoneResult()
        listenToRegisterResult()
    }

    private fun watchEtVerificationCodeField() {
        binding.etVerificationCodeChild.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null && p0.length >= 5) {
                    onVerify()
                }
            }
        })
    }

    private fun listenToVerifyPhoneResult() {
        viewModel.verifyResponse.observe(viewLifecycleOwner) {
            when (it.result){
                true -> onTrueVerifyResponse()
                false -> onFalseVerifyResponse(it.errors)
                else -> {}
            }
        }
    }

    private fun onTrueVerifyResponse() {
        viewModel.stopTimer()
        viewModel.restVerificationResponse(viewModel.verifyResponse)
        viewModel.registerUserRealEstate(requireActivity())
    }

    private fun onFalseVerifyResponse(errors: List<String>) {
        showDialogWithMessage(
            requireContext(),
            concatenateText(errors)
        ) { d, _ ->
            d.dismiss()
            viewModel.restVerificationResponse(viewModel.verifyResponse)
        }
    }

    private fun listenToRegisterResult() {
        viewModel.registerResponse.observe(viewLifecycleOwner) {
            when (it.result){
                true -> onTrueRegisterResult(it.message?:"")
                false -> onFalseRegisterResult(it.errors)
                else -> {}
            }
        }
    }

    private fun onTrueRegisterResult(message: String) {
        viewModel.stopTimer()
        showToast(requireContext(), message)
        viewModel.resetAllData()
        startNextStep()
    }

    private fun onFalseRegisterResult(errors: List<String>) {
        showDialogWithMessage(
            requireContext(),
            concatenateText(errors)
        ) { d, _ ->
            d.dismiss()
            viewModel.restVerificationResponse(viewModel.registerResponse)
        }
    }

    private fun startNextStep() {
        if (activity is LoginActivity)
            findNavController().navigate(R.id.action_signupP5ReceiveVerificationSmsFrag_to_LoginForm)
        else
            findNavController().navigate(R.id.action_signupP5ReceiveVerificationSmsFrag2_to_profileManageTeamFrag)
        viewModel.restVerificationResponse(viewModel.verifyResponse)
    }

    override fun onStart() {
        super.onStart()
        viewModel.resetTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopTimer()
    }

    private fun timerHandling() {
        viewModel.isTimeUp.observe(viewLifecycleOwner) {
            if (viewModel.isTimeUp.value == false) {
                binding.txtTimer.visibility = View.VISIBLE
                binding.txtSendAgain.visibility = View.GONE
            } else {
                binding.txtTimer.visibility = View.GONE
                binding.txtSendAgain.visibility = View.VISIBLE
            }
        }
    }

    private fun isVerifyCodeField(): Boolean {
        // not empty field
        if (binding.etVerificationCode.editText == null || binding.etVerificationCode.editText!!.text.isEmpty()) {
            binding.etVerificationCode.error =
                requireContext().resources.getString(R.string.error_on_empty_verification_code)
            return false
        }
        // not wrong field
        else if (binding.etVerificationCode.editText!!.text.length != 5) {
            binding.etVerificationCode.error =
                requireContext().resources.getString(R.string.error_on_wrong_verification_code)
            return false
        }
        binding.etVerificationCode.error = null
        return true
    }

    fun back() {
        findNavController().popBackStack()
    }

    fun onSendVerifyCodeAgain() {
        viewModel.stopTimer()
        viewModel.checkSignupData(
            requireActivity(),
            viewModel.firstName,
            viewModel.lastName,
            viewModel.realEstateNameForManager,
            viewModel.cityId,
            viewModel.mobileNo,
            viewModel.nationalCode.toString(),
            viewModel.email,
            viewModel.getSubCondition()
        )
        viewModel.restVerificationResponse(viewModel.verifyResponse)
        viewModel.resetTimer()
    }

    fun onVerify() {
        if (isVerifyCodeField())
            viewModel.sendMobileVerificationCode(
                requireActivity(),
                viewModel.mobileNo,
                binding.etVerificationCode.editText!!.text.toString()
            )
    }

}