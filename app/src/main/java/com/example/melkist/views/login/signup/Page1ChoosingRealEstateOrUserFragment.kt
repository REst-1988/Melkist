package com.example.melkist.views.login.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragmentPage1ChoosingRealEstateOrUserBinding
import com.example.melkist.viewmodels.LoginViewModel

class Page1ChoosingRealEstateOrUserFragment : Fragment() {

    lateinit var binding: FragmentPage1ChoosingRealEstateOrUserBinding
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPage1ChoosingRealEstateOrUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@Page1ChoosingRealEstateOrUserFragment
        }
    }

    fun setState(state: Int){
        viewModel.setCondition(state)
        findNavController()
            .navigate(R.id.action_page1ChoosingRealEstateOrUserFragment_to_page2ChoosingSubFragment)
    }

    fun cancel(){
        findNavController().navigate(R.id.action_page1ChoosingRealEstateOrUserFragment_to_loginForm)
    }
}