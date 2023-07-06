package com.example.melkist.views.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.melkist.AddActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragAddP1MainBinding
import com.example.melkist.utils.CAT_ID_KEY
import com.example.melkist.utils.CAT_RESULT_KEY
import com.example.melkist.utils.DATA
import com.example.melkist.utils.EMPTY_CATEGORY_ID
import com.example.melkist.utils.ITEM_TYPE_KEY
import com.example.melkist.utils.OWNER_ITEM_TYPE
import com.example.melkist.utils.SEEKER_ITEM_TYPE
import com.example.melkist.utils.SUB_CAT_RESULT_KEY
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
        setFragmentResultListener(ITEM_TYPE_KEY) { _, bundle ->
            when (bundle.getInt(DATA)) {
                OWNER_ITEM_TYPE -> viewModel.setItemType(AddItemViewModel.ItemType.OWNER)
                SEEKER_ITEM_TYPE -> viewModel.setItemType(AddItemViewModel.ItemType.SEEKER)
            }
            binding!!.txtChooseType.text = showTypeText()
            viewModel.resetAddItemFieldsByChoosingType()
        }
        setFragmentResultListener(CAT_RESULT_KEY) { _, bundle ->
            bundle.getStringArray(DATA)?.apply {
                viewModel.catId = this[0].toInt()
                viewModel.catTitle = this[1]
                if (this[0].toInt() != 0) {
                    viewModel.resetAddItemFieldsByChoosingCategory()
                }
            }
        }
        setFragmentResultListener(SUB_CAT_RESULT_KEY) { _, bundle ->
            bundle.getStringArray(DATA)?.apply {
                viewModel.subCatId = this[0].toInt()
                viewModel.subCatTitle = this[1]
            }
        }

    }

    /******************* binding commands **************************/
    fun cancel() {
        requireActivity().finish()
    }

    fun onChoosingType() {
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
        val array = arrayListOf(
            (activity as AddActivity).user.token,
            viewModel.getTypeId().toString(),
            EMPTY_CATEGORY_ID.toString()
        )
        val bundle = bundleOf(CAT_ID_KEY to array)
        findNavController().navigate(R.id.action_addP1MainFrag_to_addP3ChooseCatSubcatFrag, bundle)
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
        val array = arrayListOf(
            (activity as AddActivity).user.token,
            viewModel.getTypeId().toString(),
            viewModel.catId.toString()
        )
        val bundle = bundleOf(CAT_ID_KEY to array)
        findNavController().navigate(R.id.action_addP1MainFrag_to_addP3ChooseCatSubcatFrag, bundle)
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