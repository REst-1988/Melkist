package com.example.melkist.views.login.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
    ): View? {
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
        listentToRegisterResult()
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
                viewModel.registerUserRealstate()
                showToast(
                    requireContext(),
                    viewModel.verifyResponse.value!!.message!!
                )
                viewModel.restVerificationResponse(viewModel.verifyResponse)
            } else if (viewModel.isResponseNotOk(viewModel.verifyResponse)) {
                if (!viewModel.verifyResponse.value!!.errors[0].isEmpty())
                    showDialogWithMessage(
                        requireContext(),
                        concatenateText(viewModel.verificationCodeResponse.value!!.errors)
                    ) { d, _ ->
                        d.dismiss()
                    }
            } else
                Log.e("TAG", "listenToSendVerificationCode: ${resources.getString(R.string.somthing_goes_wrong)} ", )
            /*showToast(
                requireContext(),
                resources.getString(R.string.somthing_goes_wrong)
            )*/
        }
    }

    private fun listentToRegisterResult() {
        viewModel.registerResponse.observe(viewLifecycleOwner) {
            if (viewModel.isResponseOk(viewModel.registerResponse)) {
                viewModel.stopTimer()
                showToast(requireContext(), viewModel.registerResponse.value!!.message!!)
                startNextStep()
            } else if (viewModel.isResponseNotOk(viewModel.registerResponse)) {
                if (!viewModel.registerResponse.value!!.errors[0].isEmpty())
                    showDialogWithMessage(
                        requireContext(),
                        concatenateText(viewModel.verificationCodeResponse.value!!.errors)
                    ) { d, _ ->
                        d.dismiss()
                    }
            } else
                showToast(
                    requireContext(),
                    resources.getString(R.string.somthing_goes_wrong)
                )
        }
    }

        private fun startNextStep() {
            viewModel.restVerificationResponse(viewModel.verifyResponse)
            findNavController().navigate(R.id.action_signupP5ReceiveVerificationSmsFrag_to_LoginForm)
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
                    R.id.action_signupP5ReceiveVerificationSmsFrag_to_signupP1SignupFormFrag
                )
        }

        fun onSendVerifyCodeAgain() {
            viewModel.stopTimer()
            viewModel.checkSignupData(
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
                    viewModel.mobileNo,
                    binding.etVerificationCode.editText!!.text.toString()
                )
        }

    }