package com.example.melkist.views.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragAddP1MainBinding
import com.example.melkist.viewmodels.AddItemViewModel


class AddP1MainFrag : Fragment() {
    private lateinit var binding: FragAddP1MainBinding
    private val viewModel: AddItemViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragAddP1MainBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP1MainFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    /******************* binding commands **************************/
    fun cancel() {
        //TODO: CMPL
    }

    fun onChoosingtype() {
        findNavController().navigate(R.id.action_addP1MainFrag_to_addP2ChooseTypeFrag)
    }

    fun showTypeText(): String {
        return when (viewModel.getItemType()) {
            AddItemViewModel.ItemType.SEEKER ->
                requireContext()
                    .resources
                    .getText(R.string.choosing_seeker_header)
                    .toString()
            AddItemViewModel.ItemType.OWNER ->
                requireContext()
                    .resources
                    .getText(R.string.choosing_owner_header)
                    .toString()
            else ->
                requireContext()
                    .resources
                    .getText(R.string.choose)
                    .toString()
        }
    }

    fun isShowCategory(): Boolean {
        return viewModel.getItemType() != AddItemViewModel.ItemType.CHOOSE
    }

    fun onChoosingCategory() {
        viewModel.setReqSource(AddItemViewModel.ReqSource.CATEGORY)
        findNavController().navigate(R.id.action_addP1MainFrag_to_addP3ChooseCatSubcatFrag)
    }

    fun showCategoryText(): String {
        return if(viewModel.catId != 0 && viewModel.catTitle.isNotEmpty())
            viewModel.catTitle
        else resources.getString(R.string.choose)
    }

    fun isShowSubCategory(): Boolean {
        return viewModel.catId != 0 && viewModel.catTitle.isNotEmpty()
    }

    fun onChoosingSubCategory() {
        viewModel.setReqSource(AddItemViewModel.ReqSource.SUB_CATEGORY)
        findNavController().navigate(R.id.action_addP1MainFrag_to_addP3ChooseCatSubcatFrag)
    }

    fun showSubCategoryText(): String {
        return if(viewModel.subCatId != 0 && viewModel.subCatTitle.isNotEmpty())
            viewModel.subCatTitle
        else resources.getString(R.string.choose)
    }

    fun isShowLocationBtn(): Boolean {
        return viewModel.subCatId != 0 && viewModel.subCatTitle.isNotEmpty()
    }

    fun onProceed() {
        findNavController().navigate(
            R.id.action_addP1MainFrag_to_addP4LocationFrag
        )
    }
}