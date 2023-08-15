package com.example.melkist.views.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.data.UserDataStore
import com.example.melkist.databinding.FragmentLoginFormBinding
import com.example.melkist.models.LoginResponseModel
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.LoginViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class LoginForm : Fragment() {
    var binding: FragmentLoginFormBinding? = null
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var userDataStore: UserDataStore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.e("TAG", "onCreateView: test??? " )
        binding = FragmentLoginFormBinding.inflate(inflater, container, false)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@LoginForm
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDataStore = UserDataStore(requireContext()) // check cash for saved user
        showProperAnimation()
        listenToLoginResponse()

        //binding!!.etUsernameLoginAct.editText!!.setText("09173381951") //  Remove this line
        //binding!!.etPasswordLoginAct.editText!!.setText("12345678")//  Remove this line
    }

    private fun checkFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result // Get new FCM registration token
            Log.d(ContentValues.TAG, token)

            viewModel.login(
                requireActivity(),
                binding?.etUsernameLoginAct?.editText?.text.toString(),
                binding?.etPasswordLoginAct?.editText?.text.toString(),
                token
            )
        })
    }

    private fun showProperAnimation() {
        val animFade = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_anim)
        val animScale = AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in_anim)
        binding!!.cardLogin.startAnimation(animFade)
        binding!!.imgLogo.startAnimation(animScale)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun listenToLoginResponse() {
        viewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> onTrueResult(response)
                false -> onFalseResult(response)
                else -> Log.e(
                    "TAG",
                    "listenToCheckVerificationResult: ${resources.getString(R.string.somthing_goes_wrong)}",
                )
            }
        }
    }

    private fun onTrueResult(response: LoginResponseModel) {
        lifecycleScope.launch {
            Log.e("TAG", "listenToLoginResponse: $response  ${response.data?.isFirstTime}")
            userDataStore.saveUserToPreferencesStore(
                response
            )
        }
        binding?.etUsernameLoginAct?.error = null
        binding?.etPasswordLoginAct?.error = null
        if (response.data?.isFirstTime != false)
            findNavController().navigate(R.id.action_loginForm_to_profilePicFrag)
        else {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }

    }

    private fun onFalseResult(response: LoginResponseModel) {
        showToast(requireContext(), concatenateText(response.errors))
        binding?.etUsernameLoginAct?.error = resources.getString(R.string.wrong_username_password)
        binding?.etPasswordLoginAct?.error = resources.getString(R.string.wrong_username_password)
    }

    private fun isAllFieldIsOkay(): Boolean {
        val isPhoneNo = isPhoneNo()
        val isPassword = isPassword()
        if (isPhoneNo && isPassword) return true
        return false
    }

    private fun isPhoneNo(): Boolean {
        // not empty field
        if (binding?.etUsernameLoginAct?.editText == null || binding?.etUsernameLoginAct?.editText!!.text.isEmpty()) {
            binding?.etUsernameLoginAct?.error =
                requireContext().resources.getString(R.string.error_on_empty_mobile_no)
            return false
        }
        // not wrong field
        else if (binding?.etUsernameLoginAct?.editText!!.text.substring(
                0, 2
            ) != "09" || binding?.etUsernameLoginAct?.editText!!.text.length != 11
        ) {
            binding?.etUsernameLoginAct?.error =
                requireContext().resources.getString(R.string.error_on_wrong_mobile_no)
            return false
        }
        binding?.etUsernameLoginAct?.error = null
        return true
    }

    private fun isPassword(): Boolean {
        return if (binding?.etPasswordLoginAct?.editText == null || binding?.etPasswordLoginAct?.editText!!.text.isEmpty()) {
            binding?.etPasswordLoginAct?.error =
                requireContext().resources.getString(R.string.error_on_empty_password)
            false
        } else {
            binding?.etPasswordLoginAct?.error = null
            true
        }
    }


    /*********** Binding methods ********************/
    fun onClickLogin() {
        if (isAllFieldIsOkay())
            checkFirebaseToken()
    }

    fun onClickSignup() {
        findNavController().navigate(R.id.action_loginForm_to_signupP1SignupFormFrag)
    }

    fun onClickForgetPass() {
        findNavController().navigate(R.id.action_loginForm_to_forgetPassP1EnterNcodePhoneFrag)
    }
}