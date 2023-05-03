package com.example.melkist.views.login.forgetPass

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragForgetPassP2ReceiveVerificationSmsBinding
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.ForgetPassViewModel

//TODO: finish this
class ForgetPassP2ReceiveVerificationSmsFrag : Fragment() {

    private lateinit var binding: FragForgetPassP2ReceiveVerificationSmsBinding
    private val viewModel: ForgetPassViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragForgetPassP2ReceiveVerificationSmsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ForgetPassP2ReceiveVerificationSmsFrag
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
                showToast(
                    requireContext(),
                    viewModel.verificationCodeResponse.value!!.result.toString()
                )
        }
    }

    private fun startNextStep() {
        findNavController()
            .navigate(
                R.id.action_forgetPassP2ReceiveVerificationSmsFrag_to_forgetPassP3ChangePassFrag
            )
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
        findNavController()
            .navigate(
                R.id.action_forgetPassP2ReceiveVerificationSmsFrag_to_forgetPassP1EnterNcodePhoneFrag
            )
    }

    fun onSendVerifyCodeAgain() {
        viewModel.stopTimer()
        viewModel.getNcodeMobileVerificationCode(
            viewModel.getNationalCode().toString(),
            viewModel.getMobileNo()
        )
        viewModel.restVerificationResponse(viewModel.verifyResponse)
        viewModel.resetTimer()
    }

    fun onVerify() {
        if (isVerifyCodeField()) viewModel.sendMobileVerificationCode(
            viewModel.getMobileNo(), binding.etVerificationCode.editText!!.text.toString()
        )
    }
}