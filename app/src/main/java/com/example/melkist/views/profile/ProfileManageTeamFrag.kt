package com.example.melkist.views.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.TeamMemberAdapter
import com.example.melkist.databinding.FragProfileManageTeamBinding
import com.example.melkist.models.User
import com.example.melkist.viewmodels.ProfileTeamMemberViewModel


class ProfileManageTeamFrag : Fragment() {

    private lateinit var binding: FragProfileManageTeamBinding
    private val viewModel: ProfileTeamMemberViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileManageTeamBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfileManageTeamFrag
            binding.rvTeamMembers.adapter = TeamMemberAdapter(this@ProfileManageTeamFrag)
        }
        val user = (activity as MainActivity).user
        user?.apply {
            viewModel.getTeamMembers(
                token = user.token!!,
                userId = user.id!!,
                roleId = user.roleId!!
            )
        }
        return binding.root
    }


    fun choosingItemAction(user: User?) {
        viewModel.user = user
        findNavController().navigate(
            R.id.action_profileManageTeamFrag_to_profileUserProfileFrag
        )
    }

    /******************** binding methods ***************************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onAddUserClick() {
        findNavController().navigate(
            R.id.action_profileManageTeamFrag_to_signupP1SignupFormFrag2
        )
    }
}