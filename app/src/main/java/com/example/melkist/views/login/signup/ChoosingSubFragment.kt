package com.example.melkist.views.login.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragmentChoosingSubBinding
import com.example.melkist.models.Roles
import com.example.melkist.viewmodels.LoginViewModel

class ChoosingSubFragment : Fragment() {

    lateinit var binding: FragmentChoosingSubBinding
    private val viewModel: LoginViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChoosingSubBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ChoosingSubFragment
        }
        checkForOptions()
    }
    
    private fun checkForOptions(){
        val roles = viewModel.getRoles()
        if (viewModel.getCondition() == viewModel.STATE_REAL_ESTATE)
            makeViewReadyForRealState(roles)
        else
            makeViewReadyForNormalUser(roles)
    }

    private fun makeViewReadyForNormalUser(roles: Roles) {
        if (binding.rl3.visibility == View.VISIBLE)
            binding.rl3.visibility = View.INVISIBLE
        binding.imgSubChoosing1.setImageResource(roles.normalUser.avatar!!)
        binding.txtSubChoosing1.text = roles.normalUser.title
        binding.imgSubChoosing2.setImageResource(roles.dealer.avatar!!)
        binding.txtSubChoosing2.text = roles.dealer.title
    }

    private fun makeViewReadyForRealState(roles: Roles) {
        if (binding.rl3.visibility == View.INVISIBLE)
            binding.rl3.visibility = View.VISIBLE
        binding.imgSubChoosing1.setImageResource(roles.consultant.avatar!!)
        binding.txtSubChoosing1.text = roles.consultant.title
        binding.imgSubChoosing2.setImageResource(roles.supervisor.avatar!!)
        binding.txtSubChoosing2.text = roles.supervisor.title
        binding.imgSubChoosing3.setImageResource(roles.manager.avatar!!)
        binding.txtSubChoosing3.text = roles.manager.title
    }

    fun setSubCondition(state: Int){
        viewModel.setCondition(state)
        findNavController()
            .navigate(R.id.action_choosingRealEstateOrUserFragment_to_choosingSubFragment)
    }

    fun cancel(){
        findNavController()
            .navigate(R.id.action_choosingSubFragment_to_loginForm)
    }

    fun back(){
        findNavController()
            .navigate(R.id.action_choosingSubFragment_to_choosingRealEstateOrUserFragment)
    }
}