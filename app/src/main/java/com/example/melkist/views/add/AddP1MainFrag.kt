package com.example.melkist.views.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
        // TODO: CMPL
    }

    fun showTypeText(): String {
        TODO("CMPL")
    }

    fun isShowCategory(): Boolean {
        TODO("CMPL")
    }

    fun onChoosingCategory() {
        TODO()
    }

    fun showCategoryText(): String {
        TODO()
    }

    fun isShowSubCategory(): Boolean {
        TODO("CMPL")
    }

    fun onChoosingSubCategory() {
        TODO()
    }

    fun showSubCategoryText(): String {
        TODO()
    }

    fun onProceed() {
        TODO()
    }
}