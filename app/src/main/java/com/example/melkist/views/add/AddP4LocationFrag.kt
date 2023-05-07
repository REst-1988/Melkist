package com.example.melkist.views.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.melkist.databinding.FragAddP4LocationBinding
import com.example.melkist.viewmodels.AddItemViewModel

class AddP4LocationFrag : Fragment() {

    private lateinit var binding: FragAddP4LocationBinding
    private val viewModel: AddItemViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragAddP4LocationBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP4LocationFrag
        }
        return binding.root
    }

    /************** binding commands **********************/
    fun back() {
        TODO()
    }
    fun cancel() {
        TODO()
    }


    fun onChoosingCity() {
        TODO()
    }

    fun showCityText(): String {
        TODO()
    }

    fun onChoosingRegion() {
        TODO("CHECk if city filed")
        TODO("CMPL")
    }

    fun showRegionText(): String {
        TODO()
    }

    fun onChoosingLocation() {
        TODO()
    }

    fun isShowOtherLocations(): Boolean{
        TODO()
    }

    fun onChoosingRegion2() {
        TODO("CHECk if city filed")
        TODO("CMPL")
    }

    fun onChoosingRegion3() {
        TODO("CHECk if city filed")
        TODO("CMPL")
    }
}