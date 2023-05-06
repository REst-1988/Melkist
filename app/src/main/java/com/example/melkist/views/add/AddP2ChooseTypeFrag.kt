package com.example.melkist.views.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.melkist.R
import com.example.melkist.databinding.FragAddP1MainBinding
import com.example.melkist.databinding.FragAddP2ChooseTypeBinding
import com.example.melkist.utils.ItemType
import com.example.melkist.viewmodels.AddItemViewModel

class AddP2ChooseTypeFrag : Fragment() {


    private lateinit var binding: FragAddP2ChooseTypeBinding
    private val viewModel: AddItemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragAddP2ChooseTypeBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP2ChooseTypeFrag
        }
        return binding.root
    }

    /******************* binding commands **************************/
    fun back() {
        //TODO: CMPL
    }

    fun setItemType(type: AddItemViewModel.ItemType){
        viewModel.setItemType(type)
        back()
    }

}