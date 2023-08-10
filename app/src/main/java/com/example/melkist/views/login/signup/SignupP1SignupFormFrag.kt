package com.example.melkist.views.login.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.LoginActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragSignupP1SignupFormBinding
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.SignupViewModel


class SignupP1SignupFormFrag : Fragment() {

    lateinit var binding: FragSignupP1SignupFormBinding
    private val viewModel: SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragSignupP1SignupFormBinding.inflate(inflater)
        if (viewModel.verificationCodeResponse.hasObservers())
            viewModel.verificationCodeResponse.removeObservers(viewLifecycleOwner)
        listenToCheckVerificationResult()
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

    override fun onResume() {
        super.onResume()
        viewModel.realEstateNameForManager?.apply {
            binding.etRealEstateName.editText?.setText(this)
        }
        viewModel.firstName?.apply {
            binding.etFirstName.editText?.setText(this)
        }
        viewModel.lastName?.apply {
            binding.etLastName.editText?.setText(this)
        }
        if (viewModel.mobileNo != "")
            binding.etPhoneNo.editText?.setText(viewModel.mobileNo)
        if (viewModel.nationalCode != 0L)
            binding.etNationalCode.editText?.setText(viewModel.nationalCode.toString())
        viewModel.email?.apply {
            binding.etEmail.editText?.setText(this)
        }
    }

    private fun listenToCheckVerificationResult() {
        viewModel.verificationCodeResponse.observe(viewLifecycleOwner) {
            when (it.result){
                true -> onTrueVerificationCodeResponse()
                false -> onFalseVerificationCodeResponse()
                else -> {}
            }
        }
    }

    private fun onTrueVerificationCodeResponse() {
        viewModel.restVerificationResponse(viewModel.verificationCodeResponse)
        findNavController().navigate(R.id.action_signupP1SignupFormFrag_to_signupP5ReceiveVerificationSmsFrag)
    }

    private fun onFalseVerificationCodeResponse() {
        if (viewModel.verificationCodeResponse.value!!.errors[0].isNotEmpty())
            showDialogWithMessage(
                requireContext(),
                concatenateText(viewModel.verificationCodeResponse.value!!.errors)
            ) { d, _ ->
                d.dismiss()
                viewModel.restVerificationResponse(viewModel.verificationCodeResponse)
            }
    }

    fun cancel() {
        viewModel.resetSignupFieldsByChoosingMainField()
        if (activity is LoginActivity)
            findNavController().navigate(R.id.action_signupP1SignupFormFrag_to_loginForm)
        else
            findNavController().navigate(R.id.action_signupP1SignupFormFrag2_to_profileManageTeamFrag)
    }

    fun onCommit() {
        if (isAllFieldIsOkay()) {
            setAllFields()
            viewModel.checkSignupData(
                requireActivity(),
                viewModel.firstName,
                viewModel.lastName,
                viewModel.realEstateNameForManager,
                viewModel.cityId,
                viewModel.mobileNo,
                viewModel.nationalCode.toString(),
                viewModel.email,
                viewModel.getSubCondition()
            )
        }
    }

    private fun setAllFields() {
        viewModel.firstName = binding.etFirstName.editText!!.text.toString()
        viewModel.lastName = binding.etLastName.editText!!.text.toString()
        viewModel.realEstateNameForManager = binding.etRealEstateName.editText?.text.toString()
        viewModel.mobileNo = binding.etPhoneNo.editText!!.text.toString()
        viewModel.nationalCode = binding.etNationalCode.editText!!.text.toString().toLong()
        viewModel.email = binding.etEmail.editText?.text.toString()
        viewModel.password = binding.etPassword.editText!!.text.toString()
    }

    private fun getRealEstateIfManager(): String? {
        if (isManager()) return binding.etRealEstateName.editText!!.text.toString()
        return null
    }

    private fun isAllFieldIsOkay(): Boolean {
        val isRealEstate = if (getRealEstateIfManager() != null) isRealState() else true
        val isFirstName = isFirstName()
        val isLastName = isLastName()
        val isPhoneNo = isPhoneNo()
        val isNationalCode = isNationalCode()
        val isEmail = isEmail()
        val isPassword = isPassword()
        if (isRealEstate && isFirstName && isLastName && isPhoneNo && isNationalCode && isEmail && isPassword) return true
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

    private fun isEmail(): Boolean {
        if (binding.etEmail.editText == null || binding.etEmail.editText!!.text.isEmpty()) {
            binding.etEmail.error = null
            return true
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.editText!!.text)
                .matches()
        ) {
            binding.etEmail.error = null
            return true
        } else {
            binding.etEmail.error =
                requireContext().resources.getString(R.string.error_on_wrong_email)
            return false
        }
    }

    private fun isPassword(): Boolean {
        return if (binding.etPassword.editText == null || binding.etPassword.editText!!.text.isEmpty()) {
            binding.etPassword.error =
                requireContext().resources.getString(R.string.error_on_empty_password)
            false
        } else if (binding.etPassword.editText!!.text.length < 8) {
            binding.etPassword.error =
                requireContext().resources.getString(R.string.error_on_wrong_password)
            false
        } else {
            binding.etPassword.error = null
            true
        }
    }

    fun showConditionText(): String {
        return when (viewModel.getCondition()) {
            SignupViewModel.Condition.CHOOSE -> requireContext().resources.getText(R.string.choose)
                .toString()
            SignupViewModel.Condition.STATE_USER -> requireContext().resources.getText(R.string.choosing_user_header)
                .toString()
            else -> requireContext().resources.getText(R.string.choosing_real_estate_header)
                .toString()
        }
    }

    fun onChoosingRealStateUser() {
        findNavController().navigate(R.id.action_signupP1SignupFormFrag_to_signupP2ChoosingRealEstateOrUserFrag)
    }

    fun onChoosingCategory() {
        findNavController().navigate(R.id.action_signupP1SignupFormFrag_to_signupP3ChoosingSubFrag)
    }

    fun showCategoryText(): String {
        return when (viewModel.getSubCondition()) {
            viewModel.SUB_STATE_CHOOSE -> requireContext().resources.getText(R.string.choose)
                .toString()
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

    fun onChoosingProvince() {
        viewModel.pcrsCondition = SignupViewModel.Pcrs.PROVINCE
        findNavController().navigate(R.id.action_signupP1SignupFormFrag_to_signupP4ChoosingCityManagerSupervisorFrag)
    }

    fun isShowProvince(): Boolean {
        return isShowCategory()
                && viewModel.subConditionRoleId != 0
    }

    fun onChoosingCity() {
        viewModel.pcrsCondition = SignupViewModel.Pcrs.CITY
        findNavController().navigate(R.id.action_signupP1SignupFormFrag_to_signupP4ChoosingCityManagerSupervisorFrag)
    }

    fun isShowCity(): Boolean {
        return isShowProvince()
                && viewModel.provinceId != 0
    }

    fun onChoosingRealEstate() {
        viewModel.pcrsCondition = SignupViewModel.Pcrs.REAL_ESTATE
        findNavController().navigate(R.id.action_signupP1SignupFormFrag_to_signupP4ChoosingCityManagerSupervisorFrag)
    }

    fun isShowRealEstate(): Boolean {
        return isShowCity()
                && viewModel.cityId != 0
                && viewModel.getSubCondition() != viewModel.SUB_STATE_MANAGER
                && viewModel.getSubCondition() != viewModel.SUB_STATE_NORMAL_USER
                && viewModel.getSubCondition() != viewModel.SUB_STATE_DEALER
    }

    fun onChoosingSupervisor() {
        viewModel.pcrsCondition = SignupViewModel.Pcrs.SUPERVISOR
        findNavController().navigate(R.id.action_signupP1SignupFormFrag_to_signupP4ChoosingCityManagerSupervisorFrag)
    }

    fun isShowSupervisor(): Boolean {
        return isShowCity()
                && viewModel.realEstateId != null
                && viewModel.getSubCondition() == viewModel.SUB_STATE_CONSOLTANT
    }

    fun isManager(): Boolean {
        return viewModel.getSubCondition() == viewModel.SUB_STATE_MANAGER
    }

    fun isShowOtherFields(): Boolean {
        return isShowCity()
                && (
                (isManager() && viewModel.cityId != 0)
                        || isSupervisor()
                        || isConsultant()
                        || isNormalUser()
                        || isDealer()
                )
    }

    private fun isSupervisor(): Boolean {
        return (viewModel.getSubCondition() == viewModel.SUB_STATE_SUPERVISER && viewModel.realEstateId != 0 && viewModel.realEstateId != null)
    }

    private fun isConsultant(): Boolean {
        return (viewModel.getSubCondition() == viewModel.SUB_STATE_CONSOLTANT && viewModel.supervisorId != 0)
    }

    private fun isNormalUser(): Boolean {
        return (viewModel.getSubCondition() == viewModel.SUB_STATE_NORMAL_USER && viewModel.cityId != 0)
    }

    private fun isDealer(): Boolean {
        return (viewModel.getSubCondition() == viewModel.SUB_STATE_DEALER && viewModel.cityId != 0)
    }
}