package com.example.melkist.views.login.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.melkist.R
import com.example.melkist.databinding.FragSignupP2RecieveVerificationSmsBinding
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.SignupViewModel


class SignupP2ReceiveVerificationSmsFrag : Fragment() {

    private lateinit var binding: FragSignupP2RecieveVerificationSmsBinding
    private val viewModel: SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragSignupP2RecieveVerificationSmsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@SignupP2ReceiveVerificationSmsFrag
        }

        timerHandling()
        watchEtVerificationCodeField()
        listenToVerifyPhoneResult()
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
            if (viewModel.isResponseOk(viewModel.verifyResponse)) {
                viewModel.stopTimer()
                startNextStep()
                showToast(requireContext(), viewModel.verifyResponse.value!!.message!!)
            } else if (viewModel.isResponseNotOk(viewModel.verifyResponse)) {
                if (!viewModel.verifyResponse.value!!.errors[0].isEmpty())
                    showToast(
                        requireContext(),
                        viewModel.verifyResponse.value!!.errors[0]
                    )
            } else
                Log.e(
                    "TAG",
                    "3listenToSendVerificationCode: " +
                            "${viewModel.verificationCodeResponse.value!!.result}"
                )
        }
    }

    private fun startNextStep() {
/*            findNavController() TODO("CMPL")
                .navigate(
                    R.id.action_fragmentPage2ReceiveVerificationSms_to_page1ChoosingRealEstateOrUserFragment
                )*/
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

    fun cancel() {
        //TODO
        //findNavController().navigate(R.id.action_signupP2ReceiveVerificationSmsFrag_to_loginForm)
    }

    fun back() {
        //TODO
        //findNavController().navigate(R.id.action_signupP2ReceiveVerificationSmsFrag_to_signupP1EnterPhoneForRegistrationFrag)
    }

    fun onSendVerifyCodeAgain() {
        viewModel.stopTimer()
        viewModel.getMobileVerificationCode(viewModel.mobileNo)
        viewModel.restVerificationResponse(viewModel.verifyResponse)
        viewModel.resetTimer()
    }

    fun onVerify() {
        if (isVerifyCodeField()) viewModel.sendMobileVerificationCode(
            viewModel.mobileNo, binding.etVerificationCode.editText!!.text.toString()
        )
    }

}