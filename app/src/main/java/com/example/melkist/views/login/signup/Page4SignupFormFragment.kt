package com.example.melkist.views.login.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragmentPage2ChoosingSubBinding
import com.example.melkist.databinding.FragmentPage4SignupFormBinding
import com.example.melkist.viewmodels.LoginViewModel
import com.google.android.material.chip.ChipGroup.LayoutParams


class Page4SignupFormFragment : Fragment() {


    lateinit var binding: FragmentPage4SignupFormBinding
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPage4SignupFormBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@Page4SignupFormFragment
        }
        showRealStateFieldIfManager()
    }

    private fun showRealStateFieldIfManager() {
        if (isManager()) {
            if (binding.etRealEstateName.visibility == View.GONE) {
                binding.etRealEstateName.visibility = View.VISIBLE
                binding.iconRealEstate.visibility = View.VISIBLE
            }
        } else {
            if (binding.etRealEstateName.visibility == View.VISIBLE) {
                binding.etRealEstateName.visibility = View.GONE
                binding.iconRealEstate.visibility = View.GONE
            }
        }
    }

    fun cancel() {
        findNavController()
            .navigate(R.id.action_page4SignupFormFragment_to_loginForm)
    }

    fun back() {
        findNavController()
            .navigate(
                R.id.action_page4SignupFormFragment_to_page3ChoosingCityManagerSupervisorFragment
            )
    }

    fun onCommit() {
        Log.e(
            "TAG", "onCommit:\n" +
                    "roleId: ${viewModel.getSubCondition()} \n" +
                    "real estate name: ${viewModel.realEstateName} \n" +
                    "first name: ${viewModel.firstName}\n" +
                    "last name: ${viewModel.lastName}\n" +
                    "mobile: ${viewModel.mobileNo}\n" +
                    "national code: ${viewModel.nationalCode}\n" +
                    "email: ${viewModel.email}\n" +
                    "pass: ${viewModel.password}"
        )

        Toast.makeText(requireContext(), "Check Log file", Toast.LENGTH_SHORT).show()

        if (isAllFieldIsOkay()) {
            viewModel.createUser(
                getRealEstateIfManager(),
                binding.etFirstName.editText!!.text.toString(),
                binding.etLastName.editText!!.text.toString(),
                binding.etPhoneNo.editText!!.toString(),
                binding.etNationalCode.editText!!.text.toString().toLong(),
                binding.etEmail.editText!!.text.toString(),
                binding.etPassword.editText!!.text.toString()
            )
        }
    }

    private fun isManager(): Boolean {
        return viewModel.getSubCondition() == viewModel.SUB_STATE_MANAGER

    }

    private fun getRealEstateIfManager(): String? {
        if (isManager())
            return binding.etRealEstateName.editText!!.text.toString()
        return null
    }

    private fun isAllFieldIsOkay(): Boolean {
        val isRealEstate = if (getRealEstateIfManager() != null) isRealState() else true
        val isFirstName = isFirstName()
        val isLastName = isLastName()
        val isPhoneNo = isPhoneNo()
        val isNationalCode = isNationalCode()
        val isPassword = isPassword()
        if (isRealEstate
            && isFirstName
            && isLastName
            && isPhoneNo
            && isNationalCode
            && isPassword
        )
            return true
        return false
    }

    private fun isRealState(): Boolean {
        // not empty field
        if (binding.etRealEstateName.editText == null || binding.etRealEstateName.editText!!.text.isEmpty()) {
            binding.etRealEstateName.error =
                requireContext().resources.getString(R.string.error_on_empty_real_estate)
            return false
        }
        // not wrong field
        else if (binding.etRealEstateName.editText!!.text.length < 3) {
            binding.etRealEstateName.error =
                requireContext().resources.getString(R.string.error_on_wrong_real_estate)
            return false
        }
        binding.etRealEstateName.error = null
        return true
    }

    private fun isFirstName(): Boolean {
        if (binding.etFirstName.editText == null || binding.etFirstName.editText!!.text.isEmpty()) {
            binding.etFirstName.error =
                requireContext().resources.getString(R.string.error_on_empty_first_name)
            return false
        }
        binding.etFirstName.error = null
        return true
    }

    private fun isLastName(): Boolean {
        if (binding.etLastName.editText == null || binding.etLastName.editText!!.text.isEmpty()) {
            binding.etLastName.error =
                requireContext().resources.getString(R.string.error_on_empty_last_name)
            return false
        }
        binding.etLastName.error = null
        return true
    }

    private fun isPhoneNo(): Boolean {
        // not empty field
        if (binding.etPhoneNo.editText == null || binding.etPhoneNo.editText!!.text.isEmpty()) {
            binding.etPhoneNo.error =
                requireContext().resources.getString(R.string.error_on_empty_mobile_no)
            return false
        }
        // not wrong field
        else if (binding.etPhoneNo.editText!!.text.substring(0, 2
            ) != "09" || binding.etPhoneNo.editText!!.text.length != 11
        ) {
            binding.etPhoneNo.error =
                requireContext().resources.getString(R.string.error_on_wrong_mobile_no)
            return false
        }
        binding.etPhoneNo.error = null
        return true
    }

    private fun isNationalCode(): Boolean {
        // not empty field
        if (binding.etNationalCode.editText == null || binding.etNationalCode.editText!!.text.isEmpty()) {
            binding.etNationalCode.error =
                requireContext().resources.getString(R.string.error_on_empty_national_code)
            return false
        }
        // not wrong field
        else if (binding.etNationalCode.editText!!.text.length != 10) {
            binding.etNationalCode.error =
                requireContext().resources.getString(R.string.error_on_wrong_national_code)
            return false
        }
        binding.etNationalCode.error = null
        return true
    }

    private fun isPassword(): Boolean {
        if (binding.etPassword.editText == null || binding.etPassword.editText!!.text.isEmpty()) {
            binding.etPassword.error =
                requireContext().resources.getString(R.string.error_on_empty_password)
            return false
        } else if (binding.etPassword.editText!!.text.length < 8) {
            binding.etPassword.error =
                requireContext().resources.getString(R.string.error_on_wrong_password)
            return false
        }
        binding.etPassword.error = null
        return true
    }


}