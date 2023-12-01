package com.example.melkist.views.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.melkist.AddActivity
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.MyFilesAdapter
import com.example.melkist.databinding.FragProfileMyfilesBinding
import com.example.melkist.models.Action
import com.example.melkist.models.FileData
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.utils.UNKNOWN_ERRORS_LIST
import com.example.melkist.utils.loginRequiredDialog
import com.example.melkist.utils.onRequestFalseResult
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel

class ProfileMyFilesFrag : Fragment() {

    private lateinit var binding: FragProfileMyfilesBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileMyfilesBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfileMyFilesFrag
            rvMyFiles.adapter = MyFilesAdapter(this@ProfileMyFilesFrag)
        }
        val user = (activity as MainActivity).user
        user.apply {
            viewModel.getMyFiles(
                requireActivity(),
                token = this?.token,
                userId = this?.id
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvMyFiles.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        viewModel.myFiles.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {/*handled on databinding*/
                }

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
        viewModel.saveActionResponse.observe(viewLifecycleOwner) { response ->
            handleSaveActionResponse(response)
        }
    }

    fun choosingItemAction(file: FileData) {
        (activity as MainActivity).user.apply {
            viewModel.setFileAllDataForMyFiles(this, file)
            findNavController().navigate(
                R.id.action_profileMyFilesFrag_to_fileDetailFrag
            )
        }
    }

    fun saveAction(action: Action, fileId: Int) {
        val user = (requireActivity() as MainActivity).user
        viewModel.saveAction(
            requireActivity(),
            user?.token,
            fileId,
            user?.id,
            action.id,
            action.actionDate,
            action.actionOwnerName,
            action.actionOwnerMobile
        )
    }

    private fun handleSaveActionResponse(response: PublicResponseModel) {
        when (response.result) {
            true -> {
                showToast(requireContext(), response.message ?: "")
                viewModel.resetSaveActionResponse()
            }

            false -> {
                onRequestFalseResult(
                    requireActivity(), response.errors ?: UNKNOWN_ERRORS_LIST
                ) {}
                viewModel.resetSaveActionResponse()
            }

            null -> {}
        }
    }

    /******************** binding methods ***************************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onAddFileClick() {
        if ((activity as MainActivity).user?.id == null) {
            loginRequiredDialog(requireActivity())
            return
        }
        startActivity(
            Intent(requireActivity(), AddActivity::class.java)
        )
    }
}