package com.example.melkist.views.login.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragmentPage3ChoosingCityManagerSupervisorBinding
import com.example.melkist.viewmodels.LoginViewModel

class Page3ChoosingCityManagerSupervisorFragment : Fragment() {

    lateinit var binding: FragmentPage3ChoosingCityManagerSupervisorBinding
    private val viewModel: LoginViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPage3ChoosingCityManagerSupervisorBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@Page3ChoosingCityManagerSupervisorFragment
        }
        binding.txtTst.text =
            "condition = ${viewModel.getCondition()} subCondition = ${viewModel.getSubCondition()}"
    }

    fun nextFrag(){
        findNavController().navigate(R.id.action_page3ChoosingCityManagerSupervisorFragment_to_page4SignupFormFragment)
    }

    fun cancel() {
        findNavController()
            .navigate(R.id.action_page3ChoosingCityManagerSupervisorFragment_to_loginForm)
    }

    fun back() {
        findNavController()
            .navigate(
                R.id.action_page3ChoosingCityManagerSupervisorFragment_to_page2ChoosingSubFragment
            )
    }
}