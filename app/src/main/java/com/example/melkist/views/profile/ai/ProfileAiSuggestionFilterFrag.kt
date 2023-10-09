package com.example.melkist.views.profile.ai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.MyFilesAdapter
import com.example.melkist.databinding.FragProfileAiSuggestionFilterBinding
import com.example.melkist.models.FileData
import com.example.melkist.utils.UNKNOWN_ERRORS_LIST
import com.example.melkist.utils.onRequestFalseResult
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel

class ProfileAiSuggestionFilterFrag : Fragment() {

    private lateinit var binding: FragProfileAiSuggestionFilterBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileAiSuggestionFilterBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfileAiSuggestionFilterFrag
            rvMyFiles.adapter = MyFilesAdapter(this@ProfileAiSuggestionFilterFrag)
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
                true -> {/*handled on binding data*/}

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

    fun choosingItem(file: FileData, isAdd: Boolean) {
        if (isAdd)
            viewModel.filterItemsForSuggestions.add(file.id)
        else
            viewModel.filterItemsForSuggestions.remove(file.id)
    }

    fun getFilterList(): MutableList<Int> {
        return viewModel.filterItemsForSuggestions
    }

    /******************** binding methods **********************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onFilterClick() {
        if (viewModel.filterItemsForSuggestions.isNotEmpty())
            findNavController().popBackStack()
        else
            showToast(requireContext(), resources.getString(R.string.empty_filter_items_suggestion))
    }

    fun onUnFilterClick() {
        viewModel.filterItemsForSuggestions.clear()
        findNavController().popBackStack()
    }

}