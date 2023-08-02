package com.example.melkist.views.login.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.melkist.R
import com.example.melkist.adapters.ChoosingPcrsAdapter
import com.example.melkist.databinding.FragSignupP4ChoosingPcrsBinding
import com.example.melkist.viewmodels.SignupViewModel

class SignupP4ChoosingPcrsFrag: Fragment() {

    lateinit var binding: FragSignupP4ChoosingPcrsBinding
    private val viewModel: SignupViewModel by activityViewModels()
    lateinit var adapter: ChoosingPcrsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = ChoosingPcrsAdapter(
            viewModel,
            this@SignupP4ChoosingPcrsFrag
        )
        binding = FragSignupP4ChoosingPcrsBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@SignupP4ChoosingPcrsFrag
            rvList.adapter = adapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvList.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        binding.txtTitle.text = when (viewModel.pcrsCondition) {
            SignupViewModel.Pcrs.PROVINCE -> resources.getString(R.string.choose_province_title)
            SignupViewModel.Pcrs.CITY -> resources.getString(R.string.choose_city_title)
            SignupViewModel.Pcrs.REAL_ESTATE -> resources.getString(R.string.choose_real_estate_title)
            else -> resources.getString(R.string.choose_supervisor_title)
        }
        listenToSearchViewChanges()

    }

    private fun listenToSearchViewChanges(){
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(p0)
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        when (viewModel.pcrsCondition) {
            SignupViewModel.Pcrs.PROVINCE -> viewModel.getProvinces(requireActivity())
            SignupViewModel.Pcrs.CITY -> viewModel.getCities(requireActivity())
            SignupViewModel.Pcrs.REAL_ESTATE -> viewModel.getRealEstate(requireActivity())
            else -> viewModel.getSuperVisor(requireActivity())
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.emptyList()
        adapter.submitList(viewModel.pcrsList.value)
    }

    fun back() {
        when (viewModel.pcrsCondition) {
            SignupViewModel.Pcrs.PROVINCE -> viewModel.resetSignupFieldsByProvince()
            SignupViewModel.Pcrs.CITY -> viewModel.resetSignupFieldsByCity()
            SignupViewModel.Pcrs.REAL_ESTATE -> viewModel.resetSignupFieldsByRealEstate()
            else -> viewModel.resetSignupFieldsBySupervisor()
        }
        findNavController().popBackStack()
    }
}