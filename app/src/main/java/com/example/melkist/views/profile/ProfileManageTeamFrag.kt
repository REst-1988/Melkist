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
import com.example.melkist.utils.UNKNOWN_ERRORS_LIST
import com.example.melkist.utils.onRequestFalseResult
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
            rvTeamMembers.adapter = TeamMemberAdapter(this@ProfileManageTeamFrag)
        }
        val user = (activity as MainActivity).user
        user.apply {
            viewModel.getTeamMembers(
                requireActivity(),
                token = this?.token,
                userId = this?.id,
                roleId = this?.roleId
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.teamMembers.observe(viewLifecycleOwner){ response ->
            when (response.result){
                true -> {/*handled on data binding*/}
                false -> {
                    viewModel.setNoDataStatus()
                    onRequestFalseResult(
                        requireActivity(),
                        response.errors ?: UNKNOWN_ERRORS_LIST
                    ) {}
                }
                else -> {}
            }
        }
    }


    fun choosingItemAction(user: User?) {
        viewModel.teamMember = user
        findNavController().navigate(
            R.id.action_profileManageTeamFrag_to_profileUserProfileFrag
        )
    }

    /******************** binding methods ***************************/
    fun back() {
        findNavController().popBackStack()
    }
}