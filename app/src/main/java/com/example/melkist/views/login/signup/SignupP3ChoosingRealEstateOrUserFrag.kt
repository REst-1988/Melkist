package com.example.melkist.views.login.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragSignupP3ChoosingRealEstateOrUserBinding
import com.example.melkist.viewmodels.SignupViewModel

class SignupP3ChoosingRealEstateOrUserFrag : Fragment() {

    lateinit var binding: FragSignupP3ChoosingRealEstateOrUserBinding
    private val viewModel: SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragSignupP3ChoosingRealEstateOrUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@SignupP3ChoosingRealEstateOrUserFrag
        }
    }

    fun setState(state: Int){
        viewModel.setCondition(state)
        // TODO
/*        findNavController()
            .navigate(R.id.action_page1ChoosingRealEstateOrUserFragment_to_page2ChoosingSubFragment)*/
    }

    fun cancel(){
        // TODO
        /*findNavController().navigate(R.id.action_page1ChoosingRealEstateOrUserFragment_to_loginForm)*/
    }
}