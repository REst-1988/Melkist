package com.example.melkist.views.universal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.adapters.ChoosingCatSubCatAdapter
import com.example.melkist.databinding.FragChooseCatSubcatBinding
import com.example.melkist.utils.CAT_ID_KEY
import com.example.melkist.utils.CAT_RESULT_KEY
import com.example.melkist.utils.DATA
import com.example.melkist.utils.EMPTY_CATEGORY_ID
import com.example.melkist.utils.SUB_CAT_RESULT_KEY
import com.example.melkist.viewmodels.ChooseCatSubCatViewModel


class ChooseCatSubcatFrag : Fragment() {

    private lateinit var binding: FragChooseCatSubcatBinding
    private val viewModel: ChooseCatSubCatViewModel by viewModels()
    lateinit var adapter: ChoosingCatSubCatAdapter
    private var arrayBundle: ArrayList<String>? = arrayListOf()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrayBundle = arguments?.getStringArrayList(CAT_ID_KEY)
        arrayBundle?.apply {
            token = this[0]
            viewModel.itemType = this[1].toInt()
            viewModel.catId = this[2].toInt()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = ChoosingCatSubCatAdapter(viewModel, this)
        binding = FragChooseCatSubcatBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ChooseCatSubcatFrag
            rvList.adapter = adapter
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        arrayBundle?.apply {
            if (viewModel.catId == EMPTY_CATEGORY_ID)
                viewModel.getFileCategories(
                    requireActivity(),
                    token,
                    viewModel.itemType
                )
            else
                viewModel.getFileCategoryType(
                    requireActivity(),
                    token,
                    viewModel.itemType,
                    viewModel.catId
                )
        }
        arrayBundle?:back()
    }
    override fun onStop() {
        super.onStop()
        viewModel.emptyList()
        adapter.submitList(viewModel.itemOptionList.value)
    }

    fun arrangeResult() {
        if (viewModel.subCatId == EMPTY_CATEGORY_ID) {
            setFragmentResult(CAT_RESULT_KEY, bundleOf(DATA to viewModel.getCatArray()))
        } else
            setFragmentResult(SUB_CAT_RESULT_KEY, bundleOf(DATA to viewModel.getSubCatArray()))
        back()
    }

    /******************* binding commands **************************/
    fun back() {
        findNavController().popBackStack()
    }

    fun getPageTitle(): String {
        return if (viewModel.catId == -1)
            requireContext().resources.getString(R.string.choose_category_title)
        else
            requireContext().resources.getString(R.string.choose_sub_category_title)
    }
}