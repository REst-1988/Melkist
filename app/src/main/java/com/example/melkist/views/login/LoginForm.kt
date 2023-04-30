package com.example.melkist.views.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragmentLoginFormBinding

class LoginForm : Fragment() {
    lateinit var binding: FragmentLoginFormBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animFade = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_anim)
        val animScale = AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in_anim)
        binding.cardLogin.startAnimation(animFade)
        binding.imgLogo.startAnimation(animScale)
        binding.btnSignup.setOnClickListener{
            findNavController().navigate(R.id.action_loginForm_to_signupP1EnterPhoneForRegistrationFrag)
        }
        binding.txtForgetUserPass.setOnClickListener {
            findNavController().navigate(R.id.action_loginForm_to_forgetPassP1EnterNcodePhoneFrag)
        }
        binding.btnLogin.setOnClickListener{
            // TODO: CMPL
        }
    }
}