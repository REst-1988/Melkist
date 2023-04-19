package com.example.melkist.views.login.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragmentChoosingRealEstateOrUserBinding
import com.example.melkist.viewmodels.LoginViewModel


class ChoosingRealEstateOrUserFragment : Fragment() {

    lateinit var binding: FragmentChoosingRealEstateOrUserBinding
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChoosingRealEstateOrUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ChoosingRealEstateOrUserFragment
        }
    }

    fun setState(state: Int){
        viewModel.setCondition(state)
        findNavController().navigate(R.id.action_choosingRealEstateOrUserFragment_to_choosingSubFragment)
    }

    fun cancel(){
        findNavController().navigate(R.id.action_choosingRealEstateOrUserFragment_to_loginForm)
    }
}