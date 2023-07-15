package com.example.melkist.views.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragProfileUserProfileBinding
import com.example.melkist.viewmodels.ProfileTeamMemberViewModel

class ProfileUserProfileFrag : Fragment() {

    private lateinit var binding: FragProfileUserProfileBinding
    private val viewModel: ProfileTeamMemberViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileUserProfileBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            user = viewModel.user
            fragment = this@ProfileUserProfileFrag
        }
        return binding.root
    }

    /********************* binding methods **********************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onDeleteUserClick() {
        // TODO
    }
}