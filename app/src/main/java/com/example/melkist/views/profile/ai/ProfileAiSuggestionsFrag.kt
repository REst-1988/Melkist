package com.example.melkist.views.profile.ai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.SuggestionAdapter
import com.example.melkist.databinding.FragProfileAiSuggestionsBinding
import com.example.melkist.utils.UNKNOWN_ERRORS_LIST
import com.example.melkist.utils.onRequestFalseResult
import com.example.melkist.viewmodels.MainViewModel

class ProfileAiSuggestionsFrag : Fragment() {

    private lateinit var binding: FragProfileAiSuggestionsBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: SuggestionAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileAiSuggestionsBinding.inflate(inflater)
        adapter = SuggestionAdapter(this@ProfileAiSuggestionsFrag)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfileAiSuggestionsFrag
            rvSuggestedFiles.adapter = adapter
        }
        initObserver()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pullToRefreshMainList.setOnRefreshListener {
            getSuggestedFiles()
        }
        viewModel.aiSuggestedResponse.observe(viewLifecycleOwner) { response ->
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

    override fun onResume() {
        super.onResume()
        if (viewModel.filterItemsForSuggestions.isEmpty()) {
            getSuggestedFiles()
            binding.ibtnFilter.setBackgroundResource(R.drawable.background_rounded_btns)
        } else {
            adapter.submitList(viewModel.aiSuggestionList.value!!.filter { it.myFile!!.id == viewModel.filterItemsForSuggestions[0] })
            binding.ibtnFilter.setBackgroundResource(R.drawable.background_rounded_btns_sharp)
        }
    }

    /******************** helper methods ***********************/

    private fun getSuggestedFiles() {
        (activity as MainActivity).user.apply {
            viewModel.getAiSuggestedFiles(
                requireActivity(),
                token = this?.token,
                userId = this?.id
            )
        }
    }

    private fun initObserver() {
        viewModel.aiSuggestedResponse.observe(viewLifecycleOwner) {
            binding.pullToRefreshMainList.isRefreshing = false
        }
    }

    private fun choosingItemAction(fileId: Int) {
        viewModel.resetFileAllData()
        listenToFileDetailData()
        (activity as MainActivity).user.apply {
            viewModel.getFileInfoById(
                requireActivity(),
                this?.token,
                fileId = fileId,
                this?.id
            )
        }
    }

    private fun listenToFileDetailData() {
        viewModel.fileAllData.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true ->
                    findNavController().navigate(R.id.action_profileAiSuggestionsFrag_to_fileDetailFrag)

                false -> onRequestFalseResult(
                    requireActivity(),
                    response.errors ?: UNKNOWN_ERRORS_LIST
                ) {}

                else -> {}
            }
        }
    }

    ////////////////////// public methods //////////////////////////
    fun onCardMyFileClick(myFileId: Int) {
        choosingItemAction(fileId = myFileId)
    }

    fun inCardSimilarFileClick(similarFileId: Int) {
        choosingItemAction(fileId = similarFileId)
    }

    /***************** binding methods ***********************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onFilterClicked() {
        findNavController().navigate(
            R.id.action_profileAiSuggestionsFrag_to_profileAiSuggestionFilterFrag
        )
    }
}