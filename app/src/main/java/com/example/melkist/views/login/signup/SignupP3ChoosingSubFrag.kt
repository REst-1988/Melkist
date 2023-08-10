package com.example.melkist.views.login.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.databinding.FragSignupP3ChoosingSubBinding
import com.example.melkist.models.Roles
import com.example.melkist.viewmodels.SignupViewModel

class SignupP3ChoosingSubFrag : Fragment() {

    lateinit var binding: FragSignupP3ChoosingSubBinding
    private val viewModel: SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragSignupP3ChoosingSubBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@SignupP3ChoosingSubFrag
        }
        checkForOptions()
    }
    
    private fun checkForOptions(){
        val roles = viewModel.getRoles()
        if (viewModel.getCondition() == SignupViewModel.Condition.STATE_REAL_ESTATE)
            makeViewReadyForRealState(roles)
        else
            makeViewReadyForNormalUser(roles)
    }

    private fun makeViewReadyForNormalUser(roles: Roles) {
        if (binding.layoutRealEstate.visibility == View.VISIBLE) {
            binding.layoutRealEstate.visibility = View.GONE
            binding.layoutNormalUser.visibility = View.VISIBLE
        }
        if (binding.txtSubChoosing4.text.isEmpty()) {
            binding.imgSubChoosing4.setImageResource(roles.normalUser.avatar!!)
            binding.txtSubChoosing4.text = roles.normalUser.title
            binding.imgSubChoosing5.setImageResource(roles.dealer.avatar!!)
            binding.txtSubChoosing5.text = roles.dealer.title
        }
    }

    private fun makeViewReadyForRealState(roles: Roles) {
        if (binding.layoutNormalUser.visibility == View.VISIBLE) {
            binding.layoutNormalUser.visibility = View.GONE
            binding.layoutRealEstate.visibility = View.VISIBLE
        }
        if (binding.txtSubChoosing1.text.isEmpty()) {
            binding.imgSubChoosing1.setImageResource(roles.consultant.avatar!!)
            binding.txtSubChoosing1.text = roles.consultant.title
            binding.imgSubChoosing2.setImageResource(roles.supervisor.avatar!!)
            binding.txtSubChoosing2.text = roles.supervisor.title
            binding.imgSubChoosing3.setImageResource(roles.manager.avatar!!)
            binding.txtSubChoosing3.text = roles.manager.title
        }
    }

    fun setSubCondition(state: Int){
        viewModel.resetSignupFieldsByChoosingCategory()
        viewModel.setSubCondition(state)
        back()
    }

    fun back(){
        findNavController().popBackStack()
    }
}