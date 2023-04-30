package com.example.melkist.views.login.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.melkist.databinding.FragSignupP4ChoosingCityManagerSupervisorBinding
import com.example.melkist.viewmodels.SignupViewModel

class SignupP4ChoosingCityManagerSupervisorFrag : Fragment() {

    lateinit var binding: FragSignupP4ChoosingCityManagerSupervisorBinding
    private val viewModel: SignupViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragSignupP4ChoosingCityManagerSupervisorBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@SignupP4ChoosingCityManagerSupervisorFrag
        }
        requestShowProvinces()
    }

    private fun requestShowProvinces(){
/*        val adapter = FollowAdapter()
        binding.rvFollow.adapter = adapter
        viewModel.getFollowers(username = viewModel.gitUsername.value)
        viewModel.followers.observe(viewLifecycleOwner) {
            adapter.submitList(viewModel.followers.value)
        }*/
    }

    fun nextFrag(){
        // TODO: findNavController().navigate(R.id.action_page3ChoosingCityManagerSupervisorFragment_to_page4SignupFormFragment)
    }

    fun cancel() {
        // TODO: findNavController() .navigate(R.id.action_page3ChoosingCityManagerSupervisorFragment_to_loginForm)
    }

    fun back() {
        // TODO: findNavController() .navigate( R.id.action_page3ChoosingCityManagerSupervisorFragment_to_page2ChoosingSubFragment )
    }
}