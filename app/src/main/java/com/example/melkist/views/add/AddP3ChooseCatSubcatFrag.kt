package com.example.melkist.views.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.melkist.R
import com.example.melkist.databinding.FragAddP2ChooseTypeBinding
import com.example.melkist.databinding.FragAddP3ChooseCatSubcatBinding
import com.example.melkist.viewmodels.AddItemViewModel


class AddP3ChooseCatSubcatFrag : Fragment() {

    private lateinit var binding: FragAddP3ChooseCatSubcatBinding
    private val viewModel: AddItemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragAddP3ChooseCatSubcatBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP3ChooseCatSubcatFrag
        }
        return binding.root
    }

    /******************* binding commands **************************/
    fun back() {
        //TODO: CMPL
    }

    fun getPageTitle(): String{
        TODO("if for cat or for sub cat")
    }

}