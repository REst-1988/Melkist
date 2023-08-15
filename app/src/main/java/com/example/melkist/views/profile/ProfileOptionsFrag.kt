package com.example.melkist.views.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.data.OptionsDs
import com.example.melkist.databinding.FragProfileOptionsBinding
import com.example.melkist.utils.DATA
import com.example.melkist.utils.TYPE_OPTIONS_TAG
import com.example.melkist.utils.changeAppTheme
import com.example.melkist.utils.handleSystemException
import com.example.melkist.viewmodels.OptionsViewModel
import com.example.melkist.views.universal.dialog.BottomSheetUniversalList
import kotlinx.coroutines.launch
import kotlin.Exception

class ProfileOptionsFrag : Fragment() {
    private lateinit var binding: FragProfileOptionsBinding
    private val viewModel: OptionsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getSavedOptions()
        binding = FragProfileOptionsBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfileOptionsFrag
        }
        return binding.root
    }

    private fun getSavedOptions() {
        try {
            OptionsDs.getDataStore(requireContext()).themePreferenceFlow.asLiveData()
                .observe(viewLifecycleOwner) {
                    it?.let { theme ->
                        viewModel.theme = theme
                    }
                }
            OptionsDs.getDataStore(requireContext()).notifPreferenceFlow.asLiveData()
                .observe(viewLifecycleOwner) {
                    it?.let { notifOption ->
                        viewModel.notifOption = notifOption
                    }
                }
        } catch (e: Exception){
            handleSystemException(lifecycleScope, "${(requireActivity() as MainActivity).user?.id}, ${this.javaClass.name}, getSavedOptions, ", e)
        }
    }

    /******************* binding methods *************************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onChangePasswordClick() {
        findNavController().navigate(
            R.id.action_profileOptionsFrag_to_forgetPassP1EnterNcodePhoneFrag2
        )
    }

    fun onThemeClick() {
        try {
            val bottomFrag = BottomSheetUniversalList(
                resources.getStringArray(R.array.theme_options).toList()
            )
            bottomFrag.show(childFragmentManager, TYPE_OPTIONS_TAG)
            bottomFrag.setFragmentResultListener(TYPE_OPTIONS_TAG) { _, bundle ->
                viewModel.theme = bundle.getInt(DATA)
                lifecycleScope.launch {
                    OptionsDs.getDataStore(requireContext())
                        .saveThemeOptionToPreferencesStore(viewModel.theme, requireContext())
                }
                binding.txtTheme.text = showThemeText(viewModel.theme)
                changeAppTheme(viewModel.theme)
                back()
            }
        }catch (e: Exception){
            handleSystemException(lifecycleScope, "${(requireActivity() as MainActivity).user?.id}, ${this.javaClass.name}, onThemeClick, ", e)
        }
    }

    fun showThemeText(theme: Int): String {
        return resources.getStringArray(R.array.theme_options)[theme]
    }
}