package com.example.melkist.views.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragProfileUserProfileBinding
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.showToast
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
            user = viewModel.teamMember
            fragment = this@ProfileUserProfileFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.deleteTeamMemberResponse.observe(viewLifecycleOwner){ response ->
            when (response.result){
                true -> {
                    showToast(requireContext(), response.message?:"")
                    back()
                }
                false -> showToast(requireContext(), concatenateText(response.errors))
                else -> Log.e("TAG", "onViewCreated: ${resources.getString(R.string.null_value)}")
            }
        }
    }

    /********************* binding methods **********************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onDeleteUserClick() {
        (activity as MainActivity).user?.apply {
            try {
                viewModel.deleteTeamMembers(requireActivity(), token!!, viewModel.teamMember!!.id!!)
            }catch (e: Exception){
                handleSystemException(lifecycleScope, "ProfileUserProfileFrag, onDeleteUserClick, ", e)
            }
        }
    }
}