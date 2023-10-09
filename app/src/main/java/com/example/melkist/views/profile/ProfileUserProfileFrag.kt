package com.example.melkist.views.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragProfileUserProfileBinding
import com.example.melkist.utils.UNKNOWN_ERRORS_LIST
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.onRequestFalseResult
import com.example.melkist.utils.showDialogWith2Actions
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
            viewmodel = viewModel
            fragment = this@ProfileUserProfileFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.deleteTeamMemberResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    showToast(requireContext(), response.message ?: "")
                    back()
                }

                false -> onRequestFalseResult(
                    requireActivity(),
                    response.errors ?: UNKNOWN_ERRORS_LIST
                ){}
                else -> Log.e("TAG", "onViewCreated: ${resources.getString(R.string.null_value)}")
            }
        }
    }

    /********************* binding methods **********************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onDeleteUserClick() {
        try {
            showDialogWith2Actions(
                requireContext(),
                requireContext().resources.getString(R.string.delete_user_alert),
                { _, _ ->
                    (activity as MainActivity).user.apply {
                        viewModel.deleteTeamMembers(
                            requireActivity(),
                            this?.token,
                            viewModel.teamMember!!.id!!
                        )
                    }
                },
                { d, _ -> d.dismiss() }
            )
        } catch (e: Exception) {
            handleSystemException(
                lifecycleScope,
                "ProfileUserProfileFrag, onDeleteUserClick, ",
                e
            )
        }
    }
}