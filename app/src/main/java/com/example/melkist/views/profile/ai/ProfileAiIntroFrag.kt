package com.example.melkist.views.profile.ai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragmentProfileAiIntroBinding
import com.example.melkist.viewmodels.MainViewModel

class ProfileAiIntroFrag : Fragment() {

    private lateinit var binding: FragmentProfileAiIntroBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileAiIntroBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfileAiIntroFrag
        }
        return binding.root
    }

    /***************** binding methods ***********************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onFilterClicked() {
        // TODO
    }
}