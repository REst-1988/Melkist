package com.example.melkist.views.login.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragSignupP1SignupFormBinding
import com.example.melkist.viewmodels.SignupViewModel


class SignupP1SignupFormFrag : Fragment() {

    lateinit var binding: FragSignupP1SignupFormBinding
    private val viewModel: SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragSignupP1SignupFormBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@SignupP1SignupFormFrag
        }
    }

    fun cancel() {
        viewModel.resetSignupFieldsByChoosingMainField()
        findNavController()
            .navigate(R.id.action_signupP1SignupFormFrag_to_loginForm)
    }

    //TODO: CMPL
    fun onCommit() {
        Log.e(
            "TAG", "onCommit:\n" +
                    "roleId: ${viewModel.getSubCondition()} \n" +
                    "real estate name: ${viewModel.realEstateNameForManager} \n" +
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

    private fun isPhoneNo(): Boolean { //TODO: NO NEED MOBILE IN THIS SECTION
        // not empty field
        if (binding.etPhoneNo.editText == null || binding.etPhoneNo.editText!!.text.isEmpty()) {
            binding.etPhoneNo.error =
                requireContext().resources.getString(R.string.error_on_empty_mobile_no)
            return false
        }
        // not wrong field
        else if (binding.etPhoneNo.editText!!.text.substring(
                0, 2
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

    fun showConditionText(): String{
        return when (viewModel.getCondition()){
            SignupViewModel.Condition.CHOOSE -> requireContext().resources.getText(R.string.choose).toString()
            SignupViewModel.Condition.STATE_USER -> requireContext().resources.getText(R.string.choosing_user_header).toString()
            else -> requireContext().resources.getText(R.string.choosing_real_estate_header).toString()
        }
    }

    fun onChoosingRealStateUser() {
        findNavController().navigate(R.id.action_signupP1SignupFormFrag_to_signupP2ChoosingRealEstateOrUserFrag)
    }

    fun onChoosingCategory() {
        findNavController().navigate(R.id.action_signupP1SignupFormFrag_to_signupP3ChoosingSubFrag)
    }

    fun showCategoryText(): String{
        return when (viewModel.getSubCondition()){
            viewModel.SUB_STATE_CHOOSE -> requireContext().resources.getText(R.string.choose).toString()
            viewModel.SUB_STATE_MANAGER -> viewModel.getRoles().manager.title
            viewModel.SUB_STATE_SUPERVISER -> viewModel.getRoles().supervisor.title
            viewModel.SUB_STATE_CONSOLTANT -> viewModel.getRoles().consultant.title
            viewModel.SUB_STATE_NORMAL_USER -> viewModel.getRoles().normalUser.title
            else -> viewModel.getRoles().dealer.title
        }
    }

    fun isShowCategory(): Boolean {
        return (viewModel.getCondition() != SignupViewModel.Condition.CHOOSE)
    }

    fun showCategoryByCondition(condition: SignupViewModel.Condition) {
        if (condition == SignupViewModel.Condition.STATE_REAL_ESTATE)
            TODO("I don't know why I write this")
    }

    fun onChoosingProvince() {
        //TODO restFields on return
        findNavController().navigate(R.id.action_signupP1SignupFormFrag_to_signupP4ChoosingCityManagerSupervisorFrag)
    }

    fun isShowProvince(): Boolean {
        return (binding.llChooseCategory.visibility == View.VISIBLE
            && viewModel.subConditionRoleId != 0
        )
    }

    fun onChoosingCity() {

        //TODO restFields on return
    }

    fun isShowCity(): Boolean {
        return (binding.llChooseProvince.visibility == View.VISIBLE
            && viewModel.provinceId != 0
        )
    }

    fun onChoosingRealEstate() {

        //TODO restFields on return
    }

    fun isShowRealEstate(): Boolean {
        return  (binding.llChooseCity.visibility == View.VISIBLE
            && viewModel.cityId != 0
        )
    }

    fun onChoosingSupervisor() {
        //TODO restFields on return  I think no need for this
    }

    fun isShowSupervisor(): Boolean {
        return (binding.llChooseRealEstate.visibility == View.VISIBLE
            && viewModel.realEstateId != 0
        )
    }

    fun isManager(): Boolean {
        return viewModel.getSubCondition() == viewModel.SUB_STATE_MANAGER
    }

    fun isShowOtherFields(): Boolean {
        return (isManager()
                && viewModel.cityId != 0)
                || isSupervisor()
                || isConsultant()
                || isNormalUser()
                || isDealer()
    }

    private fun isSupervisor(): Boolean {
        return (viewModel.getSubCondition() == viewModel.SUB_STATE_SUPERVISER
                && viewModel.realEstateId != 0)
    }

    private fun isConsultant(): Boolean {
        return (viewModel.getSubCondition() == viewModel.SUB_STATE_CONSOLTANT
                && viewModel.supervisorId != 0)
    }

    private fun isNormalUser(): Boolean {
        return (viewModel.getSubCondition() == viewModel.SUB_STATE_NORMAL_USER
                && viewModel.cityId != 0)
    }

    private fun isDealer(): Boolean {
        return (viewModel.getSubCondition() == viewModel.SUB_STATE_DEALER
                && viewModel.cityId != 0)
    }


}