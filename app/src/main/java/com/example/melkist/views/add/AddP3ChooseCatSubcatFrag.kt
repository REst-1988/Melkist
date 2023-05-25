package com.example.melkist.views.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.adapters.ChoosingCatSubCatAdapter
import com.example.melkist.databinding.FragAddP3ChooseCatSubcatBinding
import com.example.melkist.viewmodels.AddItemViewModel


class AddP3ChooseCatSubcatFrag : Fragment() {

    private lateinit var binding: FragAddP3ChooseCatSubcatBinding
    private val viewModel: AddItemViewModel by activityViewModels()
    lateinit var adapter: ChoosingCatSubCatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = ChoosingCatSubCatAdapter(viewModel, this)
        binding = FragAddP3ChooseCatSubcatBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP3ChooseCatSubcatFrag
            rvList.adapter = adapter
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        when (viewModel.getReqSource()) {
            AddItemViewModel.ReqSource.CATEGORY -> viewModel.getFileCategories()
            else -> viewModel.getFileCategoryType()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.emptyList()
        adapter.submitList(viewModel.itemOptionList.value)
    }

    /******************* binding commands **************************/
    fun back() {
        findNavController().navigate(
            R.id.action_addP3ChooseCatSubcatFrag_to_addP1MainFrag
        )
    }

    fun getPageTitle(): String{
        return if (viewModel.getReqSource() == AddItemViewModel.ReqSource.CATEGORY)
            requireContext().resources.getString(R.string.choose_category_title)
        else
            requireContext().resources.getString(R.string.choose_sub_category_title)
    }
}