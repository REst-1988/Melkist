package com.example.melkist.views.login.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.databinding.FragSignupP2ChoosingRealEstateOrUserBinding
import com.example.melkist.viewmodels.SignupViewModel

class SignupP2ChoosingRealEstateOrUserFrag : Fragment() {

    lateinit var binding: FragSignupP2ChoosingRealEstateOrUserBinding
    private val viewModel: SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragSignupP2ChoosingRealEstateOrUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@SignupP2ChoosingRealEstateOrUserFrag
        }
    }

    /******************* binding commands **************************/
    fun setState(state: SignupViewModel.Condition){
        viewModel.resetSignupFieldsByChoosingMainField()
        viewModel.setCondition(state)
        onBack()
    }

    fun onBack(){
        findNavController().popBackStack()
    }
}