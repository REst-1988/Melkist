package com.example.melkist.views.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragAddP6DetailsSeekerBinding
import com.example.melkist.viewmodels.AddItemViewModel

class AddP6DetailsSeekerFrag : Fragment() {
    private lateinit var binding: FragAddP6DetailsSeekerBinding
    private val viewModel: AddItemViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragAddP6DetailsSeekerBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP6DetailsSeekerFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNumPickers()
    }

    private fun setupNumPickers(){
        binding.npickerMeasureTo.maxValue = 10000
        binding.npickerMeasureTo.minValue = 0
        binding.npickerMeasureTo.value = 120

        binding.npickerMeasureFrom.maxValue = 10000
        binding.npickerMeasureFrom.minValue = 0
        binding.npickerMeasureFrom.value = 100

        binding.npickerRoomNo.maxValue = 4
        binding.npickerRoomNo.minValue = 1
        binding.npickerRoomNo.value = 2

        binding.npickerPriceFrom.maxValue = 500000
        binding.npickerPriceFrom.minValue = 500
        binding.npickerPriceFrom.value = 1000

        binding.npickerPriceTo.maxValue = 500000
        binding.npickerPriceTo.minValue = 500
        binding.npickerPriceTo.value = 2000
    }

    /************** binding commands **********************/
    fun back() {
        findNavController().navigate(R.id.action_addP6DetailsSeekerFrag_to_addP4LocationFrag)
    }

    fun cancel() {
        //TODO
    }

    fun onCommit() {
        viewModel.saveFile()
    }
}