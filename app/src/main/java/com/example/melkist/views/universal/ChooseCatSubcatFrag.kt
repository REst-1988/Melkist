package com.example.melkist.views.universal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.ChoosingCatSubCatAdapter
import com.example.melkist.databinding.FragChooseCatSubcatBinding
import com.example.melkist.utils.CAT_ID_KEY
import com.example.melkist.viewmodels.ChooseCatSubCatViewModel


class ChooseCatSubcatFrag : Fragment() {

    private lateinit var binding: FragChooseCatSubcatBinding
    private val viewModel: ChooseCatSubCatViewModel by viewModels()
    lateinit var adapter: ChoosingCatSubCatAdapter
    private var arrayBundle: ArrayList<Int>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrayBundle = arguments?.getIntegerArrayList(CAT_ID_KEY)
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
        Log.e("TAG", "onResume1: ${arrayBundle!![0]}")
        Log.e("TAG", "onResume2: ${arrayBundle!![1]}")
        arrayBundle?.apply {
            if (arrayBundle!![1] != -1)
                viewModel.getFileCategoryType(
                    (activity as MainActivity).user.token!!,
                    arrayBundle!![0],
                    arrayBundle!![1]
                )
            else
                viewModel.getFileCategories(
                    (activity as MainActivity).user.token!!,
                    arrayBundle!![0]
                )
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.emptyList()
        adapter.submitList(viewModel.itemOptionList.value)
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