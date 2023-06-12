package com.example.melkist.views.universal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.ChoosingCatSubCatAdapter
import com.example.melkist.databinding.FragChooseCatSubcatBinding
import com.example.melkist.utils.CAT_ID_KEY
import com.example.melkist.utils.CAT_RESULT_KEY
import com.example.melkist.utils.DATA
import com.example.melkist.utils.EMPTY_CATEGORY_ID
import com.example.melkist.utils.ITEM_TYPE_KEY
import com.example.melkist.utils.SEEKER_ITEM_TYPE
import com.example.melkist.utils.SUB_CAT_RESULT_KEY
import com.example.melkist.viewmodels.ChooseCatSubCatViewModel


class ChooseCatSubcatFrag : Fragment() {

    private lateinit var binding: FragChooseCatSubcatBinding
    private val viewModel: ChooseCatSubCatViewModel by viewModels()
    lateinit var adapter: ChoosingCatSubCatAdapter
    private var arrayBundle: ArrayList<Int>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrayBundle = arguments?.getIntegerArrayList(CAT_ID_KEY)
        viewModel.itemType = arrayBundle!![0]
        viewModel.catId = arrayBundle!![1]
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
        Log.e("TAG", "onResume0: ${(activity as MainActivity).user.token!!}")
        arrayBundle?.apply {
            Log.e("TAG", "onResume1: ${arrayBundle!![0]} and ${viewModel.itemType}")
            Log.e("TAG", "onResume2: ${arrayBundle!![1]} and ${viewModel.catId}")
            if (viewModel.catId == EMPTY_CATEGORY_ID)
                viewModel.getFileCategories(
                    (activity as MainActivity).user.token!!,
                    viewModel.itemType
                )
            else
                viewModel.getFileCategoryType(
                    (activity as MainActivity).user.token!!,
                    viewModel.itemType,
                    viewModel.catId
                )
        }
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
        return if (arrayBundle!![1] == -1)
            requireContext().resources.getString(R.string.choose_category_title)
        else
            requireContext().resources.getString(R.string.choose_sub_category_title)
    }
}